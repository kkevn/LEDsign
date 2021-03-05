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

    private ExpandableListView elv_contents;
    HelpExpandableListAdapter adapter;
    static List<String> groups;
    static HashMap<String, String[]> items;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_help, container, false);

        elv_contents = root.findViewById(R.id.elv_contents);
        groups = new ArrayList<>();
        items = new HashMap<>();

        adapter = new HelpExpandableListAdapter(getContext(), groups, items);
        elv_contents.setAdapter(adapter);
        elv_contents.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                // i = group, i1 = child
                MainActivity.navigateToHelpFragmentWithBundle(i, i1);

                return false;
            }
        });
        fillList();

        return root;
    }

    public static String getHelpItem(int i, int i1) {
        return items.get(groups.get(i))[i1];
    }

    private void fillList() {
        groups.add(getString(R.string.help_title_create));
        groups.add(getString(R.string.help_title_load));
        groups.add(getString(R.string.help_title_bluetooth));
        groups.add(getString(R.string.help_title_3d));

        items.put(groups.get(0), getResources().getStringArray(R.array.help_title_create));
        items.put(groups.get(1), getResources().getStringArray(R.array.help_title_load));
        items.put(groups.get(2), getResources().getStringArray(R.array.help_title_bluetooth));
        items.put(groups.get(3), getResources().getStringArray(R.array.help_title_3d));
        adapter.notifyDataSetChanged();
    }
}