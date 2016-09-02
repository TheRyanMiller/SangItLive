package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.api.SongListAdapter;
import com.rtmillerprojects.sangitlive.model.SetInfo;

/**
 * Created by Ryan on 8/31/2016.
 */
public class SetlistDetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    TextView date;
    TextView venue;
    TextView city;
    TextView country;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setlistdetail);
        final Context context = this;

        Intent intent = getIntent();
        SetInfo setinfo = intent.getParcelableExtra("setinfo");

        date = (TextView) findViewById(R.id.date);
        date.setText(setinfo.getDate().toString());
        venue = (TextView) findViewById(R.id.venue);
        venue.setText(setinfo.getVenue().toString());
        city = (TextView) findViewById(R.id.city);
        city.setText(setinfo.getCity().toString());
        recyclerView = (RecyclerView) findViewById(R.id.listofsongs);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        SongListAdapter sla = new SongListAdapter(setinfo.getSongs(), this);
        recyclerView.setAdapter(sla);
        recyclerView.setLayoutManager(layoutManager);

    }
}
