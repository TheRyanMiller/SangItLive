package com.rtmillerprojects.sangitlive.listener;

import com.rtmillerprojects.sangitlive.model.ArtistDetails;

/**
 * Created by Ryan on 10/22/2016.
 */
public interface Callback {
    public ArtistDetails update(int position);
}
