package com.kkevn.ledsign.ui.help;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kkevn.ledsign.R;

public class HelpViewPagerFragment extends Fragment {

    public final static String CATEGORY = "HELP_CATEGORY";
    public final static String CATEGORY_INDEX = "HELP_CATEGORY_INDEX";
    public final static String CATEGORY_LENGTH = "HELP_CATEGORY_LENGTH";

    private ViewPager vp_slide;
    private HelpItemPagerAdapter adapter;

    private LinearLayout ll_page_indicator;
    private ImageView[] iv_dots;

    int category, index, length = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.pager_help_items, container, false);

        // obtain Bundle values based on selection made in help menu
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(CATEGORY) && arguments.containsKey(CATEGORY_INDEX) && arguments.containsKey(CATEGORY_LENGTH)) {
                category = getArguments().getInt(CATEGORY);
                index = getArguments().getInt(CATEGORY_INDEX);
                length = getArguments().getInt(CATEGORY_LENGTH);
            }
        }

        // obtain references to views in layout
        vp_slide = root.findViewById(R.id.vp_slide);
        ll_page_indicator = root.findViewById(R.id.ll_page_indicator);

        // set ViewPager's adapter, update view to current help item, and set a listener
        adapter = new HelpItemPagerAdapter(getChildFragmentManager(), category, length);
        vp_slide.setAdapter(adapter);
        vp_slide.setCurrentItem(index);
        vp_slide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                createDots(i);  // update dot indicators based on current page
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // update dot indicators based on current page
        createDots(index);

        return root;
    }

    private void createDots(int pos) {
        if (ll_page_indicator != null) {
            ll_page_indicator.removeAllViews();
        }

        // create array of ImageView for length of this help category
        iv_dots = new ImageView[length];

        // iterate over each dot in the array
        for (int i = 0; i < iv_dots.length; i++) {

            // initialize an ImageView at this index
            iv_dots[i] = new ImageView(getContext());

            // draw the active dot for the current page only
            if (i == pos) {
                iv_dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.indicator_dot_active));
            } else {
                iv_dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.indicator_dot_inactive));
            }

            // setup layout parameters for the dots
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);

            // add the dots and layout parameters to the layout
            ll_page_indicator.addView(iv_dots[i], params);
        }
    }
}