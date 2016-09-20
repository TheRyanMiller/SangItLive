package com.rtmillerprojects.sangitlive.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.api.LastFmArtistService;
import com.rtmillerprojects.sangitlive.api.ServiceUpcomingEvents;
import com.rtmillerprojects.sangitlive.listener.GetMbid;

/**
 * Created by Ryan on 9/15/2016.
 */
public class ActivityArtistDetail extends AppCompatActivity implements GetMbid {

    DrawerLayout drawer; //@Bind(R.id.drawer_layout)
    NavigationView navigationView; //@Bind(R.id.nav_view)
    Bundle bundle;
    ArtistMainFragment amf;
    private String mbid;
    String artistName;
    private LastFmArtistService lfad;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        artistName = getIntent().getExtras().getString("artistName");
        mbid = getIntent().getExtras().getString("mbid");

        bundle=new Bundle();
        bundle.putString("artistName", artistName);
        bundle.putString("mbid", mbid);
        //set Fragmentclass Arguments
        amf = new ArtistMainFragment();
        amf.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(ArtistMainFragment.class.getName()) == null) {

            fragmentManager.beginTransaction()
                    .replace(R.id.container, amf, ArtistMainFragment.class.getName())
                    .commit();
        }

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    @Override public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(lfad==null){lfad = new LastFmArtistService(this.getApplication());}
        EventBus.register(lfad);
    }
    @Override
    protected void onPause() {
        super.onPause();
        EventBus.unregister(lfad);
    }

    @Override
    public String getMbid() {
        return mbid;
    }
}
