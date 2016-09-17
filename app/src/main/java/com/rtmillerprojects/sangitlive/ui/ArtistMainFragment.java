package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.ArtistsSectionsPagerAdapter;
import com.rtmillerprojects.sangitlive.listener.MainListener;

/**
 * Created by Ryan on 9/2/2016.
 */
public class ArtistMainFragment extends BaseFragment{

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton fab;

    private MainListener listener;
    private ArtistsSectionsPagerAdapter artistPagerAdapter;

    public static ArtistMainFragment newInstance() {
        ArtistMainFragment fragment = new ArtistMainFragment();
        return fragment;
    }

    public ArtistMainFragment() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home_main, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        fab.hide();

        //Configure Toolbar back button
        toolbar.setTitle("Artist");
        ACA.setSupportActionBar(toolbar);
        ACA.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ACA.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ACA.onBackPressed();
            }
        });

        // Create the adapter that will return fragments for the view pager
        //UNCOMMENT!!!
        artistPagerAdapter = new ArtistsSectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager.setAdapter(artistPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                if (position == 2) {
                    fab.show();
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ACA, ActivityArtistSearch.class);
                            ACA.startActivity(intent);
                            ACA.overridePendingTransition(R.anim.slide_in_left,R.anim.stay);
                        }
                    });
                } else {
                    fab.hide();
                }
            }

            @Override public void onPageScrollStateChanged(int state) {
            }
        });


        // Set up the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);


        // Listen for fab clicks
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

            }
        });


        return rootView;
    }

    @Override public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
