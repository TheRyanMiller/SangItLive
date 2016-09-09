package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.ArtistsAdapter;
import com.rtmillerprojects.sangitlive.adapter.MusicBrainzArtistsAdapter;
import com.rtmillerprojects.sangitlive.api.MusicBrainzArtistService;
import com.rtmillerprojects.sangitlive.model.PostArtistSearch;
import com.rtmillerprojects.sangitlive.api.ServiceSetlist;
import com.rtmillerprojects.sangitlive.model.ArtistDetails;
import com.rtmillerprojects.sangitlive.model.ArtistResults;
import com.rtmillerprojects.sangitlive.model.LoadArtistEvent;
import com.rtmillerprojects.sangitlive.model.musicbrainzaritstbrowse.Artist;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by Ryan on 8/25/2016.
 */
public class ArtistSearchActivity extends AppCompatActivity {

    EditText searchString;
    Button btnSearch;
    MusicBrainzArtistsAdapter artistAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    Bus mBus;
    MusicBrainzArtistService mbas;
    ServiceSetlist serviceSetlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.artist_search);

        searchString = (EditText) findViewById(R.id.artist_string);

        btnSearch = (Button) findViewById(R.id.btn_search);
        recyclerView = (RecyclerView) findViewById(R.id.artist_results);
        final Context context = this;

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                getArtists(searchString.getText().toString());
            }
        });

    }

    @Subscribe
    public void receiveArtistResults(ArrayList<Artist> results) {
        Toast.makeText(this,"ARTIST IS RETURNED",Toast.LENGTH_SHORT).show();
        if(results==null){
            //do something if null
            artistAdapter = new MusicBrainzArtistsAdapter(null, this);
            recyclerView.setAdapter(artistAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else {
            ArrayList<Artist>/*BUILD THIS FOR MUSICBRAINZ ARTIST TYPE*/ adList = new ArrayList<>();
            artistAdapter = new MusicBrainzArtistsAdapter(results, this);
            recyclerView.setAdapter(artistAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void getArtists(String artistSearchString){
        EventBus.post(new PostArtistSearch(null, artistSearchString));
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.register(this);
        if(mbas==null){mbas = new MusicBrainzArtistService(this.getApplication());}
        EventBus.register(mbas);

    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.unregister(this);
        EventBus.unregister(mbas);
    }
}
//Toast.makeText(this,"ARTIST IS RETURNED",Toast.LENGTH_SHORT).show();