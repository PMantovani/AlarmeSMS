package com.mantovani.alarmesms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by Pedro on 31-Jan-17.
 * Dialog class used for inserting new rule
 */

public class InsertDialog extends DialogFragment {

    private int type;
    private String title, message;
    private Context context;

    public void setType(int type) {
        this.type = type;
        if (type == 0) {
            title = context.getResources().getString(R.string.add_new_sender);
            message = context.getResources().getString(R.string.insert_number);
        }
        else {
            title = context.getResources().getString(R.string.add_new_pattern);
            message = context.getResources().getString(R.string.insert_pattern);
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final EditText input = new EditText(getActivity());
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
        // Shows keyboard
        input.requestFocus();
        final InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setView(input)
                .setPositiveButton(getActivity().getResources().getText(R.string.ok)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Hides keyboard
                                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                                MainActivity mainActivity = (MainActivity) getActivity();

                                if (type == 0) {
                                    mainActivity.rule.addSender(input.getText().toString());
                                }
                                else {
                                    mainActivity.rule.addPattern(input.getText().toString());

                                }
                                // Adds newly added sender or pattern to listview adapter
                                mainActivity.refreshAdapters();
                            }
                        })
                .setNegativeButton(getActivity().getResources().getText(R.string.cancel)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
        return builder.create();
    }
}