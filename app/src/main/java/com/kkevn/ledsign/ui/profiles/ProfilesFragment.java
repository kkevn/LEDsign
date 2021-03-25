/**
 * ProfilesFragment is the fragment containing the list of all saved profiles created.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.profiles;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.kkevn.ledsign.R;

import java.io.File;
import java.util.ArrayList;

public class ProfilesFragment extends Fragment {

    // declare relevant variables
    private ArrayList<File> profiles_list;
    private static TextView tv_empty;
    private static ProfileListViewAdapter adapter;

    /**
     * Returns a view that contains the layout of this fragment that includes a list of all saved
     * profiles.
     *
     * @param {LayoutInflater} inflater: LayoutInflater object used to inflate the layout.
     * @param {ViewGroup} container: Parent view that this fragment's UI should attach to.
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {View} View containing profiles list.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the fragment's layout
        View root = inflater.inflate(R.layout.fragment_load_profile, container, false);

        // fetch list of saved profile files
        profiles_list = getSavedProfiles();

        // find the TextView in this fragment's layout and reveal message if no profiles exist
        tv_empty = root.findViewById(R.id.tv_empty);
        showEmptyMessage(profiles_list.isEmpty());

        // find the RecyclerView in this fragment's layout and apply its adapter
        RecyclerView rv = root.findViewById(R.id.lv_list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProfileListViewAdapter(getContext(), profiles_list);
        rv.setAdapter(adapter);

        // return this populated view
        return root;
    }

    /**
     * Determines whether or not to set visibility to the TextView that reveals the empty profiles
     * message.
     *
     * @param {boolean} isEmpty: Status of the TextView's visibility.
     */
    public static void showEmptyMessage(boolean isEmpty) {
        tv_empty.setVisibility(isEmpty ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * Returns a list of File objects. The list contains all saved profiles stored in the activity's
     * '.../files/' directory in system storage.
     *
     * @return {ArrayList} List of all saved profiles on disk.
     */
    private ArrayList getSavedProfiles() {

        // initialize ArrayList of File objects
        ArrayList<File> files = new ArrayList<>();

        // add reference for each existing file found in app's internal storage directory
        for (String file : getActivity().getApplicationContext().fileList()) {
            files.add(new File(getActivity().getApplicationContext().getFilesDir(), file));
        }

        return files;
    }
}