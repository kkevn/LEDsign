package com.kkevn.ledsign.ui.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.kkevn.ledsign.MainActivity;
import com.kkevn.ledsign.R;
//import com.kkevn.ledsign.processing.Sketch;
import com.kkevn.ledsign.processing.ledsign;

import java.util.Collections;
import java.util.Vector;

import processing.android.PFragment;
import processing.core.PApplet;

public class CreateFragment extends Fragment implements EffectListView.ItemClickListener, DragListener {

    private PApplet sketch;

    private static ListView lv_list;

    static EffectListView adapter;

    private ItemTouchHelper itemTouchHelper;

    private static Vector<Effect> effects_list = new Vector<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_profile, container, false);

        // setup Processing sketch
        FrameLayout frame = new FrameLayout(getContext());
        frame = (FrameLayout) root.findViewById(R.id.container);
        //frame.setId(CompatUtils.getUniqueViewId());
        //setContentView(frame, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //sketch = new Sketch();
        sketch = new ledsign();
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, getActivity());

        // setup custom effects list
        //lv_list = (ListView) root.findViewById(R.id.lv_list);

        //effects_list.clear();
        //effects_list.add(new Effect(Effect.TEXT_SCROLL, "Hello there!"));

        //lv_list.setAdapter(new EffectListView(getContext(), effects_list));

        RecyclerView rv = root.findViewById(R.id.lv_list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        //rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        adapter = new EffectListView(getContext(), effects_list, this);
        adapter.setClickListener(this);
        rv.setAdapter(adapter);
        //rv.animate();
        ItemTouchHelper.Callback customCallback = new CustomItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(customCallback);
        itemTouchHelper.attachToRecyclerView(rv);


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

    public static void editEffect(int pos, String n, String p) {
        effects_list.removeElementAt(pos);
        effects_list.add(pos, new Effect(n, p));
        //((BaseAdapter) lv_list.getAdapter()).notifyDataSetChanged();
        //adapter.notifyItemInserted(effects_list.size() - 1);
        adapter.notifyItemChanged(pos);
    }

    public static void removeEffects() {
        int size = effects_list.size();
        effects_list.clear();
        adapter.notifyItemRangeRemoved(0, size);
    }

    // https://discourse.processing.org/t/how-to-pass-non-static-variable-values/12999s
    public static String parseList() {

        // SCROLL{},SOLID{},

        String result = "";

        // check if list is empty before building its parsable string
        if (!effects_list.isEmpty()) {
            for (Effect e: effects_list) {
                result += e.getType() + e.getParam() + ",";
            }
        } else {
            return ",";
        }

        return result;
    }

    public static Vector getList() {
        return effects_list;
    }

    /* process clicks on the context menu */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // determine action based on selected context menu
        switch(item.getItemId()) {

            // edit selected effect
            case R.id.menu_effect_edit:

                adapter.editItem(item.getGroupId());
                return true;

            // duplicate selected effect from list
            case R.id.menu_effect_duplicate:

                adapter.duplicateItem(item.getGroupId());
                return true;

            // remove selected effect from list
            case R.id.menu_effect_remove:

                // context menu created with id's at adapter pos
                adapter.removeItem(item.getGroupId());
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    // https://www.youtube.com/watch?v=H9D_HoOeKWM
    /*ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            // obtain original and dropped position indices
            int fromPos = viewHolder.getAdapterPosition();
            int toPos = target.getAdapterPosition();

            // swap list items at obtained positions and update the view to reflect the change
            Collections.swap(effects_list, fromPos, toPos);
            recyclerView.getAdapter().notifyItemMoved(fromPos, toPos);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            // swipe left or right
        }
    };*/

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