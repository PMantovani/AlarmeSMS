package com.mantovani.alarmesms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Pedro on 31-Jan-17.
 * Dialog class for creating new rule
 */

public class NewRuleDialog extends DialogFragment {

    Context context;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.rule_type)
                .setItems(R.array.rule_type, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        InsertDialog insertDialog = new InsertDialog();
                        insertDialog.setContext(context);
                        insertDialog.setType(which);
                        insertDialog.show(getFragmentManager(), "insert_dialog");
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }
}