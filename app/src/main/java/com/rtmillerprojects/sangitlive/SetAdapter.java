package com.rtmillerprojects.sangitlive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Ryan on 8/25/2016.
 */
public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SongInstanceViewHolder> {

    private ArrayList<SetInfo> setInstances;
    private Context context;

    //Constructor
    public SetAdapter(ArrayList<SetInfo> setInstances, Context context){
        this.setInstances = setInstances;
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
        SetInfo si = setInstances.get(position);
        holder.view.setBackgroundColor(0xFFFF8A80);
        if(si.isWasPlayed()){
            holder.view.setBackgroundColor(0xFFD8FFA9);
        }
        holder.resultNumber.setText(++position +"");
        holder.name.setText(si.getSongName());
        holder.city.setText(si.getCity());
        holder.date.setText(si.getDate());
        holder.venue.setText(si.getVenue());
    }

    @Override
    public int getItemCount() {
        return setInstances.size();
    }


    public static class SongInstanceViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView city;
        TextView date;
        TextView venue;
        TextView resultNumber;
        ImageView profPic;
        View view;

        public SongInstanceViewHolder(View v) {
            super(v);
            city = (TextView) v.findViewById(R.id.city);
            date = (TextView) v.findViewById(R.id.date);
            name = (TextView) v.findViewById(R.id.songname);
            venue = (TextView) v.findViewById(R.id.venue);
            resultNumber = (TextView) v.findViewById(R.id.resultnumber);
            view = v;
        }
    }
}
