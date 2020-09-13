package com.kkevn.ledsign;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.kkevn.ledsign.ui.create.CreateFragment;

public class SelectEffectDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setMessage(R.string.dialog_bt_profile)
        builder.setTitle(R.string.dialog_select_effect)
                .setAdapter(MainActivity.selv, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item

                        final String effect_name = MainActivity.selv.getItem(which).getType();
                        CreateFragment.addEffect(effect_name, "test");
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // dismiss dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
