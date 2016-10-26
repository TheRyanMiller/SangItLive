package com.rtmillerprojects.sangitlive.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.HomeUpcomingAdapter;
import com.rtmillerprojects.sangitlive.model.EventCalls.BITResultPackage;
import com.rtmillerprojects.sangitlive.model.EventCalls.MBBrowseList;
import com.rtmillerprojects.sangitlive.model.EventCalls.NameMbidPair;
import com.rtmillerprojects.sangitlive.model.EventCalls.UpcomingEventQuery;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.util.DatabaseHelper;
import com.rtmillerprojects.sangitlive.util.SharedPreferencesHelper;
import com.squareup.otto.Subscribe;

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

    private boolean filterActivated = false;
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
    private RelativeLayout emptyView;
    private TextView emptyViewText;
    private DatabaseHelper db;
    private ArrayList<NameMbidPair> nameMbidPairs = new ArrayList<>();
    private String emptyViewMsg;


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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_home_upcoming);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        emptyView = (RelativeLayout) rootView.findViewById(R.id.empty_view);
        emptyViewText = (TextView) rootView.findViewById(R.id.empty_view_text);

        layoutManager = new LinearLayoutManager(ACA);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //Test Data
        db = DatabaseHelper.getInstance(ACA);
        mbids = (ArrayList<String>) db.getFavoritedArtistMbids();
        emptyViewMsg = "There are no upcoming events to display. \n You are currently tracking "+mbids.size()+ " artist(s).";
        emptyViewText.setText(emptyViewMsg);

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
    public void switchFilter(SetLocalFilter filter){
        refreshItems();
    }

    void refreshItems() {
        events = new ArrayList<>();
        db = DatabaseHelper.getInstance(ACA);
        filterActivated=SharedPreferencesHelper.getBoolean(getString(R.string.is_Filtered),false);
        mbids = (ArrayList<String>) db.getFavoritedArtistMbids();
        emptyViewMsg = "There are no upcoming events to display.\nYou are currently tracking "+mbids.size()+ " artist(s).";
        emptyViewText.setText(emptyViewMsg);
        nameMbidPairs = (ArrayList<NameMbidPair>) db.getFavoritedNameMbidPairs();
        //EventBus.post(new UpcomingEventQuery(nameMbidPairs,1,false));
        if(filterActivated){
            events = db.getEventsLocal();
        }
        else{
            events = db.getEventsAll(true);
        }
        recyclerView.setVisibility(View.VISIBLE);
        if(events==null || events.size()==0){
            //do something if null
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            emptyView.setVisibility(View.GONE);
            upcomingAdapter = new HomeUpcomingAdapter(events,getContext());
            recyclerView.setAdapter(upcomingAdapter);
            recyclerView.setLayoutManager(layoutManager);
        }
        swipeRefreshLayout.setRefreshing(false);
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

