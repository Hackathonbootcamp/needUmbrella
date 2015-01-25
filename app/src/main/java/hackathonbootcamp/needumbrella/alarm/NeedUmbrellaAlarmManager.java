package hackathonbootcamp.needumbrella.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

/**
 * 利用例
 * Button button = (Button)findViewById(R.id.button);
 * button.setOnClickListener(new View.OnClickListener() {
 *   public void onClick(View v) {
 *     Button button = (Button)v;
 *     NeedUmbrellaAlarmManager am = new NeedUmbrellaAlarmManager(getApplicationContext());
 *     am.addAlarm(AlarmConstants.WAKE_UP_SERVICE_ID, 21, 26);
 *     am.addAlarm(AlarmConstants.GO_OUT_SERVICE_ID, 21, 27);
 *   }
 * });
 */
public class NeedUmbrellaAlarmManager {
    private static final String TAG = NeedUmbrellaAlarmManager.class.getSimpleName();

    Context c;
    AlarmManager am;
    private PendingIntent pi;

    public NeedUmbrellaAlarmManager(Context c) {
        this.c = c;
        am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
    }

    public void addAlarm(int serviceId, int alarmHour, int alarmMinute) {
        pi = this.getPendingIntent(serviceId);

        //起動時間設定
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, alarmHour);
        cal.set(Calendar.MINUTE, alarmMinute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (cal.getTimeInMillis() < System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        am.setRepeating(AlarmManager.RTC, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
        Toast.makeText(c, String.format("%02d時%02d分に予約しました。(サービスID:%02d)", alarmHour, alarmMinute, serviceId), Toast.LENGTH_LONG).show();
    }

    public void stopAlarm() {
        am.cancel(pi);
    }

    private PendingIntent getPendingIntent(int serviceId) {
        Intent intent = new Intent(c, NeedUmbrellaAlarmService.class);
        return PendingIntent.getService(c, serviceId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
