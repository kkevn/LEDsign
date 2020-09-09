package com.kkevn.ledsign;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class BluetoothDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        MainActivity.pairedDevicesList();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setMessage(R.string.dialog_bt_profile)
        builder.setTitle(R.string.dialog_bt_profile)
                .setAdapter(MainActivity.mBTArrayAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        String item = MainActivity.mBTArrayAdapter.getItem(which).toString();
                        final String address = item.substring(item.length() - 17);

                        Toast.makeText(getContext(), item, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton(R.string.dialog_refresh, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing, override this button in onStart() to prevent dialog dismissal
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    // https://stackoverflow.com/questions/13746412/prevent-dialogfragment-from-dismissing-when-button-is-clicked
    @Override
    public void onStart() {
        super.onStart();

        AlertDialog d = (AlertDialog)getDialog();

        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_NEUTRAL);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean wantToCloseDialog = false;

                    MainActivity.pairedDevicesList();

                    if (wantToCloseDialog)
                        dismiss();
                }
            });
        }
    }
}
