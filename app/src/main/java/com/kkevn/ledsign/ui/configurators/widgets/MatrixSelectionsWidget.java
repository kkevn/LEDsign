package com.kkevn.ledsign.ui.configurators.widgets;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.kkevn.ledsign.R;

import java.util.Arrays;
import java.util.List;

final public class MatrixSelectionsWidget extends ConfiguratorWidget {

    private View matrix_select;

    private CheckBox cb_front, cb_right, cb_back, cb_left, cb_top;
    private List<CheckBox> cb_selections;

    public MatrixSelectionsWidget(Context context, View root) {

        // find matrix selection view in root layout
        matrix_select = (View) root.findViewById(R.id.select_matrix);

        // apply help dialog to help button
        String help_message = context.getString(R.string.dialog_help_matrix_select);
        matrix_select.findViewById(R.id.info_help).findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(help_message));

        // find check boxes and store in convenient ordered list
        cb_front = matrix_select.findViewById(R.id.cb_matrix_front);
        cb_right = matrix_select.findViewById(R.id.cb_matrix_right);
        cb_back = matrix_select.findViewById(R.id.cb_matrix_back);
        cb_left = matrix_select.findViewById(R.id.cb_matrix_left);
        cb_top = matrix_select.findViewById(R.id.cb_matrix_top);
        cb_selections = Arrays.asList(cb_front, cb_right, cb_back, cb_left, cb_top);
    }

    @Override
    public void updateWidgetInputs(String... inputs) {

        // get selections from first and only input
        String selections = inputs[0];

        // update all matrix selections from the original input
        for (int i = 0; i < cb_selections.size(); i++) {
            cb_selections.get(i).setChecked(selections.charAt(i) == '1');
        }
    }

    @Override
    public String parseWidgetInputs() {
        String selections = "";

        // determine which matrices were selected
        for (CheckBox cb : cb_selections) {
            selections += cb.isChecked() ? "1" : "0";
        }

        // flag selections as invalid if no selection was made
        if (selections.contains("1") == false) {
            selections = "-";
        }

        return selections;
    }
}