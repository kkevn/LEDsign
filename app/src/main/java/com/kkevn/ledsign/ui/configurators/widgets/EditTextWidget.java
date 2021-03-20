package com.kkevn.ledsign.ui.configurators.widgets;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.kkevn.ledsign.R;

public class EditTextWidget extends ConfiguratorWidget {

    private View edit_text;

    private EditText et_text;

    public EditTextWidget(Context context, View root) {

        // find edit text view in root layout
        edit_text = (View) root.findViewById(R.id.scroll_text);

        // apply help dialog to help button
        String help_message = context.getString(R.string.dialog_help_edit_text);
        edit_text.findViewById(R.id.info_help).findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(help_message));

        // find text field in layout
        et_text = edit_text.findViewById(R.id.et_text_to_scroll);
    }

    @Override
    public void setEnabled(boolean enabled) {
        et_text.setEnabled(enabled);
    }

    @Override
    public void updateWidgetInputs(String... inputs) {
        et_text.setText(inputs[0]);
    }

    @Override
    public String parseWidgetInputs() {

        String text = "" + et_text.getText();

        // flag input as invalid if no entry was made
        if (text.trim().isEmpty())
            text = "-";

        return text;
    }
}