package com.mantovani.alarmesms;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * This class is called when a SMS is received
 */
public class IncomingSms extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of data from intent
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    //SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], "3gpp");
                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    //Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

                    SharedPreferences prefs =
                            context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                    boolean enable = prefs.getBoolean("enable", true);

                    if (enable) {
                        Intent intentActivity = new Intent(context, AlarmAcitivity.class);
                        intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentActivity.putExtra("SENDER", senderNum);
                        intentActivity.putExtra("MESSAGE", message);

                        context.startActivity(intentActivity);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }
}
