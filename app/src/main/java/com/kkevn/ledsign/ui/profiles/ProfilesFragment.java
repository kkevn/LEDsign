package com.kkevn.ledsign.ui.profiles;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kkevn.ledsign.R;
import com.kkevn.ledsign.ui.create.CustomItemTouchHelperCallback;

import java.io.File;
import java.util.ArrayList;

public class ProfilesFragment extends Fragment implements ProfileListView.ItemClickListener {

    static ProfileListView adapter;

    private ArrayList<File> profiles_list;

    private static TextView tv_empty;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_load_profile, container, false);

        // fetch list of saved profile files
        profiles_list = getSavedProfiles();

        tv_empty = root.findViewById(R.id.tv_empty);

        // reveal message if no profiles exist
        ProfilesFragment.showEmptyMessage(profiles_list.isEmpty());

        RecyclerView rv = root.findViewById(R.id.lv_list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));
        adapter = new ProfileListView(getContext(), profiles_list);
        adapter.setClickListener(this);
        rv.setAdapter(adapter);

        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + adapter.getItem(position).getName() + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    public static void showEmptyMessage(boolean isEmpty) {
        tv_empty.setVisibility(isEmpty ? View.VISIBLE : View.INVISIBLE);
    }

    /**/
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