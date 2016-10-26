package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private Context context;
    private HomeUpcomingAdapter eventHistoryAdapter;
    private ArrayList<BandsInTownEventResult> events = new ArrayList<>();
    private SwipeRefreshLayout historySwipeRefresh;
    private String emptyViewMsg;
    private RelativeLayout emptyView;
    private TextView emptyViewText;


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
        emptyView = (RelativeLayout) rootView.findViewById(R.id.empty_view);
        emptyViewText = (TextView) rootView.findViewById(R.id.empty_view_text);

        layoutManager = new LinearLayoutManager(ACA);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        db = DatabaseHelper.getInstance(ACA);
        events = db.getEventsAttending(false);
        if(events!=null && events.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            eventHistoryAdapter = new HomeUpcomingAdapter(events,ACA);
            recyclerView.setAdapter(eventHistoryAdapter);
            recyclerView.setLayoutManager(layoutManager);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyViewMsg = "Cannot find any shows you've attended. \n (This section will display any past shows you've starred.)";
        }

        emptyViewText.setText(emptyViewMsg);
        context = getContext();


        historySwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshItems();
    }

    void refreshItems() {
        events = null;
        eventHistoryAdapter = null;
        db = DatabaseHelper.getInstance(ACA);
        events = db.getEventsAttending(false);
        if(events!=null && events.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            eventHistoryAdapter = new HomeUpcomingAdapter(events,ACA);
            recyclerView.setAdapter(eventHistoryAdapter);
            recyclerView.setLayoutManager(layoutManager);
            historySwipeRefresh.setRefreshing(false);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyViewMsg = "Past events that you've RSVP'd to will appear here to display a list of shows you've attended.";
        }

    }
}
