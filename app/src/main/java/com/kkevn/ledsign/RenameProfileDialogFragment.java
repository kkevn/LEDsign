/**
 * RenameProfileDialogFragment is the DialogFragment object used to display the prompt for renaming
 * the current active profile. Used exclusively when selecting to rename a profile.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class RenameProfileDialogFragment extends DialogFragment {

    /**
     * Returns a fragment that displays a dialog window floating above the current activity's
     * window.
     *
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {Dialog} Dialog window containing an EditText field.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // get the layout inflater and inflate to set the layout for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        LinearLayout ll_dialog_rename = (LinearLayout) inflater.inflate(R.layout.dialog_rename, null);

        // get reference to the text field for renaming the profile and fill it with the current profile name
        EditText et_profile_name = ll_dialog_rename.findViewById(R.id.et_profile_name);

        // get reference to the current profile name received via Bundle and pre-fill the text field with it
        String current_name = getArguments().getString("profile_name");
        et_profile_name.setText(current_name);

        // use the Builder class for convenient dialog construction
        builder.setView(ll_dialog_rename)
                .setMessage(R.string.dialog_rename_profile)
                .setPositiveButton(R.string.dialog_rename, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // send new profile name to main activity
                        MainActivity.updateProfileName(et_profile_name.getText().toString());
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // user cancelled the dialog, do not rename
                    }
                });

        // create the AlertDialog object with a reference
        AlertDialog dialog = builder.create();

        // add listener for changes in new profile name as it is being typed
        et_profile_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {

                // get reference to rename confirmation button
                Button b_rename = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                // get references to existing profile names and current profile name
                String[] files = getActivity().getApplicationContext().fileList();
                String currentInput = et_profile_name.getText().toString();

                // set flag for catching invalid profile names
                boolean disableFlag;

                // set empty name as invalid
                if (currentInput.isEmpty()) {
                    disableFlag = true;
                    et_profile_name.setError(getResources().getString(R.string.dialog_empty));
                } else {

                    // no invalid names found yet
                    disableFlag = false;

                    // iterate over existing profile names
                    for (String file : files) {

                        // remove extension from current filename before comparing
                        file = file.substring(0, file.lastIndexOf('.'));

                        // matched file names are invalid (but ignore current profile name before modification)
                        if (file.equals(currentInput) && !current_name.equals(currentInput)) {
                            disableFlag = true;
                            et_profile_name.setError(getResources().getString(R.string.dialog_already_exists));
                        }
                    }
                }

                // prevent use of rename confirm button if invalid name detected, otherwise allow
                if (disableFlag) {
                    b_rename.setEnabled(false);
                } else {
                    b_rename.setEnabled(true);
                    et_profile_name.setError(null);
                }
            }
        });

        // return the AlertDialog object reference
        return dialog;
    }
}