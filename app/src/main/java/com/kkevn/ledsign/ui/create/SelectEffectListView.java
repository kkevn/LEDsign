package com.kkevn.ledsign.ui.create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kkevn.ledsign.R;

import java.util.Vector;

public class SelectEffectListView extends BaseAdapter {
    private Vector<Effect> selectable_effects;
    private LayoutInflater mInflater;

    public SelectEffectListView(Context context, Vector<Effect> objects) {
        this.mInflater = LayoutInflater.from(context);
        this.selectable_effects = objects;
    }

    @Override
    public int getCount() {
        return selectable_effects.size();
    }

    @Override
    public Effect getItem(int i) {
        return selectable_effects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // grab the current view
        View v = view;

        // initialize a ViewHolder
        SelectEffectListViewHolder viewHolder = null;

        // if the view is not recycled
        if (v == null) {

            // inflate the current row with a layout
            //LayoutInflater layoutInflater = context.getLayoutInflater();
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
        viewHolder.iv_icon_select.setImageResource(EffectListView.getEffectIcon(selectable_effects, i));
        viewHolder.tv_effect_select.setText(selectable_effects.get(i).getType());

        // return the view
        return v;
    }
}
