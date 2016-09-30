package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.adapter.MusicBrainzArtistsAdapter;
import com.rtmillerprojects.sangitlive.api.MusicBrainzArtistService;
import com.rtmillerprojects.sangitlive.api.ServiceLocationGoogle;
import com.rtmillerprojects.sangitlive.api.ServiceSetlist;
import com.rtmillerprojects.sangitlive.model.EventCalls.GoogleLocationRequest;
import com.rtmillerprojects.sangitlive.model.GoogleLocation.LocationResults;
import com.rtmillerprojects.sangitlive.model.GoogleLocation.Result;
import com.rtmillerprojects.sangitlive.model.PostArtistSearch;
import com.rtmillerprojects.sangitlive.model.musicbrainzaritstbrowse.Artist;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 8/25/2016.
 */
public class ActivitySettings extends AppCompatActivity {

    EditText searchString;
    Button btnSearch;
    RecyclerView.LayoutManager layoutManager;
    ListView listView;
    Bus mBus;
    ServiceLocationGoogle slg;
    ServiceSetlist serviceSetlist;
    Toolbar toolbar;
    TextView emptyView;
    ArrayAdapter<Result> mAdapter;
    List<Result> r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchString = (EditText) findViewById(R.id.zip_string);
        btnSearch = (Button) findViewById(R.id.btn_search);
        listView = (ListView) findViewById(R.id.location_results);
        emptyView = (TextView) findViewById(R.id.empty_view);
        final Context context = this;

        toolbar.setTitle("Artist Search");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                if(searchString.getText().toString().equals("")){
                    Toast.makeText(context,"Please enter a search value",Toast.LENGTH_SHORT).show();
                }
                else {
                    r = null;
                    EventBus.post(new GoogleLocationRequest(searchString.getText().toString()));
                }
            }
        });

    }

    @Subscribe
    public void receiveArtistResults(LocationResults results) {
        r = results.getResults();
        if(r==null || r.size()==0){
            listView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            mAdapter = new ArrayAdapter<Result>(this, android.R.layout.simple_list_item_1,r);
            listView.setAdapter(mAdapter);
            listView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.register(this);
        if(slg==null){slg = new ServiceLocationGoogle(this.getApplication());}
        EventBus.register(slg);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.unregister(this);
        EventBus.unregister(slg);
    }
}
//Toast.makeText(this,"ARTIST IS RETURNED",Toast.LENGTH_SHORT).show();