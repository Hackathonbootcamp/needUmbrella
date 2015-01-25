package hackathonbootcamp.needumbrella;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import hackathonbootcamp.needumbrella.alarm.NeedUmbrellaAlarmManager;
import hackathonbootcamp.needumbrella.common.AlarmConstants;


public class TimeSetting extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);
    }
    //起床時間セット
    public void wakeUpTime(View v){

        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        NeedUmbrellaAlarmManager needUmbrellaAlarmManager = new NeedUmbrellaAlarmManager(getApplicationContext());
                        needUmbrellaAlarmManager.addAlarm(AlarmConstants.WAKE_UP_SERVICE_ID, hourOfDay, minute);
                        //設定時刻確認用
                        Toast.makeText(TimeSetting.this, "【時間設定画面】：" + hourOfDay + "時" + minute + "分" + "に起床時間をセット", Toast.LENGTH_LONG).show();
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    //出勤時間セット
    public void goOutTime(View v){

        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        NeedUmbrellaAlarmManager needUmbrellaAlarmManager = new NeedUmbrellaAlarmManager(getApplicationContext());
                        needUmbrellaAlarmManager.addAlarm(AlarmConstants.GO_OUT_SERVICE_ID, hourOfDay, minute);
                        //設定時刻確認用
                        Toast.makeText(TimeSetting.this, "【時間設定画面】：" + hourOfDay + "時" + minute + "分" + "に出社時間をセット", Toast.LENGTH_LONG).show();
                    }
                }, hour, minute, true);
        timePickerDialog.show();
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
