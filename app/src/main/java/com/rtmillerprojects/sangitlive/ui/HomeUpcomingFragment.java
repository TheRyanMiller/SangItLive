package com.rtmillerprojects.sangitlive.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.HomeUpcomingAdapter;
import com.rtmillerprojects.sangitlive.model.EventCalls.NameMbidPair;
import com.rtmillerprojects.sangitlive.model.EventCalls.UpcomingEventQuery;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.util.DatabaseHelper;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Ryan on 9/2/2016.
 */
public class HomeUpcomingFragment extends BaseFragment {

    public static final String TAG = "HomeUpcomingFragment";
    private static final String KEY_BUNDLE_PAGE_NUMBER = "page_number";
    private static final String KEY_BUNDLE_UPCOMING_EVENTS = "upcomingList";
    private static final String KEY_BUNDLE_FIRST_VISIBLE_ITEM = "first_visible_event";
    private static final String KEY_BUNDLE_SCROLL_OFFSET = "scroll_offset";
    private static final String KEY_BUNDLE_TOTAL_EVENTS = "total_num_event";
    private static final String KEY_BUNDLE_LAST_REFRESH_DATE = "last_refresh_date";
    private int mScrollPosition = 0;
    private int mScrollOffset = 0;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private HomeUpcomingAdapter upcomingAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<String> mbids;
    private int page = 0;
    private Date refreshDate;
    private ArrayList<BandsInTownEventResult> events = new ArrayList<>();
    private ProgressBar mProgressBar;
    private int mTotalEvents;
    private TextView emptyView;
    private DatabaseHelper db;
    private ArrayList<NameMbidPair> nameMbidPairs = new ArrayList<>();;


    public static HomeUpcomingFragment newInstance() {
        HomeUpcomingFragment fragment = new HomeUpcomingFragment();
        return fragment;
    }

    public HomeUpcomingFragment() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
        }

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.home_upcoming_fragment, container, false);
        Fabric.with(ACA, new Crashlytics());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_home_upcoming);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);

        layoutManager = new LinearLayoutManager(ACA);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //Test Data
        db = DatabaseHelper.getInstance(ACA);
        mbids = (ArrayList<String>) db.getFavoritedArtistMbids();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                // Refresh items
                swipeRefreshLayout.setRefreshing(true);
                refreshItems();
            }
        });


        return rootView;
    }

    @Subscribe
    public void receiveEventResults(ArrayList<BandsInTownEventResult> apiEvents) {
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        if(events==null || events.size()==0){
            //do something if null
            events = apiEvents;
            Collections.sort(events, new Comparator<BandsInTownEventResult>() {
                @Override
                public int compare(BandsInTownEventResult o1, BandsInTownEventResult o2) {
                    return o1.getDatetime().compareTo(o2.getDatetime());
                }
            });
            upcomingAdapter = new HomeUpcomingAdapter(events,getContext());
            recyclerView.setAdapter(upcomingAdapter);
            recyclerView.setLayoutManager(layoutManager);
        }
        else {
            for(BandsInTownEventResult event : apiEvents){
                events.add(event);
            }
            //upcomingAdapter = new HomeUpcomingAdapter(events,ACA);
            Collections.sort(events, new Comparator<BandsInTownEventResult>() {
                @Override
                public int compare(BandsInTownEventResult o1, BandsInTownEventResult o2) {
                    return o1.getDatetime().compareTo(o2.getDatetime());
                }
            });
            upcomingAdapter.notifyDataSetChanged();
        }
        mProgressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        if(events.size()==0){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.register(this);
        refreshItems();
        if(events != null && events.size()>0) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.unregister(this);
    }

    void refreshItems() {
        events = new ArrayList<>();
        db = DatabaseHelper.getInstance(ACA);
        mbids = (ArrayList<String>) db.getFavoritedArtistMbids();
        nameMbidPairs = (ArrayList<NameMbidPair>) db.getFavoritedNameMbidPairs();
        EventBus.post(new UpcomingEventQuery(nameMbidPairs,1,false));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /**
         * Take a look at the model objects that and you'll see
         * why Parceler is so fantastic.  1 annotation in each model object and we
         * get to use Android's amazingly fast Parcelable.
         */
    }


}

