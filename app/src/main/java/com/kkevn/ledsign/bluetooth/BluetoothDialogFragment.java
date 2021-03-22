/**
 * BluetoothDialogFragment is the DialogFragment object used to display the prompt for selecting one
 * from all available Bluetooth devices already paired with the system running the app. Successful
 * selection will attempt to establish a Bluetooth connection with the selected device. Used
 * exclusively when tapping the Bluetooth action item from the Toolbar and allowing Bluetooth.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.bluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.kkevn.ledsign.MainActivity;
import com.kkevn.ledsign.R;

public class BluetoothDialogFragment extends DialogFragment {

    /**
     * Returns a fragment that displays a dialog window floating above the current activity's
     * window.
     *
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {Dialog} Dialog window containing selectable list of all paired Bluetooth devices.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // populate the list when it first appears
        MainActivity.pairedDevicesList();

        // use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_bt_profile)
                .setAdapter(MainActivity.mBTArrayAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // retrieve the device selected from the list
                        final String address = ((PairedListViewAdapter) MainActivity.mBTArrayAdapter).getItem(which).getAddress();
                        final BluetoothDevice device = ((PairedListViewAdapter) MainActivity.mBTArrayAdapter).getItem(which);

                        // determine if valid BluetoothDevice address was selected
                        if (!address.equals("-1")) {

                            // initiate a new thread to handle connecting to the remote device via bluetooth
                            new ConnectThread(device, MainActivity.mBTAdapter, MainActivity.handler).start();
                        } else {
                            Snackbar.make(getView(), R.string.notify_no_paired_bt, Snackbar.LENGTH_LONG).show();
                        }
                    }
                })
                .setNeutralButton(R.string.dialog_refresh, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing, override this button in onStart() to prevent dialog dismissal
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // dismiss dialog
                    }
                });

        // create the AlertDialog object and return it
        return builder.create();
    }

    /**
     * Called when this becomes visible to the user during its lifecycle.
     *
     * @source https://stackoverflow.com/questions/13746412/prevent-dialogfragment-from-dismissing-when-button-is-clicked
     */
    @Override
    public void onStart() {
        super.onStart();

        // get reference to this AlertDialog
        AlertDialog alertDialog = (AlertDialog) getDialog();

        // apply listener to this dialog's neutral button
        if (alertDialog != null) {
            Button neutralButton = (Button) alertDialog.getButton(Dialog.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean wantToCloseDialog = false;

                    // refresh the list of paired Bluetooth devices
                    MainActivity.pairedDevicesList();

                    if (wantToCloseDialog)
                        dismiss();
                }
            });
        }
    }
}