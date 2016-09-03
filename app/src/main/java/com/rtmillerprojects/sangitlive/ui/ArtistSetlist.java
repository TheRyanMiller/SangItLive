package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.SetAdapter;
import com.rtmillerprojects.sangitlive.api.DoRestEvent;
import com.rtmillerprojects.sangitlive.api.SetlistService;
import com.rtmillerprojects.sangitlive.api.SetlistTypeAdapterFactory;
import com.rtmillerprojects.sangitlive.model.LoadSetlistsEvent;
import com.rtmillerprojects.sangitlive.model.SetInfo;
import com.rtmillerprojects.sangitlive.model.SetlistsByArtists;
import com.squareup.otto.Subscribe;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ArtistSetlist extends AppCompatActivity {

    private int page = 1;
    private Button searchButton;
    private SetAdapter slAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextInputLayout songString;
    //private EditText songString;
    private TextView resultsMsg;
    private SetlistService setlistService;
    private Button btnChooseArtist;
    private SetlistsByArtists setlistsByArtists;
    private String mbid;
    private Context context;
    ArrayList<SetInfo> setInfo;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.songlist);
        btnChooseArtist = (Button) findViewById(R.id.btn_choose_artist);
        resultsMsg = (TextView) findViewById(R.id.results_detail);
        songString = (TextInputLayout) findViewById(R.id.songSearch);
        searchButton = (Button) findViewById(R.id.searchButton);
        final Context context = this;
        setInfo = new ArrayList<>();

        EventBus.register(this);
        EventBus.register(new SetlistService(this.getApplication()));

        Intent intent = getIntent();
        String artistName = intent.getStringExtra("artistName");
        mbid = intent.getStringExtra("mbid");
        if(mbid!=null){
            getSetlistsFromBus(mbid, page);
        }
        else{
            /* Testing */
            artistName = "Radiohead";
            mbid = "a74b1b7f-71a5-4011-9441-d0b5e4122711";
            getSetlistsFromBus(mbid, page);
        }
        page++;

        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(new SetlistTypeAdapterFactory());
        Gson gson = gsonBuilder.create();
        InputStream is = this.getResources().openRawResource(R.raw.ofmontreal);
        Reader reader = new InputStreamReader(is);
        SetlistsByArtists localSetlistsByArtists = gson.fromJson(reader, SetlistsByArtists.class);


        if(artistName!=null) {
            btnChooseArtist.setText(artistName);
        }
        else{
            btnChooseArtist.setText("Choose an artist");
        }
        btnChooseArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArtist();
            }
        });



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(setlistsByArtists!=null) {
                    //doSearch(songString.getEditText().getText().toString(), setlistsByArtists);
                }
                else{
                    //Toast.makeText(context,"No results found for this artist",Toast.LENGTH_SHORT).show();
                }

                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }

        });
    }

    public void doSearch(String songString, ArrayList<SetInfo> setInfoToSearch) {
        /*
        for (int i = 0; i < sl.getSets().getSet().size(); i++) {
            set = sl.getSets().getSet().get(i);
            if(set.getSong()!=null) {
                for (int k = 0; k < set.getSong().size(); k++) {
                    song = set.getSong().get(k).getName().toLowerCase();
                    songs.add(set.getSong().get(k).getName());
                    if (songName.toLowerCase().equals(song) && !song.equals("")) {
                        instanceCounter++;
                        si.setWasPlayed(true);
                    }
                    //Need to add song to set songs array
                }
            }
        }
       */
    }

    public void searchArtist(){
        Intent intent = new Intent(this, ArtistSearchActivity.class);
        startActivity(intent);
    }

    public void getSetlistsFromBus(String mbid, int page){
        EventBus.post(new DoRestEvent(mbid, null, page));
    }
    @Subscribe
    public void onReceiveSetlists(LoadSetlistsEvent event){
        ArrayList<SetInfo> newSetInfo = buildSetInfo(event.getSetlists());
        addSetsToAdapter(newSetInfo);
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

    public void addSetsToAdapter(ArrayList<SetInfo> newSetInfo){
        if(slAdapter != null && slAdapter.getItemCount()>0){
            //setInfo.addAll(newSetInfo);
            slAdapter.notifyDataSetChanged();
        }
        else{
            setInfo = newSetInfo;
            newSetInfo=null;
            slAdapter = new SetAdapter(setInfo, this);
            layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setAdapter(slAdapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {
                    if(dy > 0) //check for scroll down
                    {
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                        if (loading)
                        {
                            if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                            {
                                loading = false;
                                Log.v("...", "Last Item Wow !");
                                getSetlistsFromBus(mbid, page);
                                page++;
                            }
                        }
                    }
                }
            });
        }
    }

}

