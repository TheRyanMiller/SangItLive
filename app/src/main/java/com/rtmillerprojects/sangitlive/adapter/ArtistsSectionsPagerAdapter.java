package com.rtmillerprojects.sangitlive.adapter;

/**
 * Created by tylerjroach on 10/17/15.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.rtmillerprojects.sangitlive.ui.FragmentArtistDetails;
import com.rtmillerprojects.sangitlive.ui.FragmentArtistSetlists;
import com.rtmillerprojects.sangitlive.ui.FragmentArtistUpcoming;
import com.rtmillerprojects.sangitlive.ui.HomeHistoryFragment;
import com.rtmillerprojects.sangitlive.ui.HomeRsvpFragment;
import com.rtmillerprojects.sangitlive.ui.HomeTrackedArtistsFragment;
import com.rtmillerprojects.sangitlive.ui.HomeUpcomingFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ArtistsSectionsPagerAdapter extends FragmentPagerAdapter {

  public final int ARTIST_DETAILS = 0;
  public final int ARTIST_UPCOMING = 1;
  public final int PAST_SETLISTS = 2;

  public ArtistsSectionsPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {

    Fragment pagerFragment = null;
    switch (position) {
      case ARTIST_DETAILS:
        pagerFragment = FragmentArtistDetails.newInstance();
        break;

      case ARTIST_UPCOMING:
        pagerFragment = FragmentArtistUpcoming.newInstance();
        break;

      case PAST_SETLISTS:
        pagerFragment = FragmentArtistSetlists.newInstance();
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
        return "Details";
      case 1:
        return "Upcoming";
      case 2:
        return "Setlists";
    }
    return null;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    // TODO Auto-generated method stub
    //super.destroyItem(ViewGroup container, int position, Object object);
  }
}