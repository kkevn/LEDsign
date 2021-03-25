/**
 * HelpFragment is the fragment containing the expandable list of help items.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.ExpandableListView;

import com.kkevn.ledsign.MainActivity;
import com.kkevn.ledsign.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpFragment extends Fragment {

    // declare relevant variables
    private ExpandableListView elv_contents;
    private HelpExpandableListAdapter adapter;
    static List<String> groups;
    static HashMap<String, String[]> items;

    /**
     * Returns a view that contains the layout of this fragment that includes the expandable list of
     * help categories and help items.
     *
     * @param {LayoutInflater} inflater: LayoutInflater object used to inflate the layout.
     * @param {ViewGroup} container: Parent view that this fragment's UI should attach to.
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {View} View containing expandable list view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the fragment's layout
        View root = inflater.inflate(R.layout.fragment_help, container, false);

        // obtain reference to ExpandableListView in layout
        elv_contents = root.findViewById(R.id.elv_contents);

        // initialize data structures that will contain category labels
        groups = new ArrayList<>();
        items = new HashMap<>();

        // set adapter for ExpandableListView and click listener
        adapter = new HelpExpandableListAdapter(getContext(), groups, items);
        elv_contents.setAdapter(adapter);
        elv_contents.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                // navigate to help item page for current category and index (with length of this category)
                MainActivity.navigateToHelpFragmentWithBundle(i, i1, items.get(groups.get(i)).length);
                return false;
            }
        });

        // populate list with category labels from strings.xml
        fillList();

        return root;
    }

    /**
     * Returns the help item at the specified help category and help item indexes.
     *
     * @param {int} i: Position of the group.
     * @param {int} i1: Position of the child item of the group.
     *
     * @return {String} Help item at these indexes.
     */
    public static String getHelpItem(int i, int i1) {
        return items.get(groups.get(i))[i1];
    }

    /**
     * Populates the map of help categories and their respective help items.
     */
    private void fillList() {

        // populate each help category
        groups.add(getString(R.string.help_title_create));
        groups.add(getString(R.string.help_title_load));
        groups.add(getString(R.string.help_title_bluetooth));
        groups.add(getString(R.string.help_title_3d));

        // populate each category with their items
        items.put(groups.get(0), getResources().getStringArray(R.array.help_title_create));
        items.put(groups.get(1), getResources().getStringArray(R.array.help_title_load));
        items.put(groups.get(2), getResources().getStringArray(R.array.help_title_bluetooth));
        items.put(groups.get(3), getResources().getStringArray(R.array.help_title_3d));
        adapter.notifyDataSetChanged();
    }
}