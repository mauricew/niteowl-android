package com.getniteowl.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.getniteowl.fragments.DashboardActivityFragment;
import com.getniteowl.fragments.PartyCentralInfoFragment;
import com.getniteowl.fragments.PartyCentralMembersFragment;
import com.getniteowl.fragments.PartyCentralPhotosFragment;
import com.getniteowl.models.Party;

import java.security.InvalidParameterException;

/**
 * Created by Maurice Wahba on 6/22/2015.
 */
public class PartyCentralPagerAdapter extends FragmentStatePagerAdapter {
    final int SECTION_COUNT = 3;
    private String[] tabTitles = new String[] { "Info", "Members", "Photos" };
    private Party party;

    public PartyCentralPagerAdapter(FragmentManager fm, Party party) {
        super(fm);
        this.party = party;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return PartyCentralInfoFragment.newInstance(party);
            case 1:
                return PartyCentralMembersFragment.newInstance(party);
            case 2:
                return PartyCentralPhotosFragment.newInstance(party);
            default:
                throw new InvalidParameterException();
        }
    }

    @Override
    public int getCount() {
        return SECTION_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
