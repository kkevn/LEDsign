/**
 * SaveProfileDialogFragment is the DialogFragment object used to display the prompt for saving
 * the current active profile if the user navigated away from profile creation with unsaved changes.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class SaveProfileDialogFragment extends DialogFragment {

    /**
     * Returns a fragment that displays a dialog window floating above the current activity's
     * window.
     *
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {Dialog} Dialog window containing a prompt to save and action button for saving.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_save_profile)
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // signal main activity to save the current profile
                        MainActivity.giveSaveSignal();
                    }
                })
                .setNegativeButton(R.string.dialog_discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user cancelled the dialog, don't save
                    }
                });

        // create the AlertDialog object and return it
        return builder.create();
    }
}