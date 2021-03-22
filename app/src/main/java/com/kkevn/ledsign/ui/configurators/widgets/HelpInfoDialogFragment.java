/**
 * HelpInfoDialogFragment is the DialogFragment object used to display a simple help message that
 * can be dismissed. Used exclusively in the ConfiguratorWidget objects to explain their usage.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.configurators.widgets;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.kkevn.ledsign.R;

public class HelpInfoDialogFragment extends DialogFragment {

    // declare variable containing the help message
    private String msg;

    /**
     * Returns a fragment that displays a dialog window floating above the current activity's
     * window.
     *
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {Dialog} Dialog window containing a simple help message.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // fetch the help message from Bundle arguments
        msg = getArguments().getString("msg");

        // use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_help_info)
                .setMessage(msg)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // dismiss dialog
                    }
                });

        // create the AlertDialog object and return it
        return builder.create();
    }
}