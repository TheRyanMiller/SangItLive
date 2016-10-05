package com.rtmillerprojects.sangitlive.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.model.GoogleLocation.CustomLocationResult;
import com.rtmillerprojects.sangitlive.model.GoogleLocation.Result;
import com.rtmillerprojects.sangitlive.model.EventCalls.CompletedForceRefresh;
import com.rtmillerprojects.sangitlive.util.EventManagerService;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by Ryan on 8/25/2016.
 */
public class ActivitySettings extends AppCompatActivity {

    RelativeLayout locationLayout;
    RelativeLayout forceRefresh;
    EventManagerService ems;
    RecyclerView.LayoutManager layoutManager;
    ListView listView;
    Toolbar toolbar;

    TextView emptyView;
    ArrayAdapter<Result> mAdapter;
    List<Result> r;
    String zipReturn;
    private final int ZIP_REQUEST_CODE = 1;
    CustomLocationResult location;
    TextView textViewLocation;
    ProgressDialog progressDialog;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        handleIntent(getIntent());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        locationLayout = (RelativeLayout) findViewById(R.id.location_layout);
        forceRefresh = (RelativeLayout) findViewById(R.id.force_refresh_layout);
        textViewLocation = (TextView) findViewById(R.id.location_value);
        listView = (ListView) findViewById(R.id.location_results);
        emptyView = (TextView) findViewById(R.id.empty_view);
        final Context context = this;

        forceRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ems = EventManagerService.getInstance(context);
                ems.forceRefreshCalls();
                progressDialog = new ProgressDialog(context);
                progressDialog.show();
            }
        });

        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, ActivityZipSearch.class), ZIP_REQUEST_CODE);
            }
        });


    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.m 23qv

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Toast.makeText(this,query,Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void getResponseFromForceRefresh(CompletedForceRefresh response){
        progressDialog.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                location = Parcels.unwrap(data.getParcelableExtra("result"));
                textViewLocation.setText(location.toString());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.unregister(this);
    }

}
//Toast.makeText(this,"ARTIST IS RETURNED",Toast.LENGTH_SHORT).show();