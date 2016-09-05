package com.rtmillerprojects.sangitlive.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.HomeUpcomingAdapter;
import com.rtmillerprojects.sangitlive.api.ServiceUpcomingEvents;
import com.rtmillerprojects.sangitlive.model.UpcomingEventQuery;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ryan on 9/2/2016.
 */
public class HomeUpcomingFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private HomeUpcomingAdapter upcomingAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<String> mbids;
    private Date refreshDate;
    private ArrayList<BandsInTownEventResult> events = new ArrayList<>();


    public static HomeUpcomingFragment newInstance() {
        HomeUpcomingFragment fragment = new HomeUpcomingFragment();
        return fragment;
    }

    public HomeUpcomingFragment() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.home_upcoming_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_home_upcoming);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

        layoutManager = new LinearLayoutManager(ACA);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //Test Data
        mbids = new ArrayList<>();
        mbids.add("65f4f0c5-ef9e-490c-aee3-909e7ae6b2ab");
        mbids.add("a74b1b7f-71a5-4011-9441-d0b5e4122711");

        if(refreshDate==null){
            refreshDate = new Date();
            EventBus.post(new UpcomingEventQuery(mbids,1));
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });




        return rootView;
    }

    @Subscribe
    public void receiveEventResults(ArrayList<BandsInTownEventResult> apiEvents) {
        Toast.makeText(ACA,"EVENTS RETURNED",Toast.LENGTH_SHORT).show();
        if(events==null || events.size()==0){
            //do something if null
            events = apiEvents;
            upcomingAdapter = new HomeUpcomingAdapter(events, ACA);
            recyclerView.setAdapter(upcomingAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(ACA));
        }
        else {
            for(BandsInTownEventResult event : apiEvents){
                events.add(event);
            }
            //upcomingAdapter = new HomeUpcomingAdapter(events,ACA);
            upcomingAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(upcomingAdapter);
            recyclerView.setLayoutManager(layoutManager);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.unregister(this);
    }

    void refreshItems() {
        // Load items
        // ...

        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }
}

