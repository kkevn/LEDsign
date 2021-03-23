/**
 * EffectListViewAdapter is the Adapter object used to contain the blueprint for how to adapt the
 * individual customized effects into a single View.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.create;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.kkevn.ledsign.MainActivity;
import com.kkevn.ledsign.R;

import java.util.Collections;
import java.util.Vector;

public class EffectListViewAdapter extends RecyclerView.Adapter<EffectListViewHolder> implements ItemTouchHelperAdapter {

    // declare relevant variables
    private Vector<Effect> effects;
    private Context context;
    private LayoutInflater mInflater;
    private DragListener mDragListener;

    // initialize ViewBinderHelper object
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    /**
     * Constructor for this EffectListViewAdapter.
     *
     * @param {Context} context: Reference to context of the current activity.
     * @param {Vector<Effect>} effects: List of customized Effect objects.
     */
    public EffectListViewAdapter(Context context, Vector<Effect> effects, DragListener dragListener) {

        // initialize this adapter's variables
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.effects = effects;
        this.mDragListener = dragListener;
    }

    /**
     * Returns a new EffectListViewHolder with an inflated view.
     *
     * @param {ViewGroup} parent: The ViewGroup which the new View will be added to.
     * @param {int} viewType: The view type of the new View.
     *
     * @return {EffectListViewHolder} ViewHolder that holds a View of the given view type.
     */
    @Override
    public EffectListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // inflate a View with the specified layout resource
        View view = mInflater.inflate(R.layout.list_effects, parent, false);

        // return a ProfileListViewHolder with this view
        return new EffectListViewHolder(view);
    }

    /**
     * Binds the contents of the EffectListViewHolder with updated contents.
     *
     * @param {EffectListViewHolder} holder: The ViewHolder that should have its contents updated.
     * @param {int} position: The position of the File within the profile list.
     */
    @Override
    public void onBindViewHolder(EffectListViewHolder holder, int position) {

        // get unique ID of effect at specified position
        final Effect currentEffect = getEffectAt(position);
        final String id = currentEffect.getEffectID() + position;

        // bind the SwipeRevealLayout and only allow one to be open at a time
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, id);
        viewBinderHelper.closeLayout(id);

        // get array of RGB colors in specified effect and declare gradient drawable for background
        int colors[] = currentEffect.getColor();
        GradientDrawable gd;

        // if effect uses single RGB value coloring
        if (colors[0] == 1) {

            // if effect is theater chase and rainbow effect is applied, use multi-color gradient background instead
            if (currentEffect.getType().equals(Effect.THEATER_CHASE) && !currentEffect.getParamAt(4).equals("0")) {
                gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] {0x77ff0000, 0x4400ff00, 0x770000ff});
            } else {

                // otherwise use the effect's RGB value to give it a single color background
                int color = Color.rgb(colors[1], colors[2], colors[3]);
                gd = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{color, color});
                gd.setGradientType(GradientDrawable.SWEEP_GRADIENT);
            }
        } else {

            // effect has no single RGB value, apply rainbow gradient
            gd = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] {0x77ff0000, 0x4400ff00, 0x770000ff});
        }

        // apply rounded corners, gradient background and appropriate icon for specified effect
        gd.setCornerRadius(48f);
        holder.iv_icon.setBackground(gd);
        holder.iv_icon.setImageResource(getEffectIcon(currentEffect.getType()));

        // apply effect name, its list of matrices and a drag icon handle to relevant fields
        holder.tv_effect.setText(currentEffect.getType());
        holder.tv_param.setText(currentEffect.getMatrices(true));
        holder.iv_drag.setImageResource(R.drawable.baseline_drag_handle_24);

        // apply listener to ImageView containing drag handle
        holder.iv_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                // if drag motion detected, initiate drag ability
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    mDragListener.onStartDrag(holder);
                }
                return false;
            }
        });

        // apply listeners to buttons hidden in SwipeRevealLayout
        holder.ll_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // close the SwipeRevealLayout
                viewBinderHelper.closeLayout(id);

                // remove this effect
                removeItem(holder.getAdapterPosition());
            }
        });
        holder.ll_duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // close the SwipeRevealLayout
                viewBinderHelper.closeLayout(id);

                // duplicate this effect
                duplicateItem(holder.getAdapterPosition());
            }
        });
        holder.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // close the SwipeRevealLayout
                viewBinderHelper.closeLayout(id);

                // edit this effect
                editItem(holder.getAdapterPosition());
            }
        });
    }

    /**
     * Swaps the position of the Effect object to and from the specified positions.
     *
     * @param {int} fromPos: Position of effect before being dragged.
     * @param {int} toPos: Position of effect after being released from drag.
     *
     * @return {boolean} Whether the item moved or not.
     */
    @Override
    public boolean onItemMove(int fromPos, int toPos) {

        // swap list items at obtained positions and update the view to reflect the change
        Collections.swap(effects, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
        return true;
    }

    /**
     * Removes the Effect object at the specified position.
     *
     * @param {int} pos: Position of effect in list of effects.
     */
    @Override
    public void onItemDismiss(int pos) {
        removeItem(pos);
    }

    /**
     * Returns the count of Effect objects.
     *
     * @return {int} Count of effects.
     */
    @Override
    public int getItemCount() {
        return effects.size();
    }

    /**
     * Returns the Effect object at the specified index.
     *
     * @param {int} pos: Position of effect in list of effects.
     *
     * @return {Effect} Effect at specified position.
     */
    private Effect getEffectAt(int pos) {
        return effects.get(pos);
    }

    /**
     * Edits the effect at the specified index and notifies the adapter of the change.
     *
     * @param {int} pos: Position of effect in list of effects.
     */
    private void editItem(int pos) {

        // obtain the effect at the specified position
        Effect selection = getEffectAt(pos);

        // inform the UI and adapter that this effect is being edited
        MainActivity.navigateToFragmentWithBundle(selection.getType(), pos, selection.getParam());
        notifyItemChanged(pos);
    }

    /**
     * Duplicates the effect at the specified index and notifies the adapter of the addition.
     *
     * @param {int} pos: Position of effect in list of effects.
     */
    private void duplicateItem(int pos) {

        // insert the same effect above the specified position
        effects.insertElementAt(getEffectAt(pos), pos + 1);

        // inform the adapter of the addition
        notifyItemInserted(pos + 1);

        // inform the main activity that there is an unsaved change
        MainActivity.setProfileUnsaved();
    }

    /**
     * Removes the effect at the specified index and notifies the adapter of the removal.
     *
     * @param {int} pos: Position of effect in list of effects.
     */
    private void removeItem(int pos) {

        // remove effect at the specified position
        effects.removeElementAt(pos);

        // inform the adapter of the removal
        notifyItemRemoved(pos);

        // inform the main activity that there is an unsaved change
        MainActivity.setProfileUnsaved();
    }

    /**
     * Returns the icon drawable of the specified Effect object.
     *
     * @param {String} effect: Name of effect to fetch icon for.
     *
     * @return {int} Drawable of effect icon.
     */
    public static int getEffectIcon(String effect) {

        // return appropriate drawable icon based on given effect name
        switch (effect) {
            case Effect.COLOR_SOLID:
                return R.drawable.ic_baseline_palette_24;
            case Effect.TEXT_SCROLL:
                return R.drawable.ic_baseline_text_fields_24;
            case Effect.COLOR_RAINBOW:
                return R.drawable.ic_baseline_waves_24;
            case Effect.COLOR_FADE:
                return R.drawable.ic_baseline_gradient_24;
            case Effect.COLOR_WIPE:
                return R.drawable.ic_baseline_texture_24;
            case Effect.THEATER_CHASE:
                return R.drawable.ic_baseline_theaters_24;
            case Effect.RETRO:
                return R.drawable.ic_baseline_videogame_asset_24;
            default:
                return R.drawable.ic_baseline_help_24;
        }
    }
}