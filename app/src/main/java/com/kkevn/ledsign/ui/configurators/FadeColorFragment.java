package com.kkevn.ledsign.ui.configurators;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.kkevn.ledsign.R;
import com.kkevn.ledsign.ui.create.Effect;

public class FadeColorFragment extends ConfiguratorFragment {

    private String thisEffect = Effect.COLOR_FADE;

    @Override
    public View createConfiguratorView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_configure_fade_color, container, false);

        setCurrentEffect(thisEffect);




        return root;
    }

    @Override
    void updateInputs(String inputs) {
        // "10101;120;10;10"

        String trimmed_input = inputs.substring(1, inputs.length() - 1);

        String[] isolated_inputs = trimmed_input.split(";");

        updateSelections(isolated_inputs[0]);
        /*for (int i = 0; i < cb_selections.size(); i++) {
            if (isolated_inputs[0].charAt(i) == '1') {
                cb_selections.get(i).setChecked(true);
            } else {
                cb_selections.get(i).setChecked(false);
            }
        }*/

        // any other views to update...
    }

    @Override
    String parseInputs() {
        // get checkbox selections
        /*String selections = "";
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
        }*/

        int speed = 17;

        return "{" + parseSelections() + ";" + speed + ";}";
    }
}
