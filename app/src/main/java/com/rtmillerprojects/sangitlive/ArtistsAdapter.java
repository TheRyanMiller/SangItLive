package com.rtmillerprojects.sangitlive;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ryan on 8/25/2016.
 */
public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder> {

    private ArrayList<ArtistDetails> artistDetails;
    private Context context;

    //Constructor
    public ArtistsAdapter (ArrayList<ArtistDetails> artistDetails, Context context){
        this.artistDetails = artistDetails;
        this.context = context;
    };

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_list_item, parent, false);
        ArtistViewHolder vh = new ArtistViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        ArtistDetails aDetails = artistDetails.get(position);
        holder.resultNumber.setText(++position +"");
        holder.artistName.setText(aDetails.getName());
        holder.mbid.setText(aDetails.getMbid());
    }

    @Override
    public int getItemCount() {
        return artistDetails.size();
    }


    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        TextView mbid;
        TextView resultNumber;

        public ArtistViewHolder(View v) {
            super(v);
            mbid = (TextView) v.findViewById(R.id.mbid);
            artistName = (TextView) v.findViewById(R.id.artist_name);
            resultNumber = (TextView) v.findViewById(R.id.resultnumber);
        }
    }
}