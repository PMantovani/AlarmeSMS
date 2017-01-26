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
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    Switch enableSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asks for SMS permissions
        Utilities.askForPermissionIfNeeded(this, Manifest.permission.RECEIVE_SMS);

        // Sets enable switch to preference value
        SharedPreferences prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        boolean enable = prefs.getBoolean("enable", true);

        enableSwitch = (Switch) findViewById(R.id.enable_switch);
        enableSwitch.setChecked(enable);

        // Parse json to rule object
        String ruleJSON = prefs.getString("rule", null);
        Rule rule = new Rule();
        if (ruleJSON != null) {
            rule.addFromJsonString(ruleJSON);
        }

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


    class NewRuleDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.rule_type)
                    .setItems(R.array.rule_type, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                        }
                    });
            return builder.create();
        }
    }
}

