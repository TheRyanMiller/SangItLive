package com.rtmillerprojects.sangitlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.model.musicbrainzaritstbrowse.Artist;
import com.rtmillerprojects.sangitlive.ui.ArtistSetlist;

import java.util.ArrayList;

/**
 * Created by Ryan on 8/25/2016.
 */
public class MusicBrainzArtistsAdapter extends RecyclerView.Adapter<MusicBrainzArtistsAdapter.ArtistViewHolder> {

    private ArrayList<Artist> artistDetails;
    private Context context;

    //Constructor
    public MusicBrainzArtistsAdapter(ArrayList<Artist> artistDetails, Context context){
        if(artistDetails==null){
            artistDetails = new ArrayList<Artist>();
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
        final Artist aDetails = this.artistDetails.get(position);
        holder.artistDetails = aDetails;
        holder.artistName.setText(aDetails.getName());
        holder.disambig.setText(aDetails.getDisambiguation());
    }

    @Override
    public int getItemCount() {
        return artistDetails.size();
    }


    public static class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView artistName, disambig;
        public String mbid;
        TextView resultNumber;
        Artist artistDetails;
        AppCompatActivity ACA;

        public ArtistViewHolder(View v, Context context) {
            super(v);
            artistName = (TextView) v.findViewById(R.id.artist_name);
            resultNumber = (TextView) v.findViewById(R.id.resultnumber);
            disambig = (TextView) v.findViewById(R.id.artist_disambig);
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