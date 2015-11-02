package com.codepath.instagram.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.instagram.fragments.SearchTagsResultFragment;
import com.codepath.instagram.fragments.SearchUsersResultFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

/**
 * Created by mrucker on 11/1/15.
 */
public class SearchFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {
    private FragmentManager fm;
    private CharSequence[] pageTitles = {
            "Users",
            "Tags"
    };

    public SearchFragmentStatePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fm = fragmentManager;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SearchUsersResultFragment searchUsersResultFragment = SearchUsersResultFragment.newInstance();
                return searchUsersResultFragment;
            case 1:
                SearchTagsResultFragment searchTagsResultFragment = SearchTagsResultFragment.newInstance();
                return searchTagsResultFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }
}
