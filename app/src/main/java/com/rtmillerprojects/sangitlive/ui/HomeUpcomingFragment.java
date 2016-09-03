package com.rtmillerprojects.sangitlive.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.HomeUpcomingAdapter;

import java.util.ArrayList;

/**
 * Created by Ryan on 9/2/2016.
 */
public class HomeUpcomingFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private HomeUpcomingAdapter upcomingAdapter;


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

        recyclerView.setAdapter(upcomingAdapter);
        recyclerView.setLayoutManager(layoutManager);


        return rootView;
    }
}

