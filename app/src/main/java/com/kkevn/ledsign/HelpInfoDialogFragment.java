package com.kkevn.ledsign;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class HelpInfoDialogFragment extends DialogFragment {

    private String msg;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        msg = getArguments().getString("msg");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_help_info)
                .setMessage(msg)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // dismiss dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

