package com.kkevn.ledsign.ui.create;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kkevn.ledsign.R;

import java.util.Vector;

public class EffectListView extends RecyclerView.Adapter<EffectListViewHolder> {

    private Vector<Effect> effects;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public EffectListView(Context context, Vector<Effect> effects) {
        this.mInflater = LayoutInflater.from(context);
        this.effects = effects;
    }

    // inflates the row layout from xml when needed
    @Override
    public EffectListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_effects, parent, false);
        return new EffectListViewHolder(view, mClickListener);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(EffectListViewHolder holder, int position) {
        holder.iv_icon.setImageResource(getEffectIcon(position));
        holder.tv_effect.setText(effects.get(position).getType());
        holder.tv_param.setText(effects.get(position).getParam());
        holder.iv_drag.setImageResource(R.drawable.baseline_drag_handle_24);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return effects.size();
    }

    // convenience method for getting data at click position
    Effect getItem(int id) {
        return effects.get(id);
    }

    void duplicateItem(int i) {
        effects.insertElementAt(effects.get(i), i + 1);
        notifyItemInserted(i + 1);
    }

    void removeItem(int i) {
        effects.removeElementAt(i);
        notifyItemRemoved(i);
    }

    private int getEffectIcon(int i) {

        switch (effects.get(i).getType()) {
            case Effect.EFFECT_TEXT_SCROLL:
                return R.drawable.baseline_text_fields_24;

            default:
                return R.drawable.baseline_help_24;
        }
    }
}