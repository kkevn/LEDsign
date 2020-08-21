package com.kkevn.ledsign.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.kkevn.ledsign.R;
import com.kkevn.ledsign.processing.Sketch;

import java.util.Vector;

import processing.android.PFragment;
import processing.core.PApplet;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private PApplet sketch;

    private ListView lv_list;

    private Vector<Effect> effects_list = new Vector<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_new_profile, container, false);

        // setup Processing sketch
        FrameLayout frame = new FrameLayout(getContext());
        frame = (FrameLayout) root.findViewById(R.id.container);
        //frame.setId(CompatUtils.getUniqueViewId());
        //setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        sketch = new Sketch();
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, getActivity());

        // setup custom effects list
        lv_list = (ListView) root.findViewById(R.id.lv_list);
        effects_list.add(new Effect(Effect.EFFECT_TEXT_SCROLL, "Hello there!"));

        lv_list.setAdapter(new EffectListView(getContext(), effects_list));

        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (sketch != null) {
            sketch.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //@Override
    public void onNewIntent(Intent intent) {
        if (sketch != null) {
            sketch.onNewIntent(intent);
        }
    }
}