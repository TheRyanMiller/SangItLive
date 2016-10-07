package com.rtmillerprojects.sangitlive.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.model.ArtistDetails;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.EventCalls.BITResultPackage;
import com.rtmillerprojects.sangitlive.model.EventCalls.NameMbidPair;
import com.rtmillerprojects.sangitlive.model.EventCalls.UpcomingEventQuery;
import com.rtmillerprojects.sangitlive.util.DatabaseHelper;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Ryan on 9/5/2016.
 */
public class ActivityEventDetails extends AppCompatActivity {

    LinearLayoutManager layoutManager;
    TextView date;
    TextView venue;
    TextView city;
    TextView ticketLink;
    TextView title;
    CheckBox attending;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        final Context context = this;

        Intent intent = getIntent();
        final BandsInTownEventResult event = Parcels.unwrap(intent.getParcelableExtra("event"));

        final ArtistDetails ad = new ArtistDetails();
        ad.setMbid(event.getArtists().get(0).getMbid());
        ad.setName(event.getArtists().get(0).getName());

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

        if(event.getTicketStatus()!=null && event.getTicketStatus().toString().toLowerCase().equals("available")){
            ticketStatus= "Tickets are available ";
        }
        else{
            ticketStatus= "Tickets are sold out ";
        }
        String hyperlink = ticketStatus+" <a href='"+event.getTicketUrl()+"'>Click Here!</a>";
        ticketLink.setText(Html.fromHtml(hyperlink));
        attending = (CheckBox) findViewById(R.id.starBox);
        attending.setChecked(event.isAttending());

        attending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attending.isChecked()==false){

                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Confirm selection")
                            .setMessage("Remove this event?")
                            .setPositiveButton("Remove", new DialogInterface.OnClickListener() {


                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    attending.setText(""); //not attending
                                    //Delete from db
                                    DatabaseHelper db = DatabaseHelper.getInstance(context);
                                    db.deleteEventAttending(event.getId());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    attending.setText("You are attending."); //am attending
                                    attending.setChecked(true);
                                }
                            })
                            .show();
                }
                else{
                    DatabaseHelper db = DatabaseHelper.getInstance(context);
                    db.insertEventAttending(event);
                    db.updateEventAllByIdMarkAsAttending(event.getId());
                    if(db.getArtistById(ad.getMbid())==null && ad.getMbid()!=null && ad.getName()!=null){
                        new AlertDialog.Builder(context)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("Add artist?")
                                .setMessage("You are not currently tracking this artist. Would you like to add them?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ArrayList<NameMbidPair> pairs = new ArrayList<NameMbidPair>();
                                        pairs.add(new NameMbidPair(ad.getName(),ad.getMbid()));
                                        EventBus.post(new UpcomingEventQuery(pairs,0,false));
                                        DatabaseHelper db = DatabaseHelper.getInstance(context);
                                        db.insertArtist(ad);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .show();
                    }

                }

            }
        });
    }

    @Subscribe
    public void recieveShows(BITResultPackage showResults){
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        db.insertEventsAll(showResults.events);
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
