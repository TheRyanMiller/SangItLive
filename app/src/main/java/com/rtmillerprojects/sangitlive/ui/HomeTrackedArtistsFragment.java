package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.HomeRsvpdAdapter;
import com.rtmillerprojects.sangitlive.adapter.HomeTrackedArtistsAdapter;
import com.rtmillerprojects.sangitlive.model.ArtistDetails;
import com.rtmillerprojects.sangitlive.model.ArtistImageEvent;
import com.rtmillerprojects.sangitlive.model.ArtistImageFound;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.lastfmartistsearch.ArtistLastFm;
import com.rtmillerprojects.sangitlive.util.DatabaseHelper;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 9/7/2016.
 */
public class HomeTrackedArtistsFragment extends BaseFragment{

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DatabaseHelper db;
    private List<Long> favoritedEvents;
    private Context context;
    private HomeTrackedArtistsAdapter trackedArtistAdapter;
    private ArrayList<ArtistLastFm> artists = new ArrayList<>();
    private SwipeRefreshLayout trackedArtistSwipeRefresh;
    private int returnCounter;
    private int sizeOfArtists;


    public static HomeTrackedArtistsFragment newInstance() {
        HomeTrackedArtistsFragment fragment = new HomeTrackedArtistsFragment();
        return fragment;
    }

    public HomeTrackedArtistsFragment() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.home_tracked_artists_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.tracked_artists);
        trackedArtistSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.rsvpSwipeRefresh);

        layoutManager = new LinearLayoutManager(ACA);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        db = DatabaseHelper.getInstance(ACA);
        artists = db.getAllArtists();
        context = getContext();
        trackedArtistAdapter = new HomeTrackedArtistsAdapter(artists,ACA);
        recyclerView.setAdapter(trackedArtistAdapter);
        recyclerView.setLayoutManager(layoutManager);

        trackedArtistSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        //recyclerView.setAdapter(upcomingAdapter);
        //recyclerView.setLayoutManager(layoutManager);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.register(this);
        refreshItems();

    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.unregister(this);
    }

    @Subscribe
    public void pullImages(ArtistLastFm returnedData){

        artists.add(returnedData);
        returnCounter++;
        if(returnCounter == sizeOfArtists) {
            trackedArtistAdapter = new HomeTrackedArtistsAdapter(artists, ACA);
            recyclerView.setAdapter(trackedArtistAdapter);
            recyclerView.setLayoutManager(layoutManager);
            trackedArtistSwipeRefresh.setRefreshing(false);
            returnCounter = 0;
        }
    }

    void refreshItems() {
        artists = null;
        trackedArtistAdapter = null;
        db = DatabaseHelper.getInstance(ACA);
        artists = db.getAllArtists();
        ArrayList<String> mbids = new ArrayList<>();
        for (int i = 0; i < artists.size(); i++) {
            mbids.add(artists.get(i).getArtist().getMbid());
        }
        sizeOfArtists = artists.size();
        artists = new ArrayList<ArtistLastFm>();
        //recyclerView.setAdapter(new HomeTrackedArtistsAdapter(artists,ACA));
        EventBus.post(new ArtistImageEvent(mbids));
        onItemsLoadComplete();

    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        trackedArtistSwipeRefresh.setRefreshing(false);
    }
}
