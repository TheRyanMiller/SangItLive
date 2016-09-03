package com.rtmillerprojects.sangitlive.ui;

import android.os.Bundle;
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

/**
 * Created by Ryan on 9/2/2016.
 */
public class HomeUpcomingFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private HomeUpcomingAdapter upcomingAdapter;
    private ArrayList<String> mbids;


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

        layoutManager = new LinearLayoutManager(ACA);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //Test Data
        mbids = new ArrayList<>();
        mbids.add("a74b1b7f-71a5-4011-9441-d0b5e4122711");
        EventBus.post(new UpcomingEventQuery(mbids,1));



        return rootView;
    }

    @Subscribe
    public void receiveEventResults(ArrayList<BandsInTownEventResult> events) {
        Toast.makeText(ACA,"EVENTS RETURNED",Toast.LENGTH_SHORT).show();
        if(events==null || events.size()==0){
            //do something if null
            upcomingAdapter = new HomeUpcomingAdapter(null, ACA);
            recyclerView.setAdapter(upcomingAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(ACA));
        }
        else {
            upcomingAdapter = new HomeUpcomingAdapter(events,ACA);
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
}

