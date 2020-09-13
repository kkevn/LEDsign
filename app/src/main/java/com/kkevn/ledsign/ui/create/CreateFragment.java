package com.kkevn.ledsign.ui.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.kkevn.ledsign.MainActivity;
import com.kkevn.ledsign.R;
import com.kkevn.ledsign.processing.Sketch;

import java.util.Vector;

import processing.android.PFragment;
import processing.core.PApplet;

public class CreateFragment extends Fragment implements EffectListView.ItemClickListener {

    private PApplet sketch;

    private static ListView lv_list;

    static EffectListView adapter;

    private static Vector<Effect> effects_list = new Vector<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        //lv_list = (ListView) root.findViewById(R.id.lv_list);

        //effects_list.clear();
        effects_list.add(new Effect(Effect.TEXT_SCROLL, "Hello there!"));

        //lv_list.setAdapter(new EffectListView(getContext(), effects_list));

        RecyclerView rv = root.findViewById(R.id.lv_list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EffectListView(getContext(), effects_list);
        adapter.setClickListener(this);
        rv.setAdapter(adapter);
        //rv.animate();

        // register a context menu (which uses long press by default)
        registerForContextMenu(rv);
        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position).getType() + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    public static void addEffect(String n, String p) {
        effects_list.add(new Effect(n, p));
        //((BaseAdapter) lv_list.getAdapter()).notifyDataSetChanged();
        adapter.notifyItemInserted(effects_list.size() - 1);
    }

    /* process clicks on the context menu */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // determine action based on selected context menu
        switch(item.getItemId()) {

            // edit selected effect
            case R.id.menu_effect_edit:
                try {
                    if (MainActivity.ct.isAlive()) {
                        //mBTSocket.getOutputStream().write("1".getBytes());
                        MainActivity.ct.write("1".getBytes());
                    }
                    else {
                        Toast.makeText(getContext(),"ct dead",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ioe) {
                    Log.e(this.getClass().getSimpleName(), "error writing to socket", ioe);
                }

                Toast.makeText(getContext(), "Edit Item", Toast.LENGTH_SHORT).show();
                return true;

            // duplicate selected effect from list
            case R.id.menu_effect_duplicate:

                adapter.duplicateItem(item.getGroupId());
                return true;

            // remove selected effect from list
            case R.id.menu_effect_remove:

                try {
                    if (MainActivity.ct.isAlive()) {
                        //mBTSocket.getOutputStream().write("1".getBytes());
                        MainActivity.ct.write("0".getBytes());
                    }
                    else {
                        Toast.makeText(getContext(),"ct dead",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ioe) {
                    Log.e(this.getClass().getSimpleName(), "error writing to socket", ioe);
                }

                // context menu created with id's at adapter pos
                adapter.removeItem(item.getGroupId());
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