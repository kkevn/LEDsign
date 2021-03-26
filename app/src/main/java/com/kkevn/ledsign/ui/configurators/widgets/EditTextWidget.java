/**
 * EditTextWidget is the ConfiguratorWidget responsible for accepting a phrase or word through text
 * using an EditText object.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.configurators.widgets;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.kkevn.ledsign.R;

public class EditTextWidget extends ConfiguratorWidget {

    // declare relevant variables
    private View edit_text;
    private EditText et_text;

    /**
     * Constructor for this ConfiguratorWidget.
     *
     * @param {Context} context: Reference to context of the current activity.
     * @param {View} root: The parent view it will attach itself to.
     */
    public EditTextWidget(Context context, View root) {

        // find edit text view in root layout
        edit_text = (View) root.findViewById(R.id.scroll_text);

        // apply help dialog to help button
        String help_message = context.getString(R.string.dialog_help_edit_text);
        edit_text.findViewById(R.id.info_help).findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(help_message));

        // find text field in layout
        et_text = edit_text.findViewById(R.id.et_text_to_scroll);
    }

    /**
     * Determines whether or not to disable this ConfiguratorWidget based on the given enabled flag.
     *
     * @param {boolean} enabled: Enable flag.
     */
    @Override
    public void setEnabled(boolean enabled) {
        et_text.setEnabled(enabled);
    }

    /**
     * Updates the default values of each customizable item with the given inputs.
     *
     * @param {String ...} inputs: List of inputs to update with.
     */
    @Override
    public void updateWidgetInputs(String... inputs) {
        et_text.setText(inputs[0]);
    }

    /**
     * Returns the parsed inputs of each customizable item in this ConfiguratorWidget.
     *
     * @return {String} Parsed inputs of this widget.
     */
    @Override
    public String parseWidgetInputs() {

        String text = "" + et_text.getText();

        // flag input as invalid if no entry was made
        if (text.trim().isEmpty())
            text = "-";

        return text;
    }
}