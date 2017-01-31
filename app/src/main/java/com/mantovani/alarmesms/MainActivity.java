package com.mantovani.alarmesms;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter cAdapterSender;
    private CustomAdapter cAdapterPattern;
    public final Rule rule = new Rule();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asks for SMS permissions
        Utilities.askForPermissionIfNeeded(this, Manifest.permission.RECEIVE_SMS);

        // Sets enable switch to preference value
        SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        boolean enable = prefs.getBoolean("enable", true);

        Switch enableSwitch = (Switch) findViewById(R.id.enable_switch);
        enableSwitch.setChecked(enable);

        // Parse json to rule object
        String ruleJSON = prefs.getString("rule", null);
        if (ruleJSON != null) {
            rule.addFromJsonString(ruleJSON);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        cAdapterSender = new CustomAdapter(this, new ArrayList<String>(), "sender");
        cAdapterPattern = new CustomAdapter(this, new ArrayList<String>(), "pattern");

        ListView senderListView = (ListView) findViewById(R.id.sender_list_view);
        ListView patternListView = (ListView) findViewById(R.id.pattern_list_view);

        // Attach created custom adapters to ListView
        senderListView.setAdapter(cAdapterSender);
        patternListView.setAdapter(cAdapterPattern);

        // Add all elements stored in rule to adapter
        refreshAdapters();

        // Defines the action when clicking the floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new NewRuleDialog();
                dialog.show(getFragmentManager(), "dialog");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveRuleChange();
    }

    /**
     * Saves all rule changes to SharedPreferences in JSON format
     */
    public void saveRuleChange() {
        String json = rule.convertToJsonString();

        SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("rule", json).apply();
    }

    /**
     * Clears and readds all elements of sender and pattern adapter
     */
    public void refreshAdapters() {
        cAdapterSender.clear();
        cAdapterSender.addAll(rule.getListOfSenders());
        cAdapterPattern.clear();
        cAdapterPattern.addAll(rule.getListOfPatterns());

        saveRuleChange();
    }


    /** This method is called when enable switch changes value.
     *  The method will change the SharedPreferences to disable/enable the alarm.
     */
    public void onEnableAlarmToggle(View v) {

        Switch enableSwitch = (Switch) v;
        boolean enabled = enableSwitch.isChecked();

        SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("enable", enabled).apply();
    }





}

