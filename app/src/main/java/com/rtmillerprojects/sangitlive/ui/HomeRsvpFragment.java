package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.HomeRsvpdAdapter;
import com.rtmillerprojects.sangitlive.adapter.HomeUpcomingAdapter;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.UpcomingEventQuery;
import com.rtmillerprojects.sangitlive.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 9/2/2016.
 */
public class HomeRsvpFragment extends BaseFragment{
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DatabaseHelper db;
    private List<Long> favoritedEvents;
    private Context context;
    private HomeRsvpdAdapter rsvpAdapter;
    private ArrayList<BandsInTownEventResult> events = new ArrayList<>();
    private SwipeRefreshLayout rsvpSwipeRefresh;


    public static HomeRsvpFragment newInstance() {
        HomeRsvpFragment fragment = new HomeRsvpFragment();
        return fragment;
    }

    public HomeRsvpFragment() {
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
        rsvpSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.rsvpSwipeRefresh);

        layoutManager = new LinearLayoutManager(ACA);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        db = DatabaseHelper.getInstance(ACA);
        events = db.getAllEvents();
        context = getContext();
        rsvpAdapter = new HomeRsvpdAdapter(events,ACA);
        recyclerView.setAdapter(rsvpAdapter);
        recyclerView.setLayoutManager(layoutManager);

        rsvpSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
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
        // Load items
        // ...

        // Load complete
        events = null;
        rsvpAdapter = null;
        db = DatabaseHelper.getInstance(ACA);
        events = db.getAllEvents();
        rsvpAdapter = new HomeRsvpdAdapter(events,ACA);
        recyclerView.setAdapter(rsvpAdapter);
        recyclerView.setLayoutManager(layoutManager);
        rsvpSwipeRefresh.setRefreshing(false);
    }
}
