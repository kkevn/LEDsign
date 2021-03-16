package com.kkevn.ledsign.ui.configurators;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kkevn.ledsign.R;
import com.kkevn.ledsign.ui.configurators.widgets.ColorPickerWidget;
import com.kkevn.ledsign.ui.configurators.widgets.MatrixSelectionsWidget;
import com.kkevn.ledsign.ui.create.Effect;

public class SolidColorFragment extends ConfiguratorFragment {

    private String thisEffect = Effect.COLOR_SOLID;

    private MatrixSelectionsWidget matrixSelections;
    private ColorPickerWidget colorPicker;

    @Override
    public View createConfiguratorView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_configure_solid_color, container, false);

        // update the Fragment so it knows what effect it is creating
        setCurrentEffect(thisEffect);

        // add MatrixSelectionsWidget object for its layout's logic
        matrixSelections = new MatrixSelectionsWidget(getContext(), root);

        // add ColorPickerWidget object for its layout's logic
        colorPicker = new ColorPickerWidget(getContext(), root);

        return root;
    }

    @Override
    void updateInputs(String inputs) {
        // separate the effect's different parameters into an array
        String trimmed_input = inputs.substring(1, inputs.length() - 1);
        String[] isolated_inputs = trimmed_input.split(";");

        // update the matrix selections to what was originally selected
        matrixSelections.updateWidgetInputs(isolated_inputs[0]);

        // update the color picker to reflect the original inputs
        colorPicker.updateWidgetInputs(isolated_inputs[1], isolated_inputs[2], isolated_inputs[3]);
    }

    @Override
    String parseInputs() {

        // fetch matrix selections
        String selections = matrixSelections.parseWidgetInputs();

        // fetch RGB color inputs from color picker
        String colors = colorPicker.parseWidgetInputs();

        // return matrix selections and effect parameters in proper format
        return "{" + selections + ";" + colors + "}";
    }
}