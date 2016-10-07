package com.rtmillerprojects.sangitlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.model.SetInfo;
import com.rtmillerprojects.sangitlive.ui.ActivitySetlistDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ryan on 8/25/2016.
 */
public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SetInstanceViewHolder> {

    private ArrayList<SetInfo> setInstances;
    private Context context;

    //Constructor
    public SetAdapter(ArrayList<SetInfo> setInstances, Context context){
        this.setInstances = setInstances;
        this.context = context;

    };

    @Override
    public SetInstanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_list_item, parent, false);
        SetInstanceViewHolder vh = new SetInstanceViewHolder(v,context);

        return vh;
    }

    @Override
    public void onBindViewHolder(SetInstanceViewHolder holder, int position) {
        SetInfo si = setInstances.get(position);
        //holder.view.setBackgroundColor(0xFFFF8A80);
        String dateText = getDate(si.getDate());
        holder.city.setText(si.getCity());
        holder.date.setText(dateText);
        holder.venue.setText(si.getVenue());
        holder.position = position;
        holder.setInfo = si;
        if(si.getSongs()==null || si.getSongs().size()==0){
            holder.songsFound.setImageDrawable(null);
        }
        else{holder.songsFound.setImageResource(R.drawable.setlisticon);}

    }

    private String getDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat newFormat = new SimpleDateFormat(
                "MM/dd/yyyy", Locale.getDefault());
        Date newDate = new Date();
        try {
            newDate = dateFormat.parse(date);
            String desiredDate = newFormat.format(newDate);
            return desiredDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return setInstances.size();
    }


    public static class SetInstanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView city;
        TextView date;
        TextView venue;
        int position;
        ImageView songsFound;
        RelativeLayout setListItem;
        SetInfo setInfo;
        AppCompatActivity ACA;
        View view;

        public SetInstanceViewHolder(View v, Context context) {
            super(v);
            city = (TextView) v.findViewById(R.id.city);
            date = (TextView) v.findViewById(R.id.date);
            venue = (TextView) v.findViewById(R.id.venue);
            songsFound = (ImageView) v.findViewById(R.id.songs_found);
            setListItem = (RelativeLayout) v.findViewById(R.id.setlistitem);
            this.ACA = (AppCompatActivity) context;
            v.setOnClickListener(this);
            view = v;
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(ACA, ActivitySetlistDetail.class);
            intent.putExtra("setinfo",setInfo);
            ACA.startActivity(intent);
        }
    }
}
