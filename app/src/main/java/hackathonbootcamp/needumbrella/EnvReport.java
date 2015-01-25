package hackathonbootcamp.needumbrella;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import hackathonbootcamp.needumbrella.common.GetEnvSensorResult;
import hackathonbootcamp.needumbrella.common.TextSpeaker;
import jp.ne.docomo.smt.dev.environmentsensor.data.EnvironmentObservationData;
import jp.ne.docomo.smt.dev.environmentsensor.data.EnvironmentSensorData;
import jp.ne.docomo.smt.dev.environmentsensor.data.EnvironmentSensorResultData;


public class EnvReport extends ActionBarActivity {
    private static final String TAG = EnvReport.class.getSimpleName();

    GetEnvSensorResult gsr;

    private HashMap<String, Object> homeData;
    private HashMap<String, Object> bizData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env_report);

        gsr = new GetEnvSensorResult();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String homeId = sp.getString("GeoBizString", null);
        String bizId = sp.getString("GeoHomeString", null);


        TextView homeNameView = (TextView) findViewById(R.id.rep_home_name);
        TextView homeValView = (TextView) findViewById(R.id.rep_home_val);
        homeData = new HashMap<String, Object>();
        homeData.put("id", homeId);
        homeData.put("name_view", homeNameView);
        homeData.put("val_view", homeValView);

        TextView bizNameView = (TextView) findViewById(R.id.rep_biz_name);
        TextView bizValView = (TextView) findViewById(R.id.rep_biz_val);
        bizData = new HashMap<String, Object>();
        bizData.put("id", bizId);
        bizData.put("name_view", bizNameView);
        bizData.put("val_view", bizValView);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onStart() {
        super.onStart();

        //画面に雨量最新情報を表示
        EnvReportAsyncTask homeTask = new EnvReportAsyncTask(getApplicationContext(), homeData);
        homeTask.execute();
        EnvReportAsyncTask bizTask = new EnvReportAsyncTask(getApplicationContext(), bizData);
        bizTask.execute();

        //音声通知
        String alarmMsg = getIntent().getStringExtra("alarmMsg");
        if (alarmMsg != null && alarmMsg != "") {
            TextSpeaker speaker = new TextSpeaker();
            speaker.talk(alarmMsg);
            Toast.makeText(getApplicationContext(), alarmMsg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 雨量を表示
     */
    private class EnvReportAsyncTask extends AsyncTask<Void, Integer, Long> {
        private Context context;
        private HashMap<String, Object> data;

        private String nameText;
        private String valText;


        public EnvReportAsyncTask(Context context, HashMap<String, Object> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        protected Long doInBackground(Void... params) {
            String id = (String) this.data.get("id");

            if (id == "") {
                return null;
            }
            try {
                EnvironmentSensorResultData resultData = gsr.get(id);

                ArrayList<EnvironmentSensorData> sensorList = resultData.getEnvironmentSensorDataList();
                if (sensorList != null) {
                    for (EnvironmentSensorData sensorData : sensorList) {
                        ArrayList<EnvironmentObservationData> dataList = sensorData.getEnvironmentObservationDataList();
                        if (dataList == null) {
                            continue;
                        }
                        for (EnvironmentObservationData data : dataList) {
                            ArrayList<String> valList = data.getValList();
                            for (String val : valList) {
                                double doubleVal = new Double(val);
                                Log.v(TAG, Double.toString(doubleVal));

                                //TODO 常に雨にする。テスト後に削除
                                if (doubleVal > 0.0) {
                                    this.nameText = sensorData.getName();
                                    this.valText = val;

                                    Log.v(TAG, sensorData.getName() + "で雨ふってるで。");
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

            TextView nameView = (TextView) this.data.get("name_view");
            nameView.setText(this.nameText);

            TextView valView = (TextView) this.data.get("val_view");
            valView.setText("降水量：" + this.valText + "mm");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_time_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_settings) {
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
