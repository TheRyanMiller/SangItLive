package com.rtmillerprojects.sangitlive.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.ArtistsSectionsPagerAdapter;
import com.rtmillerprojects.sangitlive.listener.GetMbid;
import com.rtmillerprojects.sangitlive.listener.MainListener;
import com.rtmillerprojects.sangitlive.model.ArtistDetails;
import com.rtmillerprojects.sangitlive.model.EventCalls.BITResultPackage;
import com.rtmillerprojects.sangitlive.model.EventCalls.CheckInternetStatus;
import com.rtmillerprojects.sangitlive.model.EventCalls.EventManagerRequest;
import com.rtmillerprojects.sangitlive.model.EventCalls.LastFmArtistDetails;
import com.rtmillerprojects.sangitlive.model.EventCalls.NameMbidPair;
import com.rtmillerprojects.sangitlive.model.EventCalls.ReturnInternetStatus;
import com.rtmillerprojects.sangitlive.model.EventCalls.UpcomingEventQuery;
import com.rtmillerprojects.sangitlive.model.lastfmartistsearch.ArtistLastFm;
import com.rtmillerprojects.sangitlive.util.DatabaseHelper;
import com.rtmillerprojects.sangitlive.util.EventManagerService;
import com.rtmillerprojects.sangitlive.util.SharedPreferencesHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by Ryan on 9/2/2016.
 */
public class FragmentArtistMain extends BaseFragment{

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ReturnInternetStatus ris;
    FloatingActionButton fab;
    String mbid;
    GetMbid mbidListener;
    AppCompatCheckBox starArtist;
    DatabaseHelper db;
    String artistName;
    ArtistDetails adr;
    EventManagerService ems;

    private MainListener listener;
    private ArtistsSectionsPagerAdapter artistPagerAdapter;

    public static FragmentArtistMain newInstance() {
        FragmentArtistMain fragment = new FragmentArtistMain();
        return fragment;
    }

    public FragmentArtistMain() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHelper.getInstance(ACA);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_artist, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        starArtist = (AppCompatCheckBox) rootView.findViewById(R.id.save_artist_star);
        mbid = mbidListener.getMbid();
        artistName = mbidListener.getArtistName();
        final ArtistDetails ad = new ArtistDetails();
        adr = ad;
        ad.setMbid(mbid);
        ad.setName(artistName);

        fab.hide();
        String toolbarTitle=getArguments().getString("artistName");

        //Configure Toolbar back button
        toolbar.setTitle(toolbarTitle);
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
                            //Turn toolbar into
                        }
                    });
                } else {
                    fab.hide();
                }
            }

            @Override public void onPageScrollStateChanged(int state) {
            }
        });



        starArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(starArtist.isChecked()==false){
                    new AlertDialog.Builder(ACA)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Confirm selection")
                            .setMessage("Unfollowing this artist will remove their events from your feed.")
                            .setPositiveButton("Unfollow", new DialogInterface.OnClickListener() {


                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.deleteArtist(ad.getName(),ad.getMbid());
                                    db.deleteEventsAllByArtist(ad.getName(),ad.getMbid());
                                    db.deleteEventsLocalByArtist(ad.getMbid(), ad.getName());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    starArtist.setChecked(true);
                                }
                            })
                            .show();
                }
                else{
                    ProgressDialog pd = new ProgressDialog(ACA);
                    pd.show();
                    pd.setMessage("Waiting");
                    pd.setCancelable(false);
                    db.insertArtist(ad);
                    ArrayList<NameMbidPair> pairs = new ArrayList<NameMbidPair>();
                    pairs.add(new NameMbidPair(ad.getName(),ad.getMbid()));

                    ems = EventManagerService.getInstance(ACA);
                    ems.getSingleArtistEventsAll(new NameMbidPair(ad.getName(),ad.getMbid()));
                    if(SharedPreferencesHelper.isUserLocationSet()){
                        ems.getSingleArtistEventsLocal(new NameMbidPair(ad.getName(),ad.getMbid()));
                    }
                    pd.cancel();
                    Toast.makeText(ACA,"You are now following "+ad.getName(),Toast.LENGTH_SHORT).show();
                }

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mbidListener = (ActivityArtistDetail) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshStar();
    }

    @Subscribe
    public void recieveShows(BITResultPackage showResults){
        DatabaseHelper db = DatabaseHelper.getInstance(ACA);
        if(showResults.isLocationFiltered){
            db.insertEventsLocal(showResults.events);
        }
        else{
            db.insertEventsAll(showResults.events);
        }
    }

    public void refreshStar(){
        if(starArtist!=null && adr!=null){
            //This produces a bad result for the artist "LOGIC"
            if(db.getArtistById(adr.getMbid())==null){
                starArtist.setChecked(false);
            }
            else{
                starArtist.setChecked(true);
            }
        }
    }
}
