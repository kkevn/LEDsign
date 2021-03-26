/**
 * ColorPickerWidget is the ConfiguratorWidget responsible for accepting RGB color values using
 * SeekBar objects or an EditText object. Previews the currently set color in a simple View.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.configurators.widgets;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kkevn.ledsign.R;

public class ColorPickerWidget extends ConfiguratorWidget {

    // declare relevant variables
    private int red = 255, green = 127, blue = 0, defaultTextColor;
    private View color_picker;
    private TextView tv_red, tv_green, tv_blue;
    private SeekBar sb_red, sb_green, sb_blue;
    private EditText et_hex;

    /**
     * Constructor for this ConfiguratorWidget.
     *
     * @param {Context} context: Reference to context of the current activity.
     * @param {View} root: The parent view it will attach itself to.
     */
    public ColorPickerWidget(Context context, View root) {

        // find color picker view in root layout
        color_picker = (View) root.findViewById(R.id.color_picker);

        // apply help dialog to help button
        String help_message = context.getString(R.string.dialog_help_color_picker);
        color_picker.findViewById(R.id.info_help).findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(help_message));

        // apply listener to red seek bar
        tv_red = color_picker.findViewById(R.id.tv_red);
        defaultTextColor = tv_red.getTextColors().getDefaultColor();
        sb_red = color_picker.findViewById(R.id.sb_red);
        sb_red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                red = sb_red.getProgress();
                tv_red.setText(formatRGB(red));
                color_picker.findViewById(R.id.v_preview).setBackgroundColor(getHexColor());
                et_hex.setText(getHexString(false));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tv_red.setTextColor(Color.RED);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_red.setTextColor(defaultTextColor);
            }
        });

        // apply listener to green seek bar
        tv_green = color_picker.findViewById(R.id.tv_green);
        sb_green = color_picker.findViewById(R.id.sb_green);
        sb_green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                green = sb_green.getProgress();
                tv_green.setText(formatRGB(green));
                color_picker.findViewById(R.id.v_preview).setBackgroundColor(getHexColor());
                et_hex.setText(getHexString(false));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tv_green.setTextColor(Color.GREEN);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_green.setTextColor(defaultTextColor);
            }
        });

        // apply listener to blue seek bar
        tv_blue = color_picker.findViewById(R.id.tv_blue);
        sb_blue = color_picker.findViewById(R.id.sb_blue);
        sb_blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                blue = sb_blue.getProgress();
                tv_blue.setText(formatRGB(blue));
                color_picker.findViewById(R.id.v_preview).setBackgroundColor(getHexColor());
                et_hex.setText(getHexString(false));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tv_blue.setTextColor(Color.CYAN);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_blue.setTextColor(defaultTextColor);
            }
        });

        // apply listener to hex edit text
        et_hex = color_picker.findViewById(R.id.et_hex);
        // TODO only works on keyboard keys, software keyboard
        et_hex.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (et_hex.length() == 6) {

                    String hex = "" + et_hex.getText();

                    red = Integer.parseInt(hex.substring(0, 2), 16);
                    green = Integer.parseInt(hex.substring(2, 4), 16);
                    blue = Integer.parseInt(hex.substring(4, 6), 16);

                    tv_red.setText(formatRGB(red));
                    sb_red.setProgress(red);
                    tv_green.setText(formatRGB(green));
                    sb_green.setProgress(green);
                    tv_blue.setText(formatRGB(blue));
                    sb_blue.setProgress(blue);

                    color_picker.findViewById(R.id.v_preview).setBackgroundColor(Color.rgb(red, green, blue));
                }
                return true;
            }
        });
    }

    /**
     * Returns the currently applied RGB values as a hexadecimal integer.
     *
     * @return {int} RGB as hexadecimal.
     */
    private int getHexColor() {
        return Color.rgb(red, green, blue);
    }

    /**
     * Returns the currently applied RGB values as a hexadecimal String. Has option to keep or drop
     * the color's alpha value.
     *
     * @param {boolean} keepAlpha: Alpha status.
     *
     * @return {String} RGB as hexadecimal.
     */
    private String getHexString(boolean keepAlpha) {
        String hex = Integer.toHexString(getHexColor());
        return keepAlpha ? hex : hex.substring(2, 8);
    }

    /**
     * Returns the given RGB value as a padded String so that it is always of length three.
     *
     * @param {boolean} val: Value to format.
     *
     * @return {String} Formatted RGB value.
     */
    private String formatRGB(int val) {
        return String.format("%3s", val).replace(' ', '0');
    }

    /**
     * Determines whether or not to disable this ConfiguratorWidget based on the given enabled flag.
     *
     * @param {boolean} enabled: Enable flag.
     */
    @Override
    public void setEnabled(boolean enabled) {
        sb_red.setEnabled(enabled);
        sb_green.setEnabled(enabled);
        sb_blue.setEnabled(enabled);
        et_hex.setEnabled(enabled);
    }

    /**
     * Updates the default values of each customizable item with the given inputs.
     *
     * @param {String ...} inputs: List of inputs to update with.
     */
    @Override
    public void updateWidgetInputs(String... inputs) {

        // update RGB values with what is specified
        red = Integer.parseInt(inputs[0]);
        green = Integer.parseInt(inputs[1]);
        blue = Integer.parseInt(inputs[2]);

        // set seekbars and their labels to specified RGB values
        tv_red.setText(formatRGB(red));
        sb_red.setProgress(red);
        tv_green.setText(formatRGB(green));
        sb_green.setProgress(green);
        tv_blue.setText(formatRGB(blue));
        sb_blue.setProgress(blue);

        // set text field with specified RGB values as hex
        et_hex.setText(getHexString(false));

        // set preview color to specified RGB values
        color_picker.findViewById(R.id.v_preview).setBackgroundColor(Color.rgb(red, green, blue));
    }

    /**
     * Returns the parsed inputs of each customizable item in this ConfiguratorWidget.
     *
     * @return {String} Parsed inputs of this widget.
     */
    @Override
    public String parseWidgetInputs() {
        return "" + red + ";" + green + ";" + blue;
    }
}