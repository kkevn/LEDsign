package com.kkevn.ledsign.ui.configurators.widgets;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kkevn.ledsign.R;
import com.kkevn.ledsign.ui.create.Effect;

public class ToggleWidget extends ConfiguratorWidget {

    private View toggle;

    private TextView tv_label;
    private CheckBox cb_toggle;

    public ToggleWidget(Context context, View root, String effect) {

        // find matrix selection view in root layout
        toggle = (View) root.findViewById(R.id.toggle);

        // apply help dialog to help button
        String help_message = context.getString(R.string.dialog_help_toggle);
        toggle.findViewById(R.id.info_help).findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(help_message));

        // find toggle label TextView and update its text for current effect
        tv_label = toggle.findViewById(R.id.tv_toggle_label);
        tv_label.setText("" + updateLabel(effect));

        // find toggle CheckBox
        cb_toggle = toggle.findViewById(R.id.cb_toggle);
    }

    public ToggleWidget(Context context, View root, String effect, ConfiguratorWidget widget) {

        // find matrix selection view in root layout
        toggle = (View) root.findViewById(R.id.toggle);

        // apply help dialog to help button
        String help_message = context.getString(R.string.dialog_help_toggle);
        toggle.findViewById(R.id.info_help).findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(help_message));

        // find toggle label TextView and update its text for current effect
        tv_label = toggle.findViewById(R.id.tv_toggle_label);
        tv_label.setText("" + updateLabel(effect));

        // find toggle CheckBox
        cb_toggle = toggle.findViewById(R.id.cb_toggle);

        // set listener for toggle CheckBox to also disable the provided ConfiguratorWidget
        cb_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                widget.setEnabled(!b);
            }
        });
    }

    private String updateLabel(String effect) {

        // determine what attribute to use based on specified effect
        switch (effect) {
            case Effect.THEATER_CHASE:
                return "Override color with rainbow";
            default:
                return "---";
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        cb_toggle.setEnabled(enabled);
    }

    @Override
    public void updateWidgetInputs(String... inputs) {
        // get toggle status from first and only input
        int isToggled = Integer.parseInt(inputs[0]);

        // set CheckBox based on toggle status
        cb_toggle.setChecked(isToggled == 0 ? false : true);
    }

    @Override
    public String parseWidgetInputs() {

        // get the toggle status as an int
        int isToggled = cb_toggle.isChecked() ? 1 : 0;

        return "" + isToggled;
    }
}