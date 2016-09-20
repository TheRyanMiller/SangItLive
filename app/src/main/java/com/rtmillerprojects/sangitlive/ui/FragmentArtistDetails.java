package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.HomeUpcomingAdapter;
import com.rtmillerprojects.sangitlive.model.ArtistImageEvent;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.EventCalls.LastFmArtistDetails;
import com.rtmillerprojects.sangitlive.model.lastfmartistsearch.ArtistLastFm;
import com.rtmillerprojects.sangitlive.util.CircleTransform;
import com.rtmillerprojects.sangitlive.listener.GetMbid;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ryan on 9/2/2016.
 */
public class FragmentArtistDetails extends BaseFragment {

    public static final String TAG = "FragmentArtistDetails";
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
    private ImageView artistImg;
    private ArtistImageEvent msg;
    private GetMbid listener;
    private LastFmArtistDetails lfad;


    public static FragmentArtistDetails newInstance() {
        FragmentArtistDetails fragment = new FragmentArtistDetails();
        return fragment;
    }

    public FragmentArtistDetails() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_artist_details, container, false);
        artistImg = (ImageView) rootView.findViewById(R.id.artist_img);

        lfad = new LastFmArtistDetails(listener.getMbid());
        EventBus.post(lfad);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ActivityArtistDetail) context;
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

    @Subscribe
    public void getArtistDetails(ArtistLastFm artistDetails){
        try {
            Picasso.with(ACA).load(artistDetails.getArtist().getImage().get(2).getText())
                    .placeholder(R.drawable.ic_person_grey600_24dp)
                    .transform(new CircleTransform())
                    .into(artistImg);
        }
        catch (IndexOutOfBoundsException e) {
            Log.e(e.getClass().getName(),e.getMessage());
        }
    }


}

