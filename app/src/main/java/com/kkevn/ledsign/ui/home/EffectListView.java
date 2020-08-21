package com.kkevn.ledsign.ui.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kkevn.ledsign.R;

import java.util.Vector;

public class EffectListView extends BaseAdapter {
    private Activity context;

    private Vector<Effect> effects;

    public EffectListView(Context context, Vector<Effect> effects) {
        this.context = (Activity) context;
        this.effects = effects;
    }

    @Override
    public int getCount() {
        return effects.size();
    }

    @Override
    public Object getItem(int i) {
        return effects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view = convertView;

        ListViewHolder viewHolder = null;

        // if the view is not recycled
        if (view == null) {

            // inflate the current row with a layout
            LayoutInflater layoutInflater = context.getLayoutInflater();
            view = layoutInflater.inflate(R.layout.list_effects, null, true);

            // create the view holder
            viewHolder = new ListViewHolder(view);

            // store a tag to the view
            view.setTag(viewHolder);
        } else {

            // update view without inflating a new row
            viewHolder = (ListViewHolder) view.getTag();
        }

        // populate item based on current position
        viewHolder.iv_icon.setImageResource(getEffectIcon(i));
        viewHolder.tv_effect.setText(effects.get(i).getType());
        viewHolder.tv_param.setText(effects.get(i).getParam());
        viewHolder.iv_drag.setImageResource(R.drawable.baseline_drag_handle_24);

        // return the view
        return view;
    }

    private int getEffectIcon(int i) {

        switch (effects.get(i).getType()) {
            case Effect.EFFECT_TEXT_SCROLL:
                return R.drawable.baseline_text_fields_24;

            default:
                return R.drawable.baseline_add_circle_outline_white_48dp;
        }
    }
}
