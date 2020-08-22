package com.kkevn.ledsign.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkevn.ledsign.R;

import java.util.Vector;

public class EffectListView extends RecyclerView.Adapter<EffectListView.ViewHolder> {

    private Vector<Effect> effects;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public EffectListView(Context context, Vector<Effect> effects) {
        this.mInflater = LayoutInflater.from(context);
        this.effects = effects;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_effects, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.iv_icon.setImageResource(getEffectIcon(position));
        holder.tv_effect.setText(effects.get(position).getType());
        holder.tv_param.setText(effects.get(position).getParam());
        holder.iv_drag.setImageResource(R.drawable.baseline_drag_handle_24);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return effects.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        ImageView iv_icon;
        TextView tv_effect;
        TextView tv_param;
        ImageView iv_drag;

        ViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_effect = itemView.findViewById(R.id.tv_effect);
            tv_param = itemView.findViewById(R.id.tv_param);
            iv_drag = itemView.findViewById(R.id.iv_drag);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }


        //@Override
        public boolean onLongClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
            return true;
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Effect Options:");
            contextMenu.add(this.getAdapterPosition(), R.id.edit, 0, R.string.menu_edit);
            contextMenu.add(this.getAdapterPosition(), R.id.duplicate, 1, R.string.menu_duplicate);
            contextMenu.add(this.getAdapterPosition(), R.id.remove, 2, R.string.menu_remove);
        }
    }

    // convenience method for getting data at click position
    Effect getItem(int id) {
        return effects.get(id);
    }

    void duplicateItem(int i) {
        //effects.remove(i);
        effects.insertElementAt(effects.get(i), i + 1);
        //effects.removeElementAt(i);
        //notifyItemRemoved(i);
        notifyItemInserted(i + 1);
    }

    void removeItem(int i) {
        //effects.remove(i);
        effects.removeElementAt(i);
        notifyItemRemoved(i);
    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        //boolean onItemLongClicked(int position);
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

/*public class EffectListView extends BaseAdapter {
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
}*/