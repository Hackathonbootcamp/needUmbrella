package hackathonbootcamp.needumbrella.alarm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import hackathonbootcamp.needumbrella.common.GetEnvSensorResult;
import jp.ne.docomo.smt.dev.environmentsensor.data.EnvironmentObservationData;
import jp.ne.docomo.smt.dev.environmentsensor.data.EnvironmentSensorData;
import jp.ne.docomo.smt.dev.environmentsensor.data.EnvironmentSensorResultData;

public class NeedUmbrellaSensorIntentService extends IntentService {
    private final static String TAG = NeedUmbrellaSensorIntentService.class.getSimpleName();

    protected GetEnvSensorResult gsr;

    public NeedUmbrellaSensorIntentService(String name) {
        super(name);
        gsr = new GetEnvSensorResult();
    }

    public NeedUmbrellaSensorIntentService() {
        this(TAG);
    }

    private String isRainyDay(String geoCode) {
        String returnVal = "";
        if (geoCode == "") {
            return returnVal;
        }
        try {
            EnvironmentSensorResultData resultData = gsr.get(geoCode);

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
                            if (doubleVal > 0.0) {
                                returnVal = sensorData.getName() + "で雨ふってるで。";
                            } else {
//                                //TODO テスト後に削除
//                                returnVal = sensorData.getName() + "で雨ふってるで。";
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return returnVal;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String geoBiz = sp.getString("GeoBizString", null);
        String bizResult = isRainyDay(geoBiz);
        String getHome = sp.getString("GeoHomeString", null);
        String homeResult = isRainyDay(getHome);

        if (bizResult != null || homeResult != null) {
            String alarmMsg = bizResult + homeResult + "傘忘れんときや！";
            Context context = getApplicationContext();
            Intent notification = new Intent(context, NeedUmbrellaAlarmNotificationActivity.class);
            notification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            notification.putExtra("alarmMsg", alarmMsg);
            context.startActivity(notification);
        }
    }
}
