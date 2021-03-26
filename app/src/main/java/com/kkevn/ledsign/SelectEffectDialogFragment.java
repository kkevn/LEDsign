/**
 * SelectEffectDialogFragment is the DialogFragment object used to display the prompt for selecting
 * one of all available effects that can appear in the 3D sketch. Successful selection navigates the
 * user to the selected effect's configuration page. Used exclusively when tapping the
 * FloatingActionButton during profile creation.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class SelectEffectDialogFragment extends DialogFragment {

    /**
     * Returns a fragment that displays a dialog window floating above the current activity's
     * window.
     *
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {Dialog} Dialog window containing selectable list of all available effects.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_select_effect)
                .setAdapter(MainActivity.selectEffectListViewAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // obtain the selected effect and navigate to its ConfiguratorFragment
                        final String effect_name = MainActivity.selectEffectListViewAdapter.getItem(which).getType();
                        MainActivity.navigateToFragment(effect_name);
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
}