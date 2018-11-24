package example.a123.test42;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {
//    private static boolean incomingCall = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
             if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                 Log.d("Receiver Log", "State offhook");
                 if (intent.getStringExtra("incoming number") == null) {
                     Log.d("Receiver Log", "Outgoing call");
                 } else {
                     Log.d("Receiver Log", "Incoming call");
                 }
                //Телефон находится в режиме звонка (набор номера при исходящем звонке / разговор)
                 RecorderService.launchTask();

            } else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                 Log.d("Receiver Log", "State idle");
                //Телефон находится в ждущем режиме - это событие наступает по окончанию разговора
                //или в ситуации "отказался поднимать трубку и сбросил звонок".
                 RecorderService.recordTask.cancel(true);
                 //как работает отмена таски
            }
        }
    }
}