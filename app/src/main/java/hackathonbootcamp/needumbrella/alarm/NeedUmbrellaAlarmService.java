package hackathonbootcamp.needumbrella.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NeedUmbrellaAlarmService extends Service {
    private static final String TAG = NeedUmbrellaAlarmService.class.getSimpleName();

    Runnable mTask = new Runnable() {
        public void run() {
            Intent alarmBroadcast = new Intent();
            alarmBroadcast.setAction("NeedUmbrellaAlarmAction");
            sendBroadcast(alarmBroadcast);
            NeedUmbrellaAlarmService.this.stopSelf();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        new Thread(null, mTask, "NeedUmbrellaAlarmServiceThread").start();
    }
}
