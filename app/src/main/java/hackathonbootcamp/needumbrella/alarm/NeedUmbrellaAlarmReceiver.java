package hackathonbootcamp.needumbrella.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NeedUmbrellaAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = NeedUmbrellaAlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, NeedUmbrellaSensorIntentService.class));
    }
}
