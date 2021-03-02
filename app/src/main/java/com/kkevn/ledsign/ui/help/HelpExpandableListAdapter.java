package com.kkevn.ledsign.ui.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kkevn.ledsign.R;

import java.util.HashMap;
import java.util.List;

public class HelpExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    List<String> groups;
    HashMap<String, String[]> items;

    public HelpExpandableListAdapter(Context context, List<String> groups, HashMap<String, String[]> items) {
        this.context = context;
        this.groups = groups;
        this.items = items;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return items.get(groups.get(i)).length;
    }

    @Override
    public Object getGroup(int i) {
        return groups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return items.get(groups.get(i))[i1];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_help_group, null);
        }

        TextView tv_help_title = view.findViewById(R.id.tv_help_title);
        tv_help_title.setText((String) getGroup(i));

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_help_item, null);
        }

        TextView tv_help_subitem = view.findViewById(R.id.tv_help_subitem);
        tv_help_subitem.setText("How to " + (String) getChild(i, i1));

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
