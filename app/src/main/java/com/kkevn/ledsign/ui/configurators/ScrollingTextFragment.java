package com.kkevn.ledsign.ui.configurators;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kkevn.ledsign.R;
import com.kkevn.ledsign.ui.create.CreateFragment;
import com.kkevn.ledsign.ui.create.Effect;

public class ScrollingTextFragment extends Fragment {

    private View text_scroll, color_picker;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_configure_scrolling_text, container, false);

        ConfiguratorListeners cl = new ConfiguratorListeners(getContext(), getFragmentManager());

        text_scroll = (View) root.findViewById(R.id.scroll_text);
        color_picker = (View) root.findViewById(R.id.color_picker);

        text_scroll.findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(getString(R.string.dialog_help_edit_text)));
        color_picker.findViewById(R.id.ib_help).setOnClickListener(e -> cl.onHelpClick(getString(R.string.dialog_help_color_picker)));


        //color_picker.findViewById(R.id.ib_help).
        //CreateFragment.addEffect(Effect.TEXT_SCROLL, "test");

        return root;
    }
}
