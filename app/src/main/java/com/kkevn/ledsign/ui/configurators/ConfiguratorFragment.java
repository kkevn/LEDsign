package com.kkevn.ledsign.ui.configurators;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.kkevn.ledsign.R;

import java.util.Arrays;
import java.util.List;

public abstract class ConfiguratorFragment extends Fragment {

    private String currentEffect = "";

    private View matrix_select, cancel_submit;

    private CheckBox cb_front, cb_right, cb_back, cb_left, cb_top;
    private List<CheckBox> cb_selections;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = createConfiguratorView(inflater, container, savedInstanceState);

        ConfiguratorListeners cl = new ConfiguratorListeners(getContext(), getFragmentManager());

        // find effect inputs
        matrix_select = (View) root.findViewById(R.id.select_matrix);
        cancel_submit = (View) root.findViewById(R.id.cancel_submit);

        // update submit button with current accent color
        int preferenceAccentColor = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(getResources().getString(R.string.pref_color_key), 0);
        cancel_submit.findViewById(R.id.b_submit).setBackgroundTintList(ColorStateList.valueOf(preferenceAccentColor));

        // apply help dialogs to help buttons
        matrix_select.findViewById(R.id.info_help).findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(getString(R.string.dialog_help_matrix_select)));

        // find check boxes
        cb_front = matrix_select.findViewById(R.id.cb_matrix_front);
        cb_right = matrix_select.findViewById(R.id.cb_matrix_right);
        cb_back = matrix_select.findViewById(R.id.cb_matrix_back);
        cb_left = matrix_select.findViewById(R.id.cb_matrix_left);
        cb_top = matrix_select.findViewById(R.id.cb_matrix_top);
        cb_selections = Arrays.asList(cb_front, cb_right, cb_back, cb_left, cb_top);

        Bundle arguments = getArguments();
        if (arguments != null) {

            if (arguments.containsKey("params") && arguments.containsKey("pos")) {
                updateInputs(getArguments().getString("params"));
                cancel_submit.findViewById(R.id.b_submit).setOnClickListener(e -> cl.onSubmitClick(currentEffect, parseInputs(), true, getArguments().getInt("pos")));
            } else {
                Toast.makeText(getContext(), "Missing Params", Toast.LENGTH_SHORT).show();
            }

        } else {
            cancel_submit.findViewById(R.id.b_submit).setOnClickListener(e -> cl.onSubmitClick(currentEffect, parseInputs(), false, -1));
        }

        cancel_submit.findViewById(R.id.b_cancel).setOnClickListener(e -> cl.onCancelClick());

        //return super.onCreateView(inflater, container, savedInstanceState);
        return root;
    }

    public abstract View createConfiguratorView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public final void setCurrentEffect(String effect) {
        currentEffect = effect;
    }

    final void updateSelections(String selections) {
        for (int i = 0; i < cb_selections.size(); i++) {
            if (selections.charAt(i) == '1') {
                cb_selections.get(i).setChecked(true);
            } else {
                cb_selections.get(i).setChecked(false);
            }
        }
    }
    abstract void updateInputs(String inputs);

    final String parseSelections() {

        String selections = "";

        for (CheckBox cb : cb_selections) {
            if (cb.isChecked()) {
                selections += "1";
            }
            else {
                selections += "0";
            }
        }

        if (selections.contains("1") == false) {
            selections = "-";
        }

        return selections;
    }

    abstract String parseInputs();

}
