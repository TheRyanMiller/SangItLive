package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.SectionsPagerAdapter;
import com.rtmillerprojects.sangitlive.listener.MainListener;

/**
 * Created by Ryan on 9/2/2016.
 */
public class MainFragment extends BaseFragment{

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton fab;

    private MainListener listener;
    private SectionsPagerAdapter sectionsPagerAdapter;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MainActivity) context;
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

        toolbar.setTitle(getResources().getString(R.string.toolbar_title));
        ACA.setSupportActionBar(toolbar);

        // Create the adapter that will return fragments for the view pager
        //UNCOMMENT!!!
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                if (position == 1) {
                    fab.hide();
                } else {
                    fab.show();
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


        DrawerLayout drawer = listener.getDrawer();
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(ACA, drawer, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        return rootView;
    }

    @Override public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
