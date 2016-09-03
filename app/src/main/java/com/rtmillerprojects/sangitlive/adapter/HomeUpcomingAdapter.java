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
import com.rtmillerprojects.sangitlive.model.SetInfo;
import com.rtmillerprojects.sangitlive.ui.SetlistDetailActivity;

import java.util.ArrayList;

/**
 * Created by Ryan on 9/3/2016.
 */
public class HomeUpcomingAdapter extends RecyclerView.Adapter<HomeUpcomingAdapter.HomeUpcomingViewHolder> {

    private ArrayList<BandsInTownEventResult> events;
    private Context context;

    //Constructor
    public HomeUpcomingAdapter(ArrayList<BandsInTownEventResult> events, Context context){
        this.events = events;
        this.context = context;

    };

    @Override
    public HomeUpcomingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        HomeUpcomingViewHolder vh = new HomeUpcomingViewHolder(v,context);

        return vh;
    }

    @Override
    public void onBindViewHolder(HomeUpcomingAdapter.HomeUpcomingViewHolder holder, int position) {
        BandsInTownEventResult event = events.get(position);
        //holder.view.setBackgroundColor(0xFFFF8A80);

        holder.resultNumber.setText(++position +"");
        holder.title.setText(event.getTitle());
        holder.location.setText(event.getFormattedLocation());
        holder.date.setText(event.getDatetime());
        holder.venue.setText(event.getVenue().getName());
        holder.position = position;
        holder.event = event;
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


    public static class HomeUpcomingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        BandsInTownEventResult event;
        TextView title;
        TextView location;
        TextView date;
        TextView venue;
        int position;
        TextView resultNumber;
        public ImageView imgView;
        LinearLayout setListItem;
        AppCompatActivity ACA;
        View view;

        public HomeUpcomingViewHolder(View v, Context context) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            location = (TextView) v.findViewById(R.id.location);
            date = (TextView) v.findViewById(R.id.date);
            venue = (TextView) v.findViewById(R.id.venue);
            resultNumber = (TextView) v.findViewById(R.id.resultnumber);
            setListItem = (LinearLayout) v.findViewById(R.id.event_item);
            imgView = (ImageView) v.findViewById(R.id.indicator);
            this.ACA = (AppCompatActivity) context;
            v.setOnClickListener(this);
            view = v;
        }



        @Override
        public void onClick(View v) {
            /*
            int position = getAdapterPosition();
            Intent intent = new Intent(ACA, SetlistDetailActivity.class);
            intent.putExtra("setinfo",setInfo);
            ACA.startActivity(intent);
            */
        }

    }
}
