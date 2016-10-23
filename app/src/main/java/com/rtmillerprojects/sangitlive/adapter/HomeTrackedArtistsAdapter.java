package com.rtmillerprojects.sangitlive.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rtmillerprojects.sangitlive.R;
import com.rtmillerprojects.sangitlive.model.ArtistDetails;
import com.rtmillerprojects.sangitlive.model.lastfmartistsearch.ArtistLastFm;
import com.rtmillerprojects.sangitlive.ui.ActivityArtistDetail;
import com.rtmillerprojects.sangitlive.util.CircleTransform;
import com.rtmillerprojects.sangitlive.util.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 9/3/2016.
 */
public class HomeTrackedArtistsAdapter extends RecyclerView.Adapter<HomeTrackedArtistsAdapter.HomeTrackedArtistViewHolder> implements Callback {

    private ArrayList<ArtistDetails> artists;
    private Context context;
    private List<Long> trackedArtists;

    //Constructor
    public HomeTrackedArtistsAdapter(ArrayList<ArtistDetails> artists, Context context){
        this.artists = artists;
        this.context = context;
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        //trackedArtists = db.getAllArtistIds();

    };

    @Override
    public HomeTrackedArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_list_item, parent, false);
        HomeTrackedArtistViewHolder vh = new HomeTrackedArtistViewHolder(v,context,this);

        return vh;
    }

    @Override
    public void onBindViewHolder(HomeTrackedArtistsAdapter.HomeTrackedArtistViewHolder holder, int position) {
        ArtistDetails artistDetails = artists.get(position);
        holder.artistDetails = artistDetails;
        holder.resultNumber = position;
        //use this if check for testing and then remove
        if(artistDetails!=null){
            holder.artistMbid = artistDetails.getMbid();
            holder.artistName.setText(artistDetails.getName());
        }
        holder.position = position;

        try {
            Picasso.with(context).load(artistDetails.getImageText())
                    .placeholder(R.drawable.ic_person_grey600_24dp)
                    .transform(new CircleTransform())
                    .into(holder.imgView);
        }
        catch (IndexOutOfBoundsException e) {
            Log.e(e.getClass().getName(),e.getMessage());
        }
        //holder.imgView = imageview;


    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    @Override
    public ArtistDetails update(int position) {
        return artists.get(position);

    }


    public static class HomeTrackedArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ArtistDetails artistDetails;
        TextView artistName;
        public String artistMbid;
        public String artistNameString;
        int position;
        int resultNumber;
        public ImageView imgView;
        LinearLayout artistListItem;
        AppCompatActivity ACA;
        View view;

        public HomeTrackedArtistViewHolder(View v, final Context context, final Callback callback) {
            super(v);
            artistName = (TextView) v.findViewById(R.id.artist_name);
            imgView = (ImageView) v.findViewById(R.id.artist_img);
            this.ACA = (AppCompatActivity) context;
            v.setOnClickListener(this);
            view = v;
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final ArtistDetails adc = callback.update(position);
                    new AlertDialog.Builder(ACA)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Confirm selection")
                            .setMessage("Remove this artist?")
                            .setPositiveButton("Remove", new DialogInterface.OnClickListener() {


                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseHelper db = DatabaseHelper.getInstance(context);
                                    db.deleteArtist(artistMbid);
                                    db.deleteEventsAllByArtist(adc.getMbid(), adc.getName());
                                    db.deleteEventsLocalByArtist(adc.getMbid(), adc.getName());
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                    return false;
                }
            });
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = new Intent(ACA, ActivityArtistDetail.class);
            intent.putExtra("mbid",artistDetails.getMbid());
            intent.putExtra("artistName",artistName.getText().toString());
            ACA.startActivity(intent);
        }

    }
}
