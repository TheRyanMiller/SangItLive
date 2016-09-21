package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.HomeUpcomingAdapter;
import com.rtmillerprojects.sangitlive.listener.GetMbid;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.UpcomingEventQuery;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Ryan on 9/2/2016.
 */
public class FragmentArtistUpcoming extends BaseFragment {

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
    private GetMbid listener;


    public static FragmentArtistUpcoming newInstance() {
        FragmentArtistUpcoming fragment = new FragmentArtistUpcoming();
        return fragment;
    }

    public FragmentArtistUpcoming() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            if (savedInstanceState.containsKey(KEY_BUNDLE_UPCOMING_EVENTS)) {
                events = Parcels.unwrap(savedInstanceState.getParcelable(KEY_BUNDLE_UPCOMING_EVENTS));
            }
            if (savedInstanceState.containsKey(KEY_BUNDLE_FIRST_VISIBLE_ITEM) && savedInstanceState.containsKey(KEY_BUNDLE_SCROLL_OFFSET)) {
                mScrollPosition = savedInstanceState.getInt(KEY_BUNDLE_FIRST_VISIBLE_ITEM, 0);
                mScrollOffset = savedInstanceState.getInt(KEY_BUNDLE_SCROLL_OFFSET, 0);
            }
            if (savedInstanceState.containsKey(KEY_BUNDLE_TOTAL_EVENTS)) {
                mTotalEvents = savedInstanceState.getInt(KEY_BUNDLE_TOTAL_EVENTS, 0);
            }
            if (savedInstanceState.containsKey(KEY_BUNDLE_LAST_REFRESH_DATE)) {
                long a = savedInstanceState.getLong(KEY_BUNDLE_LAST_REFRESH_DATE,0);
                refreshDate = new Date();
                refreshDate.setTime(a);
            }
        }

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.home_upcoming_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_home_upcoming);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        layoutManager = new LinearLayoutManager(ACA);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //Test Data
        mbids = new ArrayList<>();
        mbids.add(listener.getMbid());


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });




        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ActivityArtistDetail) context;
    }

    @Subscribe
    public void receiveEventResults(ArrayList<BandsInTownEventResult> apiEvents) {
        Toast.makeText(ACA,"EVENTS RETURNED",Toast.LENGTH_SHORT).show();
        if(events==null || events.size()==0){
            //do something if null
            events = apiEvents;
            Collections.sort(events, new Comparator<BandsInTownEventResult>() {
                @Override
                public int compare(BandsInTownEventResult o1, BandsInTownEventResult o2) {
                    return o1.getDatetime().compareTo(o2.getDatetime());
                }
            });
            if(upcomingAdapter==null){
                upcomingAdapter = new HomeUpcomingAdapter(events, ACA);
                recyclerView.setAdapter(upcomingAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ACA));
            }
            else{
                upcomingAdapter = new HomeUpcomingAdapter(events, ACA);
                upcomingAdapter.notifyDataSetChanged();
            }

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
            recyclerView.setAdapter(upcomingAdapter);
            recyclerView.setLayoutManager(layoutManager);
        }
        mProgressBar.setVisibility(View.GONE);
        refreshDate = new Date();
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

    void refreshItems() {
        // Load items
        // ...

        // Load complete
        events = null;
        EventBus.post(new UpcomingEventQuery(mbids,1));
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }

    private void recordScrollPosition() {
        /**
         * In addition to grabbing the top visible item also store the view offset
         * so we can truly keep our view exactly where it was.
         */
        mScrollPosition = recyclerView.getVerticalScrollbarPosition();
        View view = recyclerView.getChildAt(0);
        mScrollOffset = (view == null) ? 0 : view.getTop();
    }
    private void fetchMoreEvents(int pageLimit) {
        recordScrollPosition();
        mProgressBar.setVisibility(View.VISIBLE);
        EventBus.post(new UpcomingEventQuery(mbids,1));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /**
         * Take a look at the model objects that and you'll see
         * why Parceler is so fantastic.  1 annotation in each model object and we
         * get to use Android's amazingly fast Parcelable.
         */
        outState.putLong(KEY_BUNDLE_LAST_REFRESH_DATE, refreshDate != null ? refreshDate.getTime() : null);
        outState.putParcelable(KEY_BUNDLE_UPCOMING_EVENTS, Parcels.wrap(events));
        outState.putInt(KEY_BUNDLE_FIRST_VISIBLE_ITEM, mScrollPosition);
        outState.putInt(KEY_BUNDLE_SCROLL_OFFSET, mScrollOffset);
        outState.putInt(KEY_BUNDLE_PAGE_NUMBER, page);
        outState.putInt(KEY_BUNDLE_TOTAL_EVENTS, mTotalEvents);
    }


}

