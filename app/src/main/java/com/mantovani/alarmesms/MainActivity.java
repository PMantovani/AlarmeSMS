package com.mantovani.alarmesms;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Switch enableSwitch;
    CustomAdapter cAdapterSender;
    CustomAdapter cAdapterPattern;
    public Rule rule = new Rule();

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
    private void refreshAdapters() {
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


    class NewRuleDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.rule_type)
                    .setItems(R.array.rule_type, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            InsertDialog insertDialog = new InsertDialog();
                            insertDialog.setType(which);
                            insertDialog.show(getFragmentManager(), "insert_dialog");
                        }
                    });
            return builder.create();
        }
    }

    class InsertDialog extends DialogFragment {

        private int type;
        private String title, message;

        public void setType(int type) {
            this.type = type;
            if (type == 0) {
                title = getApplicationContext().getResources().getString(R.string.add_new_sender);
                message = getApplicationContext().getResources().getString(R.string.insert_number);
            }
            else {
                title = getApplicationContext().getResources().getString(R.string.add_new_pattern);
                message = getApplicationContext().getResources().getString(R.string.insert_pattern);
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final EditText input = new EditText(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            if (type == 0) {
                input.setInputType(InputType.TYPE_CLASS_PHONE);
            }
            else {
                input.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            input.setLayoutParams(lp);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title)
                    .setMessage(message)
                    .setView(input)
                    .setPositiveButton(getApplicationContext().getResources().getText(R.string.ok)
                            , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (type == 0) {
                                rule.addSender(input.getText().toString());
                            }
                            else {
                                rule.addPattern(input.getText().toString());

                            }
                            // Adds newly added sender or pattern to listview adapter
                            refreshAdapters();
                        }
                    })
                    .setNegativeButton(getApplicationContext().getResources().getText(R.string.cancel)
                            , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            return builder.create();
        }
    }
}

