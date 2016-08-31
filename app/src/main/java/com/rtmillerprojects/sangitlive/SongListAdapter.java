package com.rtmillerprojects.sangitlive;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ryan on 8/31/2016.
 */
public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder>{

    private ArrayList<String> songs;
    private Context context;

    public SongListAdapter(ArrayList<String> songs, Context context){
        this.songs = songs;
        this.context = context;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);
        SongViewHolder vh = new SongViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        String song = songs.get(position);
        //holder.view.setBackgroundColor(0xFFFF8A80);
        holder.songText.setText(song);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder{

        String song;
        TextView songText;

        public SongViewHolder(View itemView) {
            super(itemView);
            songText = (TextView) itemView.findViewById(R.id.songname);
        }
    }


}
