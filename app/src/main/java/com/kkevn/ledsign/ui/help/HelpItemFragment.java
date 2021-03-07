package com.kkevn.ledsign.ui.help;

import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.kkevn.ledsign.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class HelpItemFragment extends Fragment {

    int category, index;

    private VideoView vv_how_to_video;
    private View v_dimmer;
    private TextView tv_help_title;
    private ExpandableTextView etv;

    //private MediaController mediaController;

    //public HelpItemFragment() {
    //}

    public static HelpItemFragment newInstance(int category, int index) {
        Bundle args = new Bundle();
        args.putInt(HelpViewPagerFragment.CATEGORY, category);
        args.putInt(HelpViewPagerFragment.CATEGORY_INDEX, index);
        HelpItemFragment fragment = new HelpItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.category = args.getInt(HelpViewPagerFragment.CATEGORY);
            this.index = args.getInt(HelpViewPagerFragment.CATEGORY_INDEX);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // obtain references to the layout's different view items
        vv_how_to_video = view.findViewById(R.id.vv_how_to_video);
        v_dimmer = view.findViewById(R.id.v_dimmer);
        tv_help_title = view.findViewById(R.id.tv_help_title);
        etv = view.findViewById(R.id.expand_text_view);

        /*
        vv_how_to_video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (vv_how_to_video.isPlaying())
                    vv_how_to_video.stopPlayback();
                else
                    vv_how_to_video.start();

                return true;
            }
        });

        mediaController = new MediaController(getContext());
        vv_how_to_video.setMediaController(mediaController);
        mediaController.setAnchorView(vv_how_to_video);
        */

        // disable VideoView's audio, set to loop indefinitely, and play automatically
        vv_how_to_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(0, 0);
                mediaPlayer.setLooping(true);
            }
        });
        vv_how_to_video.start();

        // listener to play or pause/dim VideoView based on ExpandableTextView's current expand state
        etv.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {

                if (!isExpanded) {
                    vv_how_to_video.start();
                    v_dimmer.getBackground().setAlpha(0);
                } else {
                    vv_how_to_video.pause();
                    v_dimmer.getBackground().setAlpha(128); //255
                }
            }
        });

        // update the views for the current help item
        updateViews();
    }

    private void updateViews() {

        // update the help topic title
        tv_help_title.setText("How to " + HelpFragment.getHelpItem(category, index));

        // references for resources
        String[] steps;
        TypedArray vids;

        // fetch resources for specified help category
        switch (category) {
            case 0:
                steps = getResources().getStringArray(R.array.help_steps_create);
                vids = getResources().obtainTypedArray(R.array.help_videos_create);
                break;
            case 1:
                steps = getResources().getStringArray(R.array.help_steps_load);
                vids = getResources().obtainTypedArray(R.array.help_videos_load);
                break;
            case 2:
                steps = getResources().getStringArray(R.array.help_steps_bluetooth);
                vids = getResources().obtainTypedArray(R.array.help_videos_bluetooth);
                break;
            case 3:
                steps = getResources().getStringArray(R.array.help_steps_3d);
                vids = getResources().obtainTypedArray(R.array.help_videos_3d);
                break;
            default:
                steps = new String[1];
                vids = null;
        }

        // apply the formatted help steps to the expandable text view
        etv.setText(steps[index].replaceAll("[|]", "\n\n"));

        // update the video view with the proper video for this help item
        String path = "android.resource://" + getContext().getPackageName() + "/" + vids.getResourceId(index, -1);
        Uri uri = Uri.parse(path);
        vv_how_to_video.setVideoURI(uri);

        vids.recycle();
    }
}