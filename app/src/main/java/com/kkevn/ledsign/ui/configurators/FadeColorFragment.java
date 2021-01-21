package com.kkevn.ledsign.ui.configurators;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kkevn.ledsign.R;
import com.kkevn.ledsign.ui.create.Effect;

import java.util.Arrays;
import java.util.List;

public class FadeColorFragment extends Fragment {

    private View matrix_select, color_picker, cancel_submit;

    private int red = 196, green = 64, blue = 128;

    private CheckBox cb_front, cb_right, cb_back, cb_left, cb_top;
    private List<CheckBox> cb_selections;
    private TextView tv_red, tv_green, tv_blue;
    private SeekBar sb_red, sb_green, sb_blue;
    private EditText et_hex;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_configure_fade_color, container, false);

        ConfiguratorListeners cl = new ConfiguratorListeners(getContext(), getFragmentManager());

        // find effect inputs
        matrix_select = (View) root.findViewById(R.id.select_matrix);
        color_picker = (View) root.findViewById(R.id.color_picker);
        cancel_submit = (View) root.findViewById(R.id.cancel_submit);

        // apply help dialogs to help buttons
        matrix_select.findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(getString(R.string.dialog_help_matrix_select)));
        color_picker.findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(getString(R.string.dialog_help_color_picker)));

        // find check boxes
        cb_front = matrix_select.findViewById(R.id.cb_matrix_front);
        cb_right = matrix_select.findViewById(R.id.cb_matrix_right);
        cb_back = matrix_select.findViewById(R.id.cb_matrix_back);
        cb_left = matrix_select.findViewById(R.id.cb_matrix_left);
        cb_top = matrix_select.findViewById(R.id.cb_matrix_top);
        cb_selections = Arrays.asList(cb_front, cb_right, cb_back, cb_left, cb_top);

        // apply listener to red seek bar
        tv_red = color_picker.findViewById(R.id.tv_red);
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
                tv_red.setTextColor(Color.BLACK);
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
                tv_green.setTextColor(Color.BLACK);
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
                tv_blue.setTextColor(Color.BLUE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv_blue.setTextColor(Color.BLACK);
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

        Bundle arguments = getArguments();
        if (arguments != null) {

            if (arguments.containsKey("params") && arguments.containsKey("pos")) {
                updateInputs(getArguments().getString("params"));
                cancel_submit.findViewById(R.id.b_submit).setOnClickListener(e -> cl.onSubmitClick(Effect.COLOR_RAINBOW, parseInputs(), true, getArguments().getInt("pos")));
            } else {
                Toast.makeText(getContext(), "Missing Params", Toast.LENGTH_SHORT).show();
            }

        } else {
            cancel_submit.findViewById(R.id.b_submit).setOnClickListener(e -> cl.onSubmitClick(Effect.COLOR_RAINBOW, parseInputs(), false, -1));
        }

        cancel_submit.findViewById(R.id.b_cancel).setOnClickListener(e -> cl.onCancelClick());

        return root;
    }

    private int getHexColor() {
        return Color.rgb(red, green, blue);
    }

    private String getHexString(boolean keepAlpha) {
        String hex = Integer.toHexString(getHexColor());
        return keepAlpha ? hex : hex.substring(2, 8);
    }

    private String formatRGB(int val) {
        return String.format("%3s", val).replace(' ', '0');
    }

    private String parseInputs() {

        // get checkbox selections
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

        String colors = "" + red + ";" + green + ";" + blue + ";";

        //return "{" + selections + ";" + colors + "}";
        return "{" + selections + ";0}";
    }

    private void updateInputs(String inputs) {

        // "10101;120;10;10"

        String trimmed_input = inputs.substring(1, inputs.length() - 1);

        String[] isolated_inputs = trimmed_input.split(";");

        for (int i = 0; i < cb_selections.size(); i++) {
            if (isolated_inputs[0].charAt(i) == '1') {
                cb_selections.get(i).setChecked(true);
            } else {
                cb_selections.get(i).setChecked(false);
            }
        }

        red = Integer.parseInt(isolated_inputs[1]);
        green = Integer.parseInt(isolated_inputs[2]);
        blue = Integer.parseInt(isolated_inputs[3]);

        tv_red.setText(formatRGB(red));
        sb_red.setProgress(red);
        tv_green.setText(formatRGB(green));
        sb_green.setProgress(green);
        tv_blue.setText(formatRGB(blue));
        sb_blue.setProgress(blue);

        et_hex.setText(getHexString(false));

        color_picker.findViewById(R.id.v_preview).setBackgroundColor(Color.rgb(red, green, blue));
    }
}
