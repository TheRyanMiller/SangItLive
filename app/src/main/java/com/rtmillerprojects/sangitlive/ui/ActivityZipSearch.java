package com.rtmillerprojects.sangitlive.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.api.ServiceLocationGoogle;
import com.rtmillerprojects.sangitlive.api.ServiceSetlist;
import com.rtmillerprojects.sangitlive.model.EventCalls.GoogleLocationRequest;
import com.rtmillerprojects.sangitlive.model.GoogleLocation.AddressComponent;
import com.rtmillerprojects.sangitlive.model.GoogleLocation.CustomLocationResult;
import com.rtmillerprojects.sangitlive.model.GoogleLocation.LocationResults;
import com.rtmillerprojects.sangitlive.model.GoogleLocation.Result;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 8/25/2016.
 */
public class ActivityZipSearch extends AppCompatActivity {

    EditText searchString;
    Button btnSearch;
    RecyclerView.LayoutManager layoutManager;
    ListView listView;
    Bus mBus;
    ServiceLocationGoogle slg;
    ServiceSetlist serviceSetlist;
    Toolbar toolbar;
    TextView emptyView;
    ArrayAdapter<CustomLocationResult> mAdapter;
    List<Result> r;
    ArrayList<CustomLocationResult> cLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_search);
        handleIntent(getIntent());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.location_results);
        emptyView = (TextView) findViewById(R.id.empty_view);
        final Context context = this;

        toolbar.setTitle("Zip Search");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.m 23qv

        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);

        return true;
    }

    @Subscribe
    public void receiveArtistResults(LocationResults results) {
        r = results.getResults();
        if(r==null || r.size()==0){
            listView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            cLoc = new ArrayList<>();
            for(Result x : r){
                CustomLocationResult c = new CustomLocationResult();
                if(x.getFormattedAddress()!=null) {
                    c.formattedAddress=x.getFormattedAddress();
                }
                if(x.getAddressComponents()!=null && x.getAddressComponents().size()>0) {
                    List<AddressComponent> ac = x.getAddressComponents();
                    for (int i = 0; i < ac.size(); i++) {
                        if(ac.get(i).getTypes()!=null && ac.get(i).getTypes().size()>0){
                            if(ac.get(i).getTypes().get(0).contains("country")){
                                c.country = ac.get(i).getLongName();
                                c.countryAbv = ac.get(i).getShortName();
                            }
                            if(ac.get(i).getTypes().get(0).contains("locality")){
                                c.city = ac.get(i).getLongName();
                                c.cityAbv = ac.get(i).getShortName();
                            }
                            if(ac.get(i).getTypes().get(0).contains("administrative_area_level_1")){
                                c.state = ac.get(i).getLongName();
                                c.stateAbv = ac.get(i).getShortName();
                            }
                            if(ac.get(i).getTypes().get(0).contains("administrative_area_level_2")){
                                c.county = ac.get(i).getLongName();
                                c.countyAbv = ac.get(i).getShortName();
                            }
                            if(ac.get(i).getTypes().get(0).contains("postal_code")){
                                c.zip = ac.get(i).getLongName();
                            }
                        }
                    }
                    c.formattedAddress=x.getFormattedAddress();
                }
                cLoc.add(c);
            }
            if(cLoc!=null && cLoc.size()>0){
                mAdapter = new ArrayAdapter<CustomLocationResult>(this, android.R.layout.simple_list_item_1,cLoc);
                listView.setAdapter(mAdapter);
                listView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CustomLocationResult item = mAdapter.getItem(position);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",Parcels.wrap(item));
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });
            }
            else{

            }
        }

    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String zipString = intent.getStringExtra(SearchManager.QUERY);
            //use the zipString from the intent to search for zips
            Toast.makeText(this,zipString,Toast.LENGTH_SHORT).show();
            if(zipString.equals("")){
                Toast.makeText(this,"Please enter a search value",Toast.LENGTH_SHORT).show();
            }
            else {
                r = null;
                EventBus.post(new GoogleLocationRequest(zipString));
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.register(this);
        if(slg==null){slg = new ServiceLocationGoogle(this.getApplication());}
        EventBus.register(slg);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.unregister(this);
        EventBus.unregister(slg);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
//Toast.makeText(this,"ARTIST IS RETURNED",Toast.LENGTH_SHORT).show();