package com.kkevn.ledsign.ui.create;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.kkevn.ledsign.MainActivity;
import com.kkevn.ledsign.R;

import java.util.Collections;
import java.util.Vector;

// https://github.com/chthai64/SwipeRevealLayout

// adapter class
public class EffectListView extends RecyclerView.Adapter<EffectListViewHolder> implements ItemTouchHelperAdapter {

    private Vector<Effect> effects;
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private DragListener mDragListener;

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public EffectListView(Context context, Vector<Effect> effects, DragListener dragListener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.effects = effects;
        this.mDragListener = dragListener;
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

        String id = String.valueOf(effects.get(position).getType());

        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(effects.get(position).getType()));
        viewBinderHelper.closeLayout(id);

        //holder.bindData(effects.get(position));
        // probably not needed due to the below doing the same

        holder.iv_icon.setImageResource(getEffectIcon(effects, position));
        holder.tv_effect.setText(effects.get(position).getType());
        //holder.tv_param.setText(effects.get(position).getParam());
        holder.tv_param.setText(effects.get(position).getMatrices(true));
        holder.iv_drag.setImageResource(R.drawable.baseline_drag_handle_24);

        holder.iv_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    mDragListener.onStartDrag(holder);
                }
                return false;
            }
        });

        holder.ll_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Removal of " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                //Log.i("EffectListViewHolder", "Removal of " + holder.getAdapterPosition());
                viewBinderHelper.closeLayout(id);
                removeItem(holder.getAdapterPosition());
            }
        });
        holder.ll_duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Dupe of " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                //Log.i("EffectListViewHolder", "Dupe of " + holder.getAdapterPosition());
                viewBinderHelper.closeLayout(id);
                duplicateItem(holder.getAdapterPosition());
            }
        });
        holder.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Edit of " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                //Log.i("EffectListViewHolder", "Edit of " + holder.getAdapterPosition());
                viewBinderHelper.closeLayout(id);
                editItem(holder.getAdapterPosition());
            }
        });
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        // swap list items at obtained positions and update the view to reflect the change
        Collections.swap(effects, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        removeItem(position);
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

    void editItem(int i) {
        Effect selection = getItem(i);
        MainActivity.navigateToFragmentWithBundle(selection.getType(), i, selection.getParam());
        notifyItemChanged(i);
    }

    void duplicateItem(int i) {
        effects.insertElementAt(effects.get(i), i + 1);
        notifyItemInserted(i + 1);

        MainActivity.setProfileUnsaved();
    }

    void removeItem(int i) {
        effects.removeElementAt(i);
        notifyItemRemoved(i);

        MainActivity.setProfileUnsaved();
        // add undo?
    }

    /*@Override
    public void onItemMove(int fromPos, int toPos) {
        // swap list items at obtained positions and update the view to reflect the change
        Collections.swap(effects, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
    }*/

    public static int getEffectIcon(Vector<Effect> v, int i) {

        switch (v.get(i).getType()) {
            case Effect.COLOR_SOLID:
                return R.drawable.ic_baseline_palette_24;
            case Effect.TEXT_SCROLL:
                return R.drawable.baseline_text_fields_24;
            case Effect.COLOR_RAINBOW:
                return R.drawable.ic_baseline_waves_24;
            case Effect.COLOR_FADE:
                return R.drawable.ic_baseline_gradient_24;
            case Effect.THEATER_CHASE:
                return R.drawable.ic_baseline_theaters_24;
            case Effect.RETRO:
                return R.drawable.ic_baseline_videogame_asset_24;
            default:
                return R.drawable.baseline_help_24;
        }
    }
}