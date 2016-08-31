package com.rtmillerprojects.sangitlive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Ryan on 8/31/2016.
 */
public class SetlistDetailActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setlistdetail);
        recyclerView = (RecyclerView) findViewById(R.id.songlist);
        final Context context = this;

        Intent intent = getIntent();
        SetInfo setinfo = intent.getParcelableExtra("setinfo");
        String abc = setinfo.getVenue();



    }
}
