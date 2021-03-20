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

    private int red = 255, green = 127, blue = 0, defaultTextColor;

    private View color_picker;

    private TextView tv_red, tv_green, tv_blue;
    private SeekBar sb_red, sb_green, sb_blue;
    private EditText et_hex;

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

    /**/
    private int getHexColor() {
        return Color.rgb(red, green, blue);
    }

    /**/
    private String getHexString(boolean keepAlpha) {
        String hex = Integer.toHexString(getHexColor());
        return keepAlpha ? hex : hex.substring(2, 8);
    }

    /**/
    private String formatRGB(int val) {
        return String.format("%3s", val).replace(' ', '0');
    }

    /**/
    @Override
    public void setEnabled(boolean enable) {
        sb_red.setEnabled(enable);
        sb_green.setEnabled(enable);
        sb_blue.setEnabled(enable);
        et_hex.setEnabled(enable);
    }

    /**/
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

    /**/
    @Override
    public String parseWidgetInputs() {
        return "" + red + ";" + green + ";" + blue;
    }
}