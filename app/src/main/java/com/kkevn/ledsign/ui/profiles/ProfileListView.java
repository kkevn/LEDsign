package com.kkevn.ledsign.ui.profiles;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.kkevn.ledsign.MainActivity;
import com.kkevn.ledsign.R;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

// https://github.com/chthai64/SwipeRevealLayout

// adapter class
public class ProfileListView extends RecyclerView.Adapter<ProfileListViewHolder> {

    private ArrayList<File> profiles;
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public ProfileListView(Context context, ArrayList<File> profiles) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.profiles = profiles;
    }

    // inflates the row layout from xml when needed
    @Override
    public ProfileListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_profiles, parent, false);
        return new ProfileListViewHolder(view, mClickListener);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ProfileListViewHolder holder, int position) {

        String id = String.valueOf(profiles.get(position).getName());

        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(profiles.get(position).getName()));
        viewBinderHelper.closeLayout(id);

        holder.tv_count.setText((position + 1) + "");
        Random r = new Random();
        int color = Color.argb(0x77, r.nextInt(256), r.nextInt(256), r.nextInt(256));
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {color, color});
        gd.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        gd.setCornerRadius(128f);
        holder.tv_count.setBackground(gd);
        holder.tv_profile.setText(profiles.get(position).getName().replace(".json", ""));
        holder.tv_date.setText(getLastDateModified(position));

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

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return profiles.size();
    }

    // convenience method for getting data at click position
    File getItem(int id) {
        return profiles.get(id);
    }

    void editItem(int i) {
        File selection = getItem(i);
        MainActivity.giveLoadSignal(selection.getName());
        notifyItemChanged(i);
    }

    void duplicateItem(int i) {
        File selection = getItem(i);
        File duplicate = new File(context.getFilesDir(), getUniqueDuplicateFileName(selection.getName()));
        try {
            duplicate.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Writer writer = new FileWriter(duplicate);
            Reader reader = new FileReader(selection);

            int x = reader.read();
            while (x != -1) {
                writer.write(x);
                x = reader.read();
            }
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //profiles.add(i + 1, duplicate);
        //notifyItemInserted(i + 1);

        profiles.add(profiles.size(), duplicate);
        notifyItemInserted(profiles.size());
    }

    void removeItem(int i) {
        File selection = getItem(i);
        selection.delete();
        profiles.remove(i);
        notifyItemRemoved(i);

        // add undo?
    }

    /**/
    private String getLastDateModified(int pos) {
        //return new SimpleDateFormat("E, MM/dd/yyyy, hh:mm:ss a").format(new Date(profiles.get(pos).lastModified()));
        return new SimpleDateFormat("EEEE, MMMM dd, yyyy, hh:mm:ss a").format(new Date(profiles.get(pos).lastModified()));
    }

    /**/
    private String getUniqueDuplicateFileName(String duplicateFile) {
        // create list of existing file names
        List<String> files = Arrays.asList(context.fileList());

        // keep appending copy label until a unique name is found
        while (files.contains(duplicateFile)) {
            duplicateFile = duplicateFile.replace(".json", "").concat(" - Copy.json");
        }

        // return new file name with file extension at end of file name
        return duplicateFile.replace(".json", "").concat(".json");
    }
}