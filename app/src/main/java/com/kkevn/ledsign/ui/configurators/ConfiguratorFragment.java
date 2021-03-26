/**
 * ConfiguratorFragment is the template for any child ConfiguratorFragments. A ConfiguratorFragment
 * is a fragment that provides the necessary inputs for customizing its specified effect. These
 * inputs are made up of ConfiguratorWidgets and can be later parsed upon submission to populate the
 * effects list during profile creation.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.configurators;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kkevn.ledsign.ui.configurators.widgets.HelpInfoDialogFragment;
import com.kkevn.ledsign.MainActivity;
import com.kkevn.ledsign.R;
import com.kkevn.ledsign.ui.create.CreateFragment;

public abstract class ConfiguratorFragment extends Fragment {

    // declare relevant variables
    private String currentEffect = "";
    private static ConfiguratorListener configuratorListener;
    private View cancel_submit;

    /**
     * Takes note of the name of the current effect being configured.
     *
     * @param {String} effect: Name of current effect.
     */
    public final void setCurrentEffect(String effect) {
        currentEffect = effect;
    }

    /**
     * Returns access to the ConfiguratorListener object.
     *
     * @return {ConfiguratorListener} Reference to ConfiguratorListener object.
     */
    public final static ConfiguratorListener getConfiguratorListener() {
        return configuratorListener;
    }

    /**
     * ConfiguratorListener is the object that handles the listeners for button clicks on the
     * universal buttons found in each ConfiguratorFragment, namely the Cancel, Submit, and any Help
     * button.
     */
    public static class ConfiguratorListener {

        // declare relevant variables
        private Context context;
        private FragmentManager fragmentManager;

        /**
         * Constructor for the ConfiguratorListener.
         *
         * @param {Context} context: Reference to context of the current activity.
         * @param {FragmentManager} fragmentManager: Reference to the fragment manager.
         */
        public ConfiguratorListener(Context context, FragmentManager fragmentManager) {

            // initialize this object's variables
            this.context = context;
            this.fragmentManager = fragmentManager;
        }

        /**
         * Occurs when the Cancel button is pressed. Discards the current effect and returns to the
         * profile creation fragment.
         */
        public void onCancelClick() {
            MainActivity.navigateToFragment("");
        }

        /**
         * Occurs when the Submit button is pressed. Ensures valid inputs were submitted and updates
         * the effects list accordingly. Then returns to the profile creation fragment.
         *
         * @param {String} effect: Name of current effect being customized.
         * @param {String} param: Parameters of effect being customized.
         * @param {boolean} isEdit: Flag for whether or not this submission was an edit.
         * @param {int} pos: Position of effect in list being edited.
         */
        public void onSubmitClick(String effect, String param, boolean isEdit, int pos) {

            // ensure inputs are valid
            if (!param.contains("-")) {

                // add or update the effect based on edit flag
                if (isEdit == false) {
                    CreateFragment.addEffect(effect, param);
                } else {
                    CreateFragment.editEffect(pos, effect, param);
                }

                // return to the profile creation fragment
                MainActivity.navigateToFragment("");

            } else {

                // inform of invalid inputs
                Toast.makeText(context, R.string.configure_missing_param, Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * Prompts the user with a dialog containing the given help message.
         *
         * @param {String} msg: Message to display.
         */
        public void onHelpClick(String msg) {

            // create the dialog object
            HelpInfoDialogFragment hidf = new HelpInfoDialogFragment();

            // supply the dialog with the message
            Bundle args = new Bundle();
            args.putString("msg", msg);
            hidf.setArguments(args);

            // show the dialog
            hidf.show(fragmentManager, this.getClass().getSimpleName());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create ConfiguratorListener object for assigning button click functions to help and cancel/submit buttons
        configuratorListener = new ConfiguratorListener(getContext(), getFragmentManager());
    }

    /**
     * Returns the completed view that contains the entire layout of this fragment and its child
     * components of child fragments. The completed view includes adding the submit and cancel
     * buttons for submitting or discarding the effect.
     *
     * @param {LayoutInflater} inflater: LayoutInflater object used to inflate the layout.
     * @param {ViewGroup} container: Parent view that this fragment's UI should attach to.
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {View} View containing this configurator's widgets.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the fragment's layout
        View root = createConfiguratorView(inflater, container, savedInstanceState);

        // find cancel/submit buttons that all configurators have
        cancel_submit = (View) root.findViewById(R.id.cancel_submit);

        // update submit button with current accent color
        int preferenceAccentColor = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(getResources().getString(R.string.pref_color_key), 0);
        cancel_submit.findViewById(R.id.b_submit).setBackgroundTintList(ColorStateList.valueOf(preferenceAccentColor));

        // determine whether or not this effect is an edit or creation and inform relevant views
        Bundle arguments = getArguments();
        if (arguments != null) {

            // Bundle with arguments found, this is an effect being edited
            if (arguments.containsKey("params") && arguments.containsKey("pos")) {

                // update the configurator views with the effect's previous inputs
                updateInputs(getArguments().getString("params"));

                // inform the submit button to edit an existing effect rather than creating a new one
                cancel_submit.findViewById(R.id.b_submit).setOnClickListener(e -> configuratorListener.onSubmitClick(currentEffect, parseInputs(), true, getArguments().getInt("pos")));
            } else {

                // missing arguments detected
                Toast.makeText(getContext(), "Missing Params", Toast.LENGTH_SHORT).show();
            }
        } else {
            // no Bundle with arguments found, this is an effect being added

            // inform the submit button to add this as a new effect
            cancel_submit.findViewById(R.id.b_submit).setOnClickListener(e -> configuratorListener.onSubmitClick(currentEffect, parseInputs(), false, -1));
        }

        // set listener on cancel button to return to profile creation
        cancel_submit.findViewById(R.id.b_cancel).setOnClickListener(e -> configuratorListener.onCancelClick());

        return root;
    }

    /**
     * Returns a view that contains the layout of this fragment that includes all of its necessary
     * configurator widgets for customizing this effect.
     *
     * @param {LayoutInflater} inflater: LayoutInflater object used to inflate the layout.
     * @param {ViewGroup} container: Parent view that this fragment's UI should attach to.
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {View} View containing this configurator's widgets.
     */
    public abstract View createConfiguratorView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * Updates the default values of each ConfiguratorWidget with the given inputs.
     *
     * @param {String} inputs: Inputs to update with.
     */
    abstract void updateInputs(String inputs);

    /**
     * Returns the parsed inputs of each ConfiguratorWidget in this ConfiguratorFragment.
     *
     * @return {String} Parsed inputs of this fragment.
     */
    abstract String parseInputs();
}