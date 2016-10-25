package com.rtmillerprojects.sangitlive.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.rtmillerprojects.sangitlive.util.SharedPreferencesHelper;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    ProgressDialog forceRefreshProgressDialog;
    ProgressDialog locationChangeProgressDialog;
    Context context;
    RelativeLayout refreshFrequencyLayout;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    TextView refreshFrequencyValue;
    TextView tvNextRefreshDate;
    TextView tvLastRefreshDate;
    Date lastRefresh;
    Date nextRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        handleIntent(getIntent());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        locationLayout = (RelativeLayout) findViewById(R.id.location_layout);
        forceRefresh = (RelativeLayout) findViewById(R.id.force_refresh_layout);
        refreshFrequencyLayout = (RelativeLayout) findViewById(R.id.refresh_frequency_layout);
        textViewLocation = (TextView) findViewById(R.id.location_value);
        refreshFrequencyValue = (TextView) findViewById(R.id.refresh_requency_value);
        listView = (ListView) findViewById(R.id.location_results);
        emptyView = (TextView) findViewById(R.id.empty_view);
        tvLastRefreshDate = (TextView) findViewById(R.id.last_refresh_value);
        tvNextRefreshDate = (TextView) findViewById(R.id.next_refresh_value);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        final Context context = this;
        ems = EventManagerService.getInstance(context);

        populateUserLocation();
        populateRefreshDates();

        forceRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forceRefreshProgressDialog = new ProgressDialog(context);
                forceRefreshProgressDialog.show();
                forceRefreshProgressDialog.setCancelable(false);
                forceRefreshProgressDialog.setMessage("Please wait...");
                forceRefreshProgressDialog.setTitle("Refreshing your feeds");
                ems = EventManagerService.getInstance(context);
                ems.forceRefreshCalls(false);
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

        String frequencyDisplay = sharedPref.getInt(getString(R.string.user_refresh_frequency),60) + " days";
        refreshFrequencyValue.setText(frequencyDisplay);

        refreshFrequencyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                b.setTitle("Refresh Frequency");
                String[] types = {"7 days", "21 days", "60 days"};
                b.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String frequencyDisplay;
                        int frequency;
                        long newDate;

                        dialog.dismiss();
                        switch(which){
                            case 0:
                                editor = sharedPref.edit();
                                frequency = 7;
                                editor.putInt(getString(R.string.user_refresh_frequency), frequency);
                                editor.commit();
                                frequencyDisplay = frequency + " days";
                                refreshFrequencyValue.setText(frequencyDisplay);
                                newDate = calculateNextRefreshDate(frequency);
                                tvNextRefreshDate.setText(longToString(newDate));
                                break;
                            case 1:
                                editor = sharedPref.edit();
                                frequency = 21;
                                editor.putInt(getString(R.string.user_refresh_frequency), frequency);
                                editor.commit();
                                frequencyDisplay = frequency + " days";
                                refreshFrequencyValue.setText(frequencyDisplay);
                                newDate = calculateNextRefreshDate(frequency);
                                tvNextRefreshDate.setText(longToString(newDate));
                                break;
                            case 2:
                                editor = sharedPref.edit();
                                frequency = 60;
                                editor.putInt(getString(R.string.user_refresh_frequency), frequency);
                                editor.putLong(getString(R.string.user_next_refresh), calculateNextRefreshDate(frequency));
                                editor.commit();
                                frequencyDisplay = 60 + " days";
                                refreshFrequencyValue.setText(frequencyDisplay);
                                newDate = calculateNextRefreshDate(frequency);
                                tvNextRefreshDate.setText(longToString(newDate));
                                break;
                        }
                    }

                });

                b.show();

            }
        });





    }

    private void populateRefreshDates() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int frequency = sharedPref.getInt(getString(R.string.user_refresh_frequency),0);
        nextRefresh = new Date(sharedPref.getLong(getString(R.string.user_next_refresh), calculateNextRefreshDate(frequency/*Zero is for default*/)));
        lastRefresh = new Date(sharedPref.getLong(getString(R.string.user_last_refresh), calculateLastRefreshDate()));
        tvLastRefreshDate.setText(dateToString(lastRefresh));
        tvNextRefreshDate.setText(dateToString(nextRefresh));
    }

    private long calculateLastRefreshDate() {
        Date d = new Date();
        return d.getTime();
    }

    private long calculateNextRefreshDate(int frequency) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        if(frequency==0){frequency=60;}
        c.add(Calendar.DATE, frequency); // Adding 5 days
        Date d = c.getTime();
        return d.getTime();
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

        }
    }

    private String dateToString(Date d) {
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        return formatter.format(d);
    }

    @Subscribe
    public void getResponseFromForceRefresh(CompletedForceRefresh response){
        if(forceRefreshProgressDialog!=null && forceRefreshProgressDialog.isShowing()){forceRefreshProgressDialog.cancel();}
        if(locationChangeProgressDialog!=null && locationChangeProgressDialog.isShowing()){locationChangeProgressDialog.cancel();}
        int numNewShows = 0;
        String message;
        for (int i = 0; i < response.getNewEventsList().size(); i++) {
            if(response.getNewEventsList().get(i)!=null) {
                numNewShows = numNewShows + response.getNewEventsList().get(i).getNumOfNewShows();
            }
        }

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("New events")
                .setMessage(numNewShows+ " new shows found")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

        nextRefresh = new Date(sharedPref.getLong(getString(R.string.user_next_refresh), calculateNextRefreshDate(0/*Zero is for default*/)));
        int frequency = sharedPref.getInt(getString(R.string.user_refresh_frequency), 0);
        SharedPreferencesHelper.putLong(getString(R.string.user_next_refresh), calculateNextRefreshDate(frequency));
        SharedPreferencesHelper.putLong(getString(R.string.user_last_refresh), calculateLastRefreshDate());
        tvLastRefreshDate.setText(dateToString(lastRefresh));
        tvNextRefreshDate.setText(dateToString(nextRefresh));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String oldLocation = SharedPreferencesHelper.getStringPreference(getString(R.string.user_location_zip),"default");

                location = Parcels.unwrap(data.getParcelableExtra("result"));
                boolean hasLocationChanged = !oldLocation.equals(location.getZip());
                boolean validLocation = true;//location.city != null && location.stateAbv != null;
                textViewLocation.setText(location.formattedAddress);
                //Put user location data into Shared Preferences
                SharedPreferencesHelper.putStringPreference(getString(R.string.user_location_state_abr), location.getStateAbv());
                SharedPreferencesHelper.putStringPreference(getString(R.string.user_location_state), location.getState());
                SharedPreferencesHelper.putStringPreference(getString(R.string.user_location_address_fmt), location.getFormattedAddress());
                SharedPreferencesHelper.putStringPreference(getString(R.string.user_location_country), location.getCountry());
                SharedPreferencesHelper.putStringPreference(getString(R.string.user_location_zip), location.getZip());
                SharedPreferencesHelper.putStringPreference(getString(R.string.user_city), location.city);

                if(hasLocationChanged && validLocation){
                    locationChangeProgressDialog = new ProgressDialog(this);
                    locationChangeProgressDialog.setMessage("Please wait while your local events are updated");
                    locationChangeProgressDialog.setTitle("Please Wait");
                    locationChangeProgressDialog.setCancelable(false);
                    locationChangeProgressDialog.show();

                    ems = EventManagerService.getInstance(context);
                    ems.forceRefreshCalls(true);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }


        }
    }

    private void populateUserLocation(){
        location = new CustomLocationResult();
        location.city = SharedPreferencesHelper.getStringPreference(getString(R.string.user_city), "");
        location.zip = SharedPreferencesHelper.getStringPreference(getString(R.string.user_location_zip),"");
        location.country = SharedPreferencesHelper.getStringPreference(getString(R.string.user_location_country), "");
        location.formattedAddress = SharedPreferencesHelper.getStringPreference(getString(R.string.user_location_address_fmt), "");
        location.state = SharedPreferencesHelper.getStringPreference(getString(R.string.user_location_state), "");
        location.stateAbv = SharedPreferencesHelper.getStringPreference(getString(R.string.user_location_state_abr), "");
        textViewLocation.setText(location.formattedAddress);
    }

    private String longToString(long longDate){
        Date d = new Date(longDate); //* 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        return sdf.format(d);
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