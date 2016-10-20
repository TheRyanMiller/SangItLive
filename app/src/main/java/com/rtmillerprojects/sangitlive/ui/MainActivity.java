package com.rtmillerprojects.sangitlive.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.api.LastFmArtistService;
import com.rtmillerprojects.sangitlive.api.ServiceArtistImage;
import com.rtmillerprojects.sangitlive.api.ServiceSetlist;
import com.rtmillerprojects.sangitlive.api.ServiceUpcomingEvents;
import com.rtmillerprojects.sangitlive.listener.MainListener;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

/**
 * Created by Ryan on 9/2/2016.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainListener {

    DrawerLayout drawer; //@Bind(R.id.drawer_layout)
    NavigationView navigationView; //@Bind(R.id.nav_view)
    ServiceUpcomingEvents sue;
    ServiceArtistImage sai;
    LastFmArtistService lfas;
    Intent intent;
    MenuItem menuItem;



    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Code to initialize SQLiteStudio connection
        SQLiteStudioService.instance().start(this);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(MainFragment.class.getName()) == null) {

            fragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance(), MainFragment.class.getName())
                    .commit();
        }


        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        menuItem = menu.findItem(R.id.nav_filter);
        View actionView = MenuItemCompat.getActionView(menuItem);
        SwitchCompat filterSwitch = (SwitchCompat) actionView.findViewById(R.id.filter_switch);
        filterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "FILTER CLICKED", Toast.LENGTH_SHORT).show();
            }
        });


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



    @SuppressWarnings("StatementWithEmptyBody") @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_settings){
            startActivity(new Intent(this, ActivitySettings.class));
        }
        if(id == R.id.nav_artist_search){
            startActivity(new Intent(this, ActivityArtistSearch.class));
        }
        //implement click handlers here
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override public DrawerLayout getDrawer() {
        return drawer;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SQLiteStudioService.instance().stop();
    }
}
