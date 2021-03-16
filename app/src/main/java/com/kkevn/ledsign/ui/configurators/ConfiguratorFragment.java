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

import com.kkevn.ledsign.HelpInfoDialogFragment;
import com.kkevn.ledsign.MainActivity;
import com.kkevn.ledsign.R;
import com.kkevn.ledsign.ui.create.CreateFragment;

public abstract class ConfiguratorFragment extends Fragment {

    private String currentEffect = "";

    private static ConfiguratorListener cl;

    private View cancel_submit;

    /**/
    public final void setCurrentEffect(String effect) {
        currentEffect = effect;
    }

    public final static ConfiguratorListener getCl() {
        return cl;
    }

    public static class ConfiguratorListener {
        private Context context;

        private FragmentManager fragmentManager;

        public ConfiguratorListener(Context c, FragmentManager fm) {
            context = c;
            fragmentManager = fm;
        }

        public void onCancelClick() {
            MainActivity.navigateToFragment("");
        }

        public void onSubmitClick(String effect, String param, boolean isEdit, int pos) {

            if (!param.contains("-")) {
                if (isEdit == false) {
                    CreateFragment.addEffect(effect, param);
                }
                else {
                    CreateFragment.editEffect(pos, effect, param);
                }
                MainActivity.navigateToFragment("");
            } else {
                Toast.makeText(context, R.string.configure_missing_param, Toast.LENGTH_SHORT).show();
            }
        }

        public void onHelpClick(String msg) {
            //new SelectEffectDialogFragment().show(getSupportFragmentManager(), this.getClass().getSimpleName());
            //new HelpInfoDialogFragment(msg).setShowsDialog(true);

            HelpInfoDialogFragment hidf = new HelpInfoDialogFragment();

            Bundle args = new Bundle();
            args.putString("msg", msg);
            hidf.setArguments(args);

            hidf.show(fragmentManager, this.getClass().getSimpleName());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create ConfiguratorListener object for assigning button click functions to help and cancel/submit buttons
        cl = new ConfiguratorListener(getContext(), getFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
                cancel_submit.findViewById(R.id.b_submit).setOnClickListener(e -> cl.onSubmitClick(currentEffect, parseInputs(), true, getArguments().getInt("pos")));
            } else {

                // missing arguments detected
                Toast.makeText(getContext(), "Missing Params", Toast.LENGTH_SHORT).show();
            }
        } else {
            // no Bundle with arguments found, this is an effect being added

            // inform the submit button to add this as a new effect
            cancel_submit.findViewById(R.id.b_submit).setOnClickListener(e -> cl.onSubmitClick(currentEffect, parseInputs(), false, -1));
        }

        // set listener on cancel button to return to profile creation
        cancel_submit.findViewById(R.id.b_cancel).setOnClickListener(e -> cl.onCancelClick());

        return root;
    }

    /**/
    public abstract View createConfiguratorView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**/
    abstract void updateInputs(String inputs);

    /**/
    abstract String parseInputs();
}