package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.SetAdapter;
import com.rtmillerprojects.sangitlive.api.SetlistTypeAdapterFactory;
import com.rtmillerprojects.sangitlive.listener.GetMbid;
import com.rtmillerprojects.sangitlive.model.EventCalls.SetlistRequest;
import com.rtmillerprojects.sangitlive.model.LoadSetlistsEvent;
import com.rtmillerprojects.sangitlive.model.SetInfo;
import com.rtmillerprojects.sangitlive.model.SetlistsByArtists;
import com.squareup.otto.Subscribe;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Created by Ryan on 9/2/2016.
 */
public class FragmentArtistSetlists extends BaseFragment {

    private int page = 1;
    private Button searchButton;
    private SetAdapter slAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private Context context;
    private boolean loading = true;
    private View rootView;
    private GetMbid listener;
    private TextView emptyView;
    private SetlistRequest sr;


    public static FragmentArtistSetlists newInstance() {
        FragmentArtistSetlists fragment = new FragmentArtistSetlists();
        return fragment;
    }

    public FragmentArtistSetlists() {
        // Required empty public constructor
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_artist_setlists, container, false);

        layoutManager = new LinearLayoutManager(ACA);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        searchButton = (Button) rootView.findViewById(R.id.searchButton);
        emptyView = (TextView) rootView.findViewById(R.id.empty_view);
        recyclerView.setLayoutManager(layoutManager);

        sr = new SetlistRequest(listener.getMbid(), 1);
        EventBus.post(sr);

        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(new SetlistTypeAdapterFactory());
        Gson gson = gsonBuilder.create();
        InputStream is = this.getResources().openRawResource(R.raw.ofmontreal);
        Reader reader = new InputStreamReader(is);
        if(recyclerView.getAdapter()==null || recyclerView.getAdapter().getItemCount()<1){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        listener = (ActivityArtistDetail) context;
    }

    @Subscribe
    public void onReceiveSetlists(LoadSetlistsEvent event){
        ArrayList<SetInfo> newSetInfo = buildSetInfo(event.getSetlists());
        slAdapter = new SetAdapter(newSetInfo,context);
        recyclerView.setAdapter(slAdapter);
        recyclerView.setLayoutManager(layoutManager);
        if (event.getSetlists().getSetlists().getSetlist().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    public ArrayList<SetInfo> buildSetInfo(SetlistsByArtists apiReturn){
        ArrayList<SetInfo> setInfoList = new ArrayList<>();
        SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean set;
        String song;
        ArrayList<String> songs = new ArrayList<>();
        int instanceCounter = 0;
        int totalSetsCounted = 0;
        for (SetlistsByArtists.SetlistsBean.SetlistBean sl : apiReturn.getSetlists().getSetlist()) {
            //PUT SETS OUT HERE1!!!
            SetInfo si = new SetInfo();
            si.setCity(sl.getVenue().getCity().getName() + ", " + sl.getVenue().getCity().getCountry().getName());
            si.setDate(sl.getEventDate());
            si.setVenue(sl.getVenue().getName());
            si.setTourName(sl.getTour());
            for (int i = 0; i < sl.getSets().getSet().size(); i++) {
                set = sl.getSets().getSet().get(i);
                if (set.getSong() != null) {
                    for (int k = 0; k < set.getSong().size(); k++) {
                        songs.add(set.getSong().get(k).getName());
                    }
                }
            }
            si.setSongs(songs);
            songs = new ArrayList<String>();
            setInfoList.add(si);
            totalSetsCounted++;
        }

        return setInfoList;
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

