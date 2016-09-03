package com.rtmillerprojects.sangitlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rtmillerprojects.sangitlive.ui.ArtistSetlist;
import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.model.ArtistDetails;

import java.util.ArrayList;

/**
 * Created by Ryan on 8/25/2016.
 */
public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder> {

    private ArrayList<ArtistDetails> artistDetails;
    private Context context;

    //Constructor
    public ArtistsAdapter (ArrayList<ArtistDetails> artistDetails, Context context){
        if(artistDetails==null){
            artistDetails = new ArrayList<ArtistDetails>();
        }
        this.artistDetails = artistDetails;
        this.context = context;
    };

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_list_item, parent, false);
        ArtistViewHolder vh = new ArtistViewHolder(v,context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        ArtistDetails aDetails = artistDetails.get(position);
        holder.resultNumber.setText(++position +"");
        holder.artistName.setText(aDetails.getName());
        holder.mbid = aDetails.getMbid();
    }

    @Override
    public int getItemCount() {
        return artistDetails.size();
    }


    public static class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView artistName;
        public String mbid;
        TextView resultNumber;
        AppCompatActivity ACA;

        public ArtistViewHolder(View v, Context context) {
            super(v);
            artistName = (TextView) v.findViewById(R.id.artist_name);
            resultNumber = (TextView) v.findViewById(R.id.resultnumber);
            this.ACA = (AppCompatActivity) context;
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(ACA, ArtistSetlist.class);
            intent.putExtra("mbid",mbid);
            intent.putExtra("artistName",artistName.getText().toString());
            ACA.startActivity(intent);
        }
    }

}
//Toast.makeText(ACA.getApplicationContext(),"Position: "+position,Toast.LENGTH_SHORT).show();