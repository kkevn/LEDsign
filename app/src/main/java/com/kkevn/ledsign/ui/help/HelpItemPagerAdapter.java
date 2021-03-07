package com.kkevn.ledsign.ui.help;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HelpItemPagerAdapter extends FragmentStatePagerAdapter {

    int category, length;

    public HelpItemPagerAdapter(FragmentManager fm, int category, int length) {
        super(fm);

        this.category = category;
        this.length = length;
    }

    @Override
    public int getCount() {
        return length;
    }

    @Override
    public Fragment getItem(int i) {
        return HelpItemFragment.newInstance(category, i);
    }
}