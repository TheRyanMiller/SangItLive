package com.rtmillerprojects.sangitlive.util;

import android.content.Context;

/**
 * Created by Ryan on 10/1/2016.
 */
public class EventManagerService {
    private DatabaseHelper db;
    private static EventManagerService emsInstance;
    private int dbVersion;

    //Singleton
    public static synchronized EventManagerService getInstance(Context context){
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (emsInstance == null) {
            emsInstance = new EventManagerService(context.getApplicationContext());
        }
        return emsInstance;
    }

    //Private Constructor
    private EventManagerService(Context context) {
        db = db.getInstance(context);
        dbVersion = db.getCurrentVersion();
    }

    /*
        This class has multiple purposes
        1. It must manage writes and removes from SQLite event table
        2. It must determine successfulness of those writes/deletes and flag them for later checking
        3. It must figure out if there is a user-location set, and make multiple calls for events
        4. It must track lastRefreshDate and nextRefreshDate, and use it to determine if more calls are needed

        Main Functions needed:
            1. globalRefresh (only used when nextRefreshDate arrives)
                -refreshes local (if necessary)
                -refreshes all
            2. getArtistEventsLocal
                -used when user switches location
            3. onLocationChange
                -run deleteLocal
            4. addNewArtist
            5. removeAritst


        Mini Functions:
            1. deleteEventsLocal(artist)
            2. deleteEventsAll(artist)
            3. getArtistEventsLocal(artists)
            4. getArtistEventsAll(artists)
            5. recordError()
            6. getAllArtistsNameMbidPairs()
     */




}
