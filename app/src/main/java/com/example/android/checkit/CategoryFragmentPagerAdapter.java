package com.example.android.checkit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by jonathanbarrera on 9/25/18.
 */

public class CategoryFragmentPagerAdapter extends FragmentPagerAdapter {

    // Member variables
    private Context mContext;

    // Constructor
    public CategoryFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new CheckOutListFragment();
            case 1:
                return new ThingsListFragment();
            default:
                throw new IllegalArgumentException("Invalid fragment adapter position: " + position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Upcoming Checkouts";
            case 1:
                return "Things To Pack";
            default:
                throw new IllegalArgumentException("Invalid fragment adapter position: " + position);
        }
    }
}
