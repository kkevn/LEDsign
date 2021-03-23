/**
 * SelectEffectListViewAdapter is the Adapter object used to contain the blueprint for how to adapt
 * the individual available Effect objects into a single View.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kkevn.ledsign.R;

import java.util.Vector;

public class SelectEffectListViewAdapter extends BaseAdapter {

    // declare relevant variables
    private Vector<Effect> selectable_effects;
    private LayoutInflater mInflater;

    /**
     * Constructor for this SelectEffectListViewAdapter.
     *
     * @param {Context} context: Reference to context of the current activity.
     * @param {Vector<Effect>} objects: List of Effect objects available.
     */
    public SelectEffectListViewAdapter(Context context, Vector<Effect> objects) {

        // initialize this adapter's variables
        this.mInflater = LayoutInflater.from(context);
        this.selectable_effects = objects;
    }

    /**
     * Returns a count of the items in this adapted list.
     *
     * @return {int} Count of items.
     */
    @Override
    public int getCount() {
        return selectable_effects.size();
    }

    /**
     * Returns the item at the specified index.
     *
     * @param {int} i: Index of the item.
     *
     * @return {Object} Item object.
     */
    @Override
    public Effect getItem(int i) {
        return selectable_effects.get(i);
    }

    /**
     * Returns the ID of the item at the specified index.
     *
     * @param {int} i: Index of the item.
     *
     * @return {long} ID of item.
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Returns a view that adapts the item at the given index.
     *
     * @param {int} i: Index of the item to adapt.
     * @param {View} view: The old view to reuse.
     * @param {ViewGroup} viewGroup: The parent this view will eventually be attached to.
     *
     * @return {View} View containing the adapted item.
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // grab the current view
        View v = view;

        // initialize a ViewHolder
        SelectEffectListViewHolder viewHolder = null;

        // if the view is not recycled
        if (v == null) {

            // inflate the current row with a layout
            v = mInflater.inflate(R.layout.list_select_effect, null, true);

            // create the view holder
            viewHolder = new SelectEffectListViewHolder(v);

            // store a tag to the view
            v.setTag(viewHolder);
        }

        // otherwise view was null
        else {

            // update view without inflating a new row
            viewHolder = (SelectEffectListViewHolder) v.getTag();
        }

        // populate item based on current position
        viewHolder.iv_icon_select.setImageResource(EffectListViewAdapter.getEffectIcon(selectable_effects.get(i).getType()));
        viewHolder.tv_effect_select.setText(selectable_effects.get(i).getType());

        // return the view
        return v;
    }
}