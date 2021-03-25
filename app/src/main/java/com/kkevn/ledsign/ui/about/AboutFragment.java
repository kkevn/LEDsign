/**
 * AboutFragment is the fragment containing the list of all credits and outside libraries used to
 * create this application.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.kkevn.ledsign.R;

public class AboutFragment extends Fragment {

    /**
     * Returns a view that contains the layout of this fragment that includes a scrollable list of
     * CardView objects. Each of these CardView objects contains information on the credits involved
     * in this project, specifically the @author's and all outside libraries used to create LEDsign.
     *
     * @param {LayoutInflater} inflater: LayoutInflater object used to inflate the layout.
     * @param {ViewGroup} container: Parent view that this fragment's UI should attach to.
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {View} View containing all development credits.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}