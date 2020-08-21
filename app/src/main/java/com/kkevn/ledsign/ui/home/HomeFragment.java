package com.kkevn.ledsign.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

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


        // listener for item short press
        onShortPress();

        // register a context menu (which uses long press by default)
        registerForContextMenu(lv_list);


        return root;
    }

    public static void addEffect(String n, String p) {
        effects_list.add(new Effect(n, p));
        ((BaseAdapter) lv_list.getAdapter()).notifyDataSetChanged();
    }

    /* listens for short press on list item and opens edit dialog */
    public void onShortPress() {
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Edit Item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* create the context menu */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.effect_menu, menu);
    }

    /* process clicks on the context menu */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // get index and id of list view item
        int selection_index = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        long selection_id = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).id;

        // determine action based on selected context menu
        switch(item.getItemId()) {

            // edit selected effect
            case R.id.edit:

                Toast.makeText(getContext(), "Edit Item", Toast.LENGTH_SHORT).show();
                return true;

            // remove selected effect from list
            case R.id.remove:

                effects_list.remove(selection_index);
                ((BaseAdapter) lv_list.getAdapter()).notifyDataSetChanged();

                Toast.makeText(getContext(), "Removed Item", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;
        }
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