package com.kkevn.ledsign.ui.configurators;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kkevn.ledsign.R;
import com.kkevn.ledsign.ui.create.CreateFragment;
import com.kkevn.ledsign.ui.create.Effect;

public class ScrollingTextFragment extends Fragment {

    private View text_scroll, color_picker, cancel_submit;

    private int red = 196, green = 64, blue = 128;

    private TextView tv_red, tv_green, tv_blue;
    private SeekBar sb_red, sb_green, sb_blue;
    private EditText et_text, et_hex;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_configure_scrolling_text, container, false);

        ConfiguratorListeners cl = new ConfiguratorListeners(getContext(), getFragmentManager());

        // find effect inputs
        text_scroll = (View) root.findViewById(R.id.scroll_text);
        color_picker = (View) root.findViewById(R.id.color_picker);
        cancel_submit = (View) root.findViewById(R.id.cancel_submit);

        // apply help dialogs to help buttons
        text_scroll.findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(getString(R.string.dialog_help_edit_text)));
        color_picker.findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(getString(R.string.dialog_help_color_picker)));

        et_text = text_scroll.findViewById(R.id.et_text_to_scroll);

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
        et_hex.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == keyEvent.KEYCODE_ENTER && et_hex.length() == 6) {

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

        cancel_submit.findViewById(R.id.b_cancel).setOnClickListener(e -> cl.onCancelClick());
        cancel_submit.findViewById(R.id.b_submit).setOnClickListener(e -> cl.onSubmitClick(Effect.TEXT_SCROLL, parseInputs()));
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

        String text = "" + et_text.getText();
        if (text.trim().isEmpty())
            text = "-";

        return "{" + text + ";" + red + ";" + green + ";" + blue + "}";
    }
}
