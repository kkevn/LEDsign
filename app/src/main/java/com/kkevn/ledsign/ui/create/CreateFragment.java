/**
 * CreateFragment is the fragment containing the 3D preview sketch and buildable effects list.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.create;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.kkevn.ledsign.MainActivity;
import com.kkevn.ledsign.R;
import com.kkevn.ledsign.processing.LEDsign;

import java.util.Vector;

import processing.android.PFragment;
import processing.core.PApplet;

public class CreateFragment extends Fragment implements DragListener {

    // declare relevant variables
    private PApplet sketch;
    private ItemTouchHelper itemTouchHelper;
    static EffectListViewAdapter adapter;

    // initialize the effects list
    private static Vector<Effect> effects_list = new Vector<>();

    /**
     * Returns a view that contains the layout of this fragment that includes a Processing sketch 3D
     * preview and RecyclerView of buildable effects.
     *
     * @param {LayoutInflater} inflater: LayoutInflater object used to inflate the layout.
     * @param {ViewGroup} container: Parent view that this fragment's UI should attach to.
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {View} View containing Processing sketch and effects list RecyclerView.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the fragment's layout
        View root = inflater.inflate(R.layout.fragment_new_profile, container, false);

        // find the FrameLayout in this fragment's layout and set it up with the Processing sketch
        FrameLayout frame = (FrameLayout) root.findViewById(R.id.container);
        sketch = new LEDsign();
        PFragment fragment = new PFragment(sketch);
        fragment.setView(frame, getActivity());

        // find the RecyclerView in this fragment's layout and apply its adapter and touch callback
        RecyclerView recyclerView = root.findViewById(R.id.lv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EffectListViewAdapter(getContext(), effects_list, this);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback customCallback = new CustomItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(customCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // return this populated view
        return root;
    }

    /**
     * Action to perform on the specified ViewHolder once drag movement is initiated.
     *
     * @param {RecyclerView.ViewHolder} viewHolder: ViewHolder item to manipulate.
     */
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    /**
     * Callback for the result from requesting permissions. Necessary for the Processing sketch
     * because otherwise it may not work properly in the event of requested permission results not
     * reaching the sketch.
     *
     * @param {int} requestCode: Request code passed.
     * @param {String[]} permissions: Requested permissions.
     * @param {int[]} grantResults: Grant results for the corresponding permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (sketch != null) {
            sketch.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Called when activity is re-launched while at the top of the activity stack instead of a new
     * instance of the activity being started. Necessary for the Processing sketch so that it can
     * handle intents sent to the main activity.
     *
     * @param {Intent} intent: New intent started for the activity.
     */
    //@Override
    //public void onNewIntent(Intent intent) {
    //    if (sketch != null) {
    //        sketch.onNewIntent(intent);
    //    }
    //}

    /**
     * Returns the current effect list that is loaded.
     *
     * @return {Vector} Effect list loaded.
     */
    public static Vector getList() {
        return effects_list;
    }

    /**
     * Edits the effect at the specified index and notifies the adapter of the change. The edit is
     * performed by removing the pre-edit of the effect and inserting the post-edit version.
     *
     * @param {String} type: Type of effect.
     * @param {String} params: Parameters of the effect.
     */
    public static void addEffect(String type, String params) {

        // add the new effect to the end of the list
        effects_list.add(new Effect(type, params));

        // inform the adapter of the addition
        adapter.notifyItemInserted(effects_list.size() - 1);

        // inform the main activity that there is an unsaved change
        MainActivity.setProfileUnsaved();
    }

    /**
     * Edits the effect at the specified index and notifies the adapter of the change. The edit is
     * performed by removing the pre-edit of the effect and inserting the post-edit version.
     *
     * @param {int} pos: Position of effect in list of effects.
     * @param {String} type: Type of effect at the given position.
     * @param {String} params: Parameters of the effect at the given position.
     */
    public static void editEffect(int pos, String type, String params) {

        // remove the old effect and insert the new edited effect at the specified position
        effects_list.removeElementAt(pos);
        effects_list.add(pos, new Effect(type, params));

        // inform the adapter of the change
        adapter.notifyItemChanged(pos);

        // inform the main activity that there is an unsaved change
        MainActivity.setProfileUnsaved();
    }

    /**
     * Removes all of the effects in the list.
     */
    public static void removeEffects() {

        // obtain reference to length of list before clearing it
        int size = effects_list.size();

        // clear the list
        effects_list.clear();

        // inform the adapter of the removals
        adapter.notifyItemRangeRemoved(0, size);

        // inform the main activity that there is an unsaved change
        MainActivity.setProfileUnsaved();
    }

    /**
     * Returns a parsed String of all the effects in the currently loaded list. The effects are
     * comma delimited and are returned in readable formats for the Processing sketch and Arduino to
     * process. In the event of an empty list, a single comma is returned to denote an empty list to
     * the Processing sketch and Arduino.
     *
     * @return {String} Parsed list of all effects in the list.
     */
    public static String parseList() {

        // initialize blank result
        String result = "";

        // check if list is empty before building its parsable string
        if (!effects_list.isEmpty()) {

            // obtain type code and parameters of all effects in list, delimited with a comma
            for (Effect e: effects_list) {
                result += e.getTypeCode() + e.getParam() + ",";
            }
        } else {
            return ",";
        }

        return result;
    }
}