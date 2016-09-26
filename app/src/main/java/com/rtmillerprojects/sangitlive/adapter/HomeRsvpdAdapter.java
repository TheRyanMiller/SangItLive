package com.rtmillerprojects.sangitlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.ui.EventDetailsActivity;
import com.rtmillerprojects.sangitlive.util.DatabaseHelper;

import org.parceler.Parcels;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 9/3/2016.
 */
public class HomeRsvpdAdapter extends RecyclerView.Adapter<HomeRsvpdAdapter.HomeRsvpViewHolder> {

    private ArrayList<BandsInTownEventResult> events;
    private Context context;
    private List<Long> favoritedShows;

    //Constructor
    public HomeRsvpdAdapter(ArrayList<BandsInTownEventResult> events, Context context){
        this.events = events;
        this.context = context;
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        favoritedShows = db.getFavoritedEventIds();

    };

    @Override
    public HomeRsvpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        HomeRsvpViewHolder vh = new HomeRsvpViewHolder(v,context);

        return vh;
    }

    @Override
    public void onBindViewHolder(HomeRsvpdAdapter.HomeRsvpViewHolder holder, int position) {
        BandsInTownEventResult event = events.get(position);
        if(event.isAttending()){
            holder.view.setBackgroundResource(R.color.material_lime_500);
        }
        else{
            holder.view.setBackgroundResource(R.color.light_grey_row_color);
        }



        holder.resultNumber = position;
        holder.artist.setText(event.getTitle());
        holder.location.setText(event.getFormattedLocation());
        Format formatter = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = formatter.format(event.getDatetime());
        //holder.date.setText(event.getDatetime().toString());
        holder.venue.setText(event.getVenue().getName());
        holder.position = position;
        holder.event = event;
        holder.attending = event.isAttending();
        /*
        if(event.getWasPlayed()){
            holder.setListItem.setBackgroundResource(R.drawable.bordergreen);
            //holder.imgView.setImageResource(R.drawable.greenlight);
        }
        else{
            holder.setListItem.setBackgroundResource(R.drawable.border);
        }
        */

    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    public static class HomeRsvpViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        BandsInTownEventResult event;
        TextView artist;
        TextView location;
        TextView dayOfMonth;
        TextView dayOfWeek;
        TextView year;
        TextView month;
        TextView venue;
        int position;
        boolean attending;
        int resultNumber;
        public ImageView imgView;
        LinearLayout setListItem;
        AppCompatActivity ACA;
        View view;

        public HomeRsvpViewHolder(View v, Context context) {
            super(v);
            dayOfMonth = (TextView) v.findViewById(R.id.day_of_month);
            dayOfWeek = (TextView) v.findViewById(R.id.day_of_week);
            month = (TextView) v.findViewById(R.id.month);
            year = (TextView) v.findViewById(R.id.year);
            artist = (TextView) v.findViewById(R.id.artist);
            location = (TextView) v.findViewById(R.id.location);
            venue = (TextView) v.findViewById(R.id.venue);
            imgView = (ImageView) v.findViewById(R.id.indicator);
            this.ACA = (AppCompatActivity) context;
            v.setOnClickListener(this);
            view = v;
        }



        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(ACA, EventDetailsActivity.class);
            intent.putExtra("event", Parcels.wrap(event));
            ACA.startActivity(intent);
        }

    }
}
