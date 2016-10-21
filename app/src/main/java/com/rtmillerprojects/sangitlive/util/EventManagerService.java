package com.rtmillerprojects.sangitlive.util;

import android.content.Context;

import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.model.EventCalls.BITResultPackageEventMgr;
import com.rtmillerprojects.sangitlive.model.ArtistDetails;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.EventCalls.CompletedForceRefresh;
import com.rtmillerprojects.sangitlive.model.EventCalls.EventManagerRequest;
import com.rtmillerprojects.sangitlive.model.EventCalls.NameMbidPair;
import com.rtmillerprojects.sangitlive.model.NotifyNewEventsForArtist;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by Ryan on 10/1/2016.
 */
public class EventManagerService {
    private DatabaseHelper db;
    private static EventManagerService emsInstance;
    private int dbVersion;
    private int numOfCallsMade = 0;
    private int numOfCallsReceived = 0;
    private Context context;
    private ArrayList<BITResultPackageEventMgr> eventsList = new ArrayList<>();

    //Singleton
    public static EventManagerService getInstance(Context context) {
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
        this.context = context;
    }


    public void getSingleArtistEventsAll(NameMbidPair nmPair) {
        ArrayList<NameMbidPair> pairs = new ArrayList<>();
        pairs.add(nmPair);
        //Request All Events for this artist
        EventBus.post(new EventManagerRequest(pairs, false, false,false));
        //Need new function to request only local events
    }


    @Subscribe
    public void receiveArtistEventsAll(BITResultPackageEventMgr result) {
        numOfCallsReceived++;
        if (result.events != null) {
            if (result.isForceRefresh) {
                eventsList.add(result);
                if (numOfCallsReceived == numOfCallsMade) {
                    reconcileTheChanges(eventsList);
                }
            } else {
                numOfCallsReceived = 0;
                numOfCallsMade = 0;
                db = DatabaseHelper.getInstance(context);
                if(result.isFilteredByLocation){
                    db.deleteEventsLocalByArtist(result.pair.getMbid(), result.pair.getArtistName());
                    db.insertEventsLocal(result.events);
                }
                else{
                    db.deleteEventsAllByArtist(result.pair.getMbid(), result.pair.getArtistName());
                    db.insertEventsAll(result.events);
                }
                //Insert into DB

            }
        } else {
            //Handle situation where REST call failed

        }
    }

    public void forceRefreshCalls() {
        ArrayList<NameMbidPair> pairs = new ArrayList<>();
        NameMbidPair nmPair;
        db = DatabaseHelper.getInstance(context);
        ArrayList<ArtistDetails> artistList = db.getAllArtists();
        for (ArtistDetails a : artistList) {
            nmPair = new NameMbidPair(a.getName(), a.getMbid());
            pairs.add(nmPair);
        }
        //Request All Events for this artist
        numOfCallsMade = artistList.size();
        EventBus.post(new EventManagerRequest(pairs, false, true,true));
        EventBus.post(new EventManagerRequest(pairs, false, true,false));
        //Need new function to request only local events
    }

    private void reconcileTheChanges(ArrayList<BITResultPackageEventMgr> eventsList) {
        //This is the method called when all the callbacks are returned and the updates are ready to be made
        //Compare objects to what is in the database
        //Need to repeat this for all local shows
        numOfCallsReceived = 0;
        numOfCallsMade = 0;
        ArrayList<NotifyNewEventsForArtist> results = new ArrayList<>();
        for (BITResultPackageEventMgr r : eventsList) {
            if(r.events != null && r.pair != null) {
                results.add(identifyIfNewDatesExist(r.events, r.pair));

            }
        }
        //Reconcile process is complete
        //Now rready to build CompletedForceRefresh object and post
        // it back to the event manager
        CompletedForceRefresh complete = new CompletedForceRefresh(true, false);
        complete.setNewEventsList(results);
        EventBus.post(complete);
    }

    private NotifyNewEventsForArtist identifyIfNewDatesExist(ArrayList<BandsInTownEventResult> freshEvents, NameMbidPair nmp) {
        int numAdded = 0;
        //ALL EVENTS TABLE MATCHING
        //Compare the database values and the REST returned values
        //We will say their are no changes if either of these conditions are true
        //  1. Their array sizes match
        //  2. The last event in their arrays have the same Event ID
        ArrayList<BandsInTownEventResult> dbEvents = db.getEventsAllByArtist(true, false, false, nmp.getArtistName(), nmp.getMbid(), true);
        Integer dbLastEventId = 0;
        Integer freshLastEventId = 0;
        if(dbEvents!=null && dbEvents.size()>0 && dbEvents.get(dbEvents.size()-1).getId() != null){
            dbLastEventId = dbEvents.get(dbEvents.size()-1).getId();
        }
        if(freshEvents!=null && freshEvents.size()>0 && freshEvents.get(freshEvents.size()-1).getId() != null){
            freshLastEventId = freshEvents.get(freshEvents.size()-1).getId();
        }

        if (dbEvents.size() == freshEvents.size() || freshLastEventId == dbLastEventId) {
            //Match was found. Delete all db events and add the new.
            db.deleteEventsAllByArtist(nmp.getArtistName(), nmp.getMbid());
            db.insertEventsAll(freshEvents);
        } else if (dbEvents.size() < freshEvents.size()) {
            //More events found in the REST response
            //We will assume that this indicates new events have been announced
            numAdded = freshEvents.size() - dbEvents.size();
            int numAddedLocal = 0;
            NotifyNewEventsForArtist ne = new NotifyNewEventsForArtist(nmp.getArtistName(), nmp.getMbid(),numAdded);
            db.deleteEventsAllByArtist(nmp.getArtistName(), nmp.getMbid());
            db.insertEventsAll(freshEvents);
            return ne;
        } else {
            //db events is greater.
            //Not quite sure what the explaination would be, but for now
            //get rid of old events from db and replace with the new
            db.deleteEventsAllByArtist(nmp.getArtistName(), nmp.getMbid());
            db.insertEventsAll(freshEvents);
        }
        return null;

    }
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

