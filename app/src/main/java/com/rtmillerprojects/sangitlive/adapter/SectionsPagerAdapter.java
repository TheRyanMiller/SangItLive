package com.rtmillerprojects.sangitlive.adapter;

/**
 * Created by tylerjroach on 10/17/15.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rtmillerprojects.sangitlive.ui.HomeHistoryFragment;
import com.rtmillerprojects.sangitlive.ui.HomeRsvpFragment;
import com.rtmillerprojects.sangitlive.ui.HomeUpcomingFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

  public final int HOME_UPCOMING = 0;
  public final int HOME_RSVP = 1;
  public final int HOME_HISTORY = 2;

  public SectionsPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {

    Fragment pagerFragment = null;
    switch (position) {
      case HOME_UPCOMING:
        pagerFragment = HomeUpcomingFragment.newInstance();
        break;

      case HOME_RSVP:
        pagerFragment = HomeRsvpFragment.newInstance();
        break;

      case HOME_HISTORY:
        pagerFragment = HomeHistoryFragment.newInstance();
        break;
    }
    return pagerFragment;
  }

  @Override public int getCount() {
    // Show 3 total pages.
    return 3;
  }

  @Override public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return "Upcoming";
      case 1:
        return "RSVP'd";
      case 2:
        return "History";
    }
    return null;
  }
}