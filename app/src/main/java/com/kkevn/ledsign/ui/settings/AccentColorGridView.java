package com.kkevn.ledsign.ui.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.kkevn.ledsign.R;

public class AccentColorGridView extends BaseAdapter {
    private int[] colors;
    private LayoutInflater mInflater;
    private int width;

    public AccentColorGridView(Context context/*, int[] colors*/) {
        //super(context, 0, colors);
        width = context.getResources().getSystem().getDisplayMetrics().widthPixels;
        this.mInflater = LayoutInflater.from(context);
        this.colors = context.getResources().getIntArray(R.array.accents);
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int i) {
        return colors[i];
    }

    @Override
    public long getItemId(int i) {
        return colors[i];
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // grab the current view
        View v = view;

        // initialize a ViewHolder
        AccentColorGridViewHolder viewHolder = null;

        // if the view is not recycled
        if (v == null) {

            // inflate the current row with a layout
            //LayoutInflater layoutInflater = context.getLayoutInflater();
            v = mInflater.inflate(R.layout.list_color_accent, null, true);
            v.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, (int) (width * 0.1)));

            // create the view holder
            viewHolder = new AccentColorGridViewHolder(v);

            // store a tag to the view
            v.setTag(viewHolder);
        }

        // otherwise view was null
        else {

            // update view without inflating a new row
            viewHolder = (AccentColorGridViewHolder) v.getTag();
        }

        // populate item based on current position
        viewHolder.iv_color.setBackgroundColor(colors[i]);

        // return the view
        return v;
    }
}
