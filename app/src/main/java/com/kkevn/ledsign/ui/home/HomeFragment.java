package com.kkevn.ledsign.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    private static ListView lv_list;

    private static Vector<Effect> effects_list = new Vector<>();

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

        //effects_list.clear();
        effects_list.add(new Effect(Effect.EFFECT_TEXT_SCROLL, "Hello there!"));

        lv_list.setAdapter(new EffectListView(getContext(), effects_list));
        
        return root;
    }

    public static void addEffect(String n, String p) {
        effects_list.add(new Effect(n, p));
        ((BaseAdapter) lv_list.getAdapter()).notifyDataSetChanged();
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