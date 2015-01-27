package hackathonbootcamp.needumbrella;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import hackathonbootcamp.needumbrella.common.TextSpeaker;


public class DevSetting extends ActionBarActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_setting);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isAlwaysRainMode = sp.getBoolean("RainMode", false);

        Switch sw = (Switch) findViewById(R.id.isAlwaysRainSwitch);
        sw.setChecked(isAlwaysRainMode);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    //android:id="@+id/isAlwaysRainSwitch"のオンクリックメソッド
    public void onSwitchClicked(View view) {
        boolean isAlwaysRainMode;
        String resultMsg;

        Switch sw = (Switch) view;
        if(sw.isChecked()){
            isAlwaysRainMode = true;
            resultMsg = "アラームを常に雨にする設定をオンにしました。";
        }else{
            isAlwaysRainMode = false;
            resultMsg = "アラームを常に雨にする設定をオフにしました。";
        }

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putBoolean("RainMode", isAlwaysRainMode).commit();

        Toast.makeText(getApplicationContext(), resultMsg, Toast.LENGTH_LONG).show();
        TextSpeaker speaker = new TextSpeaker();
        speaker.talk(resultMsg);

        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
