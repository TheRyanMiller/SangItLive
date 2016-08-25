package com.rtmillerprojects.sangitlive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Ryan on 8/25/2016.
 */
public class SongInstanceAdapter extends RecyclerView.Adapter<SongInstanceAdapter.SongInstanceViewHolder> {

    private ArrayList<SongPlayedInfo> songInstances;
    private Context context;

    //Constructor
    public SongInstanceAdapter (ArrayList<SongPlayedInfo> songInstances, Context context){
        this.songInstances = songInstances;
        this.context = context;
    };

    @Override
    public SongInstanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_list_item, parent, false);
        SongInstanceViewHolder vh = new SongInstanceViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(SongInstanceViewHolder holder, int position) {
        SongPlayedInfo spi = songInstances.get(position);
        holder.resultNumber.setText(++position +"");
        holder.name.setText(spi.getSongName());
        holder.city.setText(spi.getCity());
        holder.date.setText(spi.getDate());
        holder.venue.setText(spi.getVenue());
    }

    @Override
    public int getItemCount() {
        return songInstances.size();
    }


    public static class SongInstanceViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView city;
        TextView date;
        TextView venue;
        TextView resultNumber;
        ImageView profPic;

        public SongInstanceViewHolder(View v) {
            super(v);
            city = (TextView) v.findViewById(R.id.city);
            date = (TextView) v.findViewById(R.id.date);
            name = (TextView) v.findViewById(R.id.songname);
            venue = (TextView) v.findViewById(R.id.venue);
            resultNumber = (TextView) v.findViewById(R.id.resultnumber);
        }
    }
}
