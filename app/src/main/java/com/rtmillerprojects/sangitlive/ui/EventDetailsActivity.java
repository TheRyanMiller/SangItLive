package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.api.SongListAdapter;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.SetInfo;

import org.parceler.Parcels;

/**
 * Created by Ryan on 9/5/2016.
 */
public class EventDetailsActivity extends AppCompatActivity {

    LinearLayoutManager layoutManager;
    TextView date;
    TextView venue;
    TextView city;
    TextView ticketLink;
    TextView title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        final Context context = this;

        Intent intent = getIntent();
        BandsInTownEventResult event = Parcels.unwrap(intent.getParcelableExtra("event"));

        title = (TextView) findViewById(R.id.title);
        title.setText(event.getTitle().toString());
        date = (TextView) findViewById(R.id.date);
        date.setText(event.getDatetime().toString());
        venue = (TextView) findViewById(R.id.venue);
        venue.setText(event.getVenue().getPlace().toString());
        city = (TextView) findViewById(R.id.city);
        city.setText(event.getVenue().getCity().toString());
        ticketLink = (TextView) findViewById(R.id.ticket_link);
        String ticketStatus;
        if(event.getTicketStatus().toString().toLowerCase().equals("available")){
            ticketStatus= "Tickets are available ";
        }
        else{
            ticketStatus= "Tickets are sold out ";
        }
        String hyperlink = ticketStatus+" <a href='"+event.getTicketUrl()+"'>Click Here!</a>";
        ticketLink.setText(Html.fromHtml(hyperlink));
    }
}
