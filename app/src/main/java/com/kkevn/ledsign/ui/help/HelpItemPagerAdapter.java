/**
 * HelpItemPagerAdapter is the Adapter object used to contain the blueprint for how to adapt the
 * individual help page items into a single View.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.help;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HelpItemPagerAdapter extends FragmentStatePagerAdapter {

    // declare relevant variables
    int category, length;

    /**
     * Constructor for this HelpItemPagerAdapter.
     *
     * @param {FragmentManager} fragmentManager: Reference to the fragment manager.
     * @param {int} category: Index of this category of all the different available categories.
     * @param {int} length: Length of items in this help category.
     */
    public HelpItemPagerAdapter(FragmentManager fragmentManager, int category, int length) {
        super(fragmentManager);

        // initialize this adapter's variables
        this.category = category;
        this.length = length;
    }

    /**
     * Returns the count of help items in this help category.
     *
     * @return {int} Count of help items.
     */
    @Override
    public int getCount() {
        return this.length;
    }

    /**
     * Returns an instance of the help item in this help category at the specified position.
     *
     * @param {int} pos: Position of help item in this category.
     *
     * @return {Fragment} Fragment containing the specified help item.
     */
    @Override
    public Fragment getItem(int pos) {
        return HelpItemFragment.newInstance(this.category, pos);
    }
}