package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.HomeUpcomingAdapter;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ryan on 9/2/2016.
 */
public class HomeHistoryFragment extends BaseFragment{
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DatabaseHelper db;
    private List<Long> favoritedEvents;
    private Context context;
    private HomeUpcomingAdapter eventHistoryAdapter;
    private ArrayList<BandsInTownEventResult> events = new ArrayList<>();
    private ArrayList<BandsInTownEventResult> scrubbedEvents = new ArrayList<>();
    private SwipeRefreshLayout historySwipeRefresh;


    public static HomeHistoryFragment newInstance() {
        HomeHistoryFragment fragment = new HomeHistoryFragment();
        return fragment;
    }

    public HomeHistoryFragment() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.home_rsvp_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rsvpd_events);
        historySwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.rsvpSwipeRefresh);

        layoutManager = new LinearLayoutManager(ACA);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        db = DatabaseHelper.getInstance(ACA);
        events = db.getAllEvents();
        context = getContext();
        Date d = new Date();
        for(BandsInTownEventResult e : events){
            if(e.getDatetime().before(d)){
                scrubbedEvents.add(e);
            }
        }
        eventHistoryAdapter = new HomeUpcomingAdapter(scrubbedEvents,ACA);
        recyclerView.setAdapter(eventHistoryAdapter);
        recyclerView.setLayoutManager(layoutManager);

        historySwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
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
        refreshItems();
    }

    void refreshItems() {
        events = null;
        scrubbedEvents = new ArrayList<>();
        eventHistoryAdapter = null;
        db = DatabaseHelper.getInstance(ACA);
        events = db.getAllEvents();
        Date d = new Date();
        for(BandsInTownEventResult e : events){
            if(e.getDatetime().before(d)){
                scrubbedEvents.add(e);
            }
        }
        eventHistoryAdapter = new HomeUpcomingAdapter(scrubbedEvents,ACA);
        recyclerView.setAdapter(eventHistoryAdapter);
        recyclerView.setLayoutManager(layoutManager);
        historySwipeRefresh.setRefreshing(false);
    }
}
