/**
 * ProfileListViewAdapter is the Adapter object used to contain the blueprint for how to adapt the
 * individual profiles saved on disk into a single View.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.profiles;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class ProfileListViewAdapter extends RecyclerView.Adapter<ProfileListViewHolder> {

    // declare relevant variables
    private ArrayList<File> profiles;
    private Context context;
    private LayoutInflater mInflater;

    // initialize ViewBinderHelper object
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    /**
     * Constructor for this ProfileListViewAdapter.
     *
     * @param {Context} context: Reference to context of the current activity.
     * @param {ArrayList<File>} objects: List of File objects for saved profiles.
     */
    public ProfileListViewAdapter(Context context, ArrayList<File> profiles) {

        // initialize this adapter's variables
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.profiles = profiles;
    }

    /**
     * Returns a new ProfileListViewHolder with an inflated view.
     *
     * @param {ViewGroup} parent: The ViewGroup which the new View will be added to.
     * @param {int} viewType: The view type of the new View.
     *
     * @return {ProfileListViewHolder} ViewHolder that holds a View of the given view type.
     */
    @Override
    public ProfileListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // inflate a View with the specified layout resource
        View view = mInflater.inflate(R.layout.list_profiles, parent, false);

        // return a ProfileListViewHolder with this view
        return new ProfileListViewHolder(view);
    }

    /**
     * Binds the contents of the ProfileListViewHolder with updated contents.
     *
     * @param {ProfileListViewHolder} holder: The ViewHolder that should have its contents updated.
     * @param {int} position: The position of the File within the profile list.
     */
    @Override
    public void onBindViewHolder(ProfileListViewHolder holder, int position) {

        // get unique ID or name of profile File at specified position
        String id = getFileAt(position).getName();

        // bind the SwipeRevealLayout and only allow one to be open at a time
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, id);
        viewBinderHelper.closeLayout(id);

        // apply the next position value to this TextView and a random circular background color
        holder.tv_count.setText((position + 1) + "");
        Random r = new Random();
        int color = Color.argb(0x77, r.nextInt(256), r.nextInt(256), r.nextInt(256));
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[] {color, color});
        gd.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        gd.setCornerRadius(128f);
        holder.tv_count.setBackground(gd);

        // apply the profile name without extension and formatted last modified date to the views
        holder.tv_profile.setText(id.replace(".json", ""));
        holder.tv_date.setText(getLastDateModified(position));

        // apply listeners to buttons hidden in SwipeRevealLayout
        holder.ll_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // close the SwipeRevealLayout
                viewBinderHelper.closeLayout(id);

                // remove this profile
                removeItem(holder.getAdapterPosition());
            }
        });
        holder.ll_duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // close the SwipeRevealLayout
                viewBinderHelper.closeLayout(id);

                // duplicate this profile
                duplicateItem(holder.getAdapterPosition());
            }
        });
        holder.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // close the SwipeRevealLayout
                viewBinderHelper.closeLayout(id);

                // edit this profile
                editItem(holder.getAdapterPosition());
            }
        });
    }

    /**
     * Returns the count of profile File objects.
     *
     * @return {int} Count of profiles.
     */
    @Override
    public int getItemCount() {
        return profiles.size();
    }

    /**
     * Returns the profile File object at the specified index.
     *
     * @param {int} pos: Position of file in list of profiles.
     *
     * @return {File} Profile at specified position.
     */
    private File getFileAt(int pos) {
        return profiles.get(pos);
    }

    /**
     * Edits the profile at the specified index and notifies the adapter of the change.
     *
     * @param {int} pos: Position of file in list of profiles.
     */
    private void editItem(int pos) {

        // obtain the file at the specified position
        File selection = getFileAt(pos);

        // inform the UI and adapter that this profile is being loaded
        MainActivity.giveLoadSignal(selection.getName());
        notifyItemChanged(pos);
    }

    /**
     * Duplicates the profile at the specified index and notifies the adapter of the addition.
     *
     * @param {int} pos: Position of file in list of profiles.
     */
    private void duplicateItem(int pos) {

        // obtain the file at the specified position
        File selection = getFileAt(pos);

        // create a new blank File object with a similar name to the original specified file
        File duplicate = new File(context.getFilesDir(), getUniqueDuplicateFileName(selection.getName()));

        // create the blank file
        try {

            // create the blank file on disk from the File object
            duplicate.createNewFile();

            // copy the original into the blank
            try {

                // create Reader stream from specified file and Writer stream for duplicate file
                Reader reader = new FileReader(selection);
                Writer writer = new FileWriter(duplicate);

                // copy each character from the original file into the blank one
                int currChar = reader.read();
                while (currChar != -1) {
                    writer.write(currChar);
                    currChar = reader.read();
                }

                // close Reader and Writer streams
                reader.close();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add the copy to the list of profiles and inform the adapter of the addition
        profiles.add(profiles.size(), duplicate);
        notifyItemInserted(profiles.size());
    }

    /**
     * Removes the profile at the specified index and notifies the adapter of the removal.
     *
     * @param {int} pos: Position of file in list of profiles.
     */
    private void removeItem(int pos) {

        // obtain the file at the specified position
        File selection = getFileAt(pos);

        // delete the file, remove the profile object from the list, and notify the adapter
        selection.delete();
        profiles.remove(pos);
        notifyItemRemoved(pos);

        // reveal message if no profiles exist
        ProfilesFragment.showEmptyMessage(profiles.isEmpty());
    }

    /**
     * Returns a formatted String of the last modified date and time of this profile file. The
     * format used is in the form of: 'January 01, 1970, 12:34:56 PM'
     *
     * @param {int} pos: Position of file in list of profiles.
     *
     * @return {String} Formatted date and time.
     */
    private String getLastDateModified(int pos) {

        // create format pattern from String resource
        String formatPattern = context.getString(R.string.date_time_format_pattern);

        // create a Date object of the last modified date and time of the specified profile
        Date lastModified = new Date(profiles.get(pos).lastModified());

        // return the Date object as a formatted String based on the format pattern
        return new SimpleDateFormat(formatPattern).format(lastModified);
    }

    /**
     * Returns a unique version of the provided filename concatenated with a copy label.
     *
     * @param {String} duplicateFile: Original filename to be duplicated.
     *
     * @return {String} Unique filename appended with a label denoting it as a copy.
     */
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