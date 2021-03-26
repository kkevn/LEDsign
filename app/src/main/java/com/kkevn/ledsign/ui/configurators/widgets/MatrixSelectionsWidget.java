/**
 * MatrixSelectionsWidget is the ConfiguratorWidget responsible for accepting any combination of
 * matrices that will be customized in the given effect.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.configurators.widgets;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import com.kkevn.ledsign.R;

import java.util.Arrays;
import java.util.List;

final public class MatrixSelectionsWidget extends ConfiguratorWidget {

    // declare relevant variables
    private View matrix_select;
    private CheckBox cb_front, cb_right, cb_back, cb_left, cb_top;
    private List<CheckBox> cb_selections;

    /**
     * Constructor for this ConfiguratorWidget.
     *
     * @param {Context} context: Reference to context of the current activity.
     * @param {View} root: The parent view it will attach itself to.
     */
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

    /**
     * Determines whether or not to disable this ConfiguratorWidget based on the given enabled flag.
     *
     * @param {boolean} enabled: Enable flag.
     */
    @Override
    public void setEnabled(boolean enabled) {
        for (int i = 0; i < cb_selections.size(); i++) {
            cb_selections.get(i).setEnabled(enabled);
        }
    }

    /**
     * Updates the default values of each customizable item with the given inputs.
     *
     * @param {String ...} inputs: List of inputs to update with.
     */
    @Override
    public void updateWidgetInputs(String... inputs) {

        // get selections from first and only input
        String selections = inputs[0];

        // update all matrix selections from the original input
        for (int i = 0; i < cb_selections.size(); i++) {
            cb_selections.get(i).setChecked(selections.charAt(i) == '1');
        }
    }

    /**
     * Returns the parsed inputs of each customizable item in this ConfiguratorWidget.
     *
     * @return {String} Parsed inputs of this widget.
     */
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