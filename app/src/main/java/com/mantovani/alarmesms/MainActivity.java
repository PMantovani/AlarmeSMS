package com.mantovani.alarmesms;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utilities.askForPermissionIfNeeded(this, Manifest.permission.RECEIVE_SMS);
    }
}
