package com.mantovani.alarmesms;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlarmAcitivity extends AppCompatActivity {

    private Ringtone ringtone;
    private Button stopButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_acitivity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String sender = extras.getString("SENDER");
            String message = extras.getString("MESSAGE");

            TextView senderTextView = (TextView) findViewById(R.id.sender_textview);
            TextView messageTextView = (TextView) findViewById(R.id.message_textview);
            stopButton = (Button) findViewById(R.id.stop_button);

            senderTextView.setText(sender);
            messageTextView.setText(message);

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringtone.play();
        }
    }

    public void onStopAlarmButtonClick(View v) {
        ringtone.stop();

        stopButton.setText("Close app");
        stopButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }
}
