package com.kkevn.ledsign.ui.help;

import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private VideoView vv_how_to_video;
    private View v;
    private TextView tv_help_title;
    private ExpandableTextView etv;

    private MediaController mediaController;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_help_item, container, false);

        vv_how_to_video = root.findViewById(R.id.vv_how_to_video);
        v = root.findViewById(R.id.v_dimmer);
        tv_help_title = root.findViewById(R.id.tv_help_title);
        etv = root.findViewById(R.id.expand_text_view);

        /*vv_how_to_video.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (vv_how_to_video.isPlaying())
                    vv_how_to_video.stopPlayback();
                else
                    vv_how_to_video.start();

                return true;
            }
        });*/

        mediaController = new MediaController(getContext());
        vv_how_to_video.setMediaController(mediaController);
        vv_how_to_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(0, 0);
                mediaPlayer.setLooping(true);
            }
        });
        mediaController.setAnchorView(vv_how_to_video);
        vv_how_to_video.start();

        etv.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(TextView textView, boolean isExpanded) {

                if (!isExpanded) {
                    vv_how_to_video.start();
                    v.getBackground().setAlpha(0);
                } else {
                    vv_how_to_video.pause();
                    v.getBackground().setAlpha(128); //255
                }
            }
        });

        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey("title") && arguments.containsKey("subtitle")&& arguments.containsKey("sub")) {
                int title = getArguments().getInt("title");
                int subtitle = getArguments().getInt("subtitle");
                String sub = getArguments().getString("sub");
                updateViews(title, subtitle, sub);
            }
        }

        return root;
    }

    private void updateViews(int title, int subtitle, String sub) {

        // update the help topic title
        tv_help_title.setText("How to " + sub);

        // references for resources
        String[] steps;
        TypedArray vids;

        // fetch resources for specified help category
        switch (title) {
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
        etv.setText(steps[subtitle].replaceAll("[|]", "\n\n"));

        // update the video view with the proper video for this help item
        String path = "android.resource://" + getContext().getPackageName() + "/" + vids.getResourceId(subtitle, -1);
        Uri uri = Uri.parse(path);
        vv_how_to_video.setVideoURI(uri);

        vids.recycle();
    }
}
