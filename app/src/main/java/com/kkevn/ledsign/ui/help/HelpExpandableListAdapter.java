/**
 * HelpExpandableListAdapter is the Adapter object used to contain the blueprint for how to adapt
 * the individual help page categories into a single View.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.help;

import android.content.Context;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kkevn.ledsign.R;

import java.util.HashMap;
import java.util.List;

public class HelpExpandableListAdapter extends BaseExpandableListAdapter {

    // declare relevant variables
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> groups;
    private HashMap<String, String[]> items;

    /**
     * Constructor for this HelpExpandableListAdapter.
     *
     * @param {Context} context: Reference to context of the current activity.
     * @param {List<String>} groups: List of all help category items.
     * @param {HashMap<String, String[]} items: Map of all help category items and their help items.
     */
    public HelpExpandableListAdapter(Context context, List<String> groups, HashMap<String, String[]> items) {

        // initialize this adapter's variables
        this.context = context;
        this.groups = groups;
        this.items = items;
    }

    /**
     * Returns the count of help categories.
     *
     * @return {int} Count of help categories.
     */
    @Override
    public int getGroupCount() {
        return groups.size();
    }

    /**
     * Returns the count of help items in the specified help category index.
     *
     * @return {int} Count of help items.
     */
    @Override
    public int getChildrenCount(int i) {
        return items.get(groups.get(i)).length;
    }

    /**
     * Returns the help category's items at the specified index.
     *
     * @return {Object} Help category item.
     */
    @Override
    public Object getGroup(int i) {
        return groups.get(i);
    }

    /**
     * Returns the help category's child item at the specified indexes.
     *
     * @return {Object} Help item.
     */
    @Override
    public Object getChild(int i, int i1) {
        return items.get(groups.get(i))[i1];
    }

    /**
     * Returns the help category's ID at the specified index.
     *
     * @return {Object} Help category item.
     */
    @Override
    public long getGroupId(int i) {
        return i;
    }

    /**
     * Returns the help category's child item ID at the specified indexes.
     *
     * @return {Object} Help item.
     */
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    /**
     * Returns whether or not the ListView has stable IDs for its list items.
     *
     * @return {boolean} Stable ID status.
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Returns the View that displays the given group.
     *
     * @param {int} i: Position of the group.
     * @param {boolean} b: Whether the group is expended or collapsed.
     * @param {View} view: The old view to reuse.
     * @param {ViewGroup} viewGroup: The parent that this view will eventually be attached to.
     *
     * @return {View} View corresponding to the group at specified position.
     */
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        // inflate the view if null
        if (view == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_help_group, null);
        }

        // fill the TextView with the accent color and help category title
        TextView tv_help_title = view.findViewById(R.id.tv_help_title);
        tv_help_title.setTextColor(PreferenceManager.getDefaultSharedPreferences(context).getInt(context.getResources().getString(R.string.pref_color_key), 0));
        tv_help_title.setText((String) getGroup(i));

        return view;
    }

    /**
     * Returns the View that displays the given child of the given group.
     *
     * @param {int} i: Position of the group.
     * @param {int} i1: Position of the child item of the group.
     * @param {boolean} b: Whether the group is expended or collapsed.
     * @param {View} view: The old view to reuse.
     * @param {ViewGroup} viewGroup: The parent that this view will eventually be attached to.
     *
     * @return {View} View corresponding to the child of the group at specified position.
     */
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        // inflate the view if null
        if (view == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_help_item, null);
        }

        // fill the TextView with the accent color and help category title
        TextView tv_help_subitem = view.findViewById(R.id.tv_help_subitem);
        tv_help_subitem.setText("How to " + (String) getChild(i, i1));

        return view;
    }

    /**
     * Returns whether or not the specified child item is selectable.
     *
     * @param {int} i: Position of the group.
     * @param {int} i1: Position of the child item of the group.
     *
     * @return {boolean} Selectable status of child item.
     */
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}