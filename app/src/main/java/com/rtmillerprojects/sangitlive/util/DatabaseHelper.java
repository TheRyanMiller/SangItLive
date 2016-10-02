package com.rtmillerprojects.sangitlive.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rtmillerprojects.sangitlive.model.ArtistDetails;
import com.rtmillerprojects.sangitlive.model.BandsInTownArtist;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.EventCalls.NameMbidPair;
import com.rtmillerprojects.sangitlive.model.Venue;
import com.rtmillerprojects.sangitlive.model.lastfmartistsearch.Artist;
import com.rtmillerprojects.sangitlive.model.lastfmartistsearch.ArtistLastFm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ryan on 9/5/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;
    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;

    //Table Defs
    public static final String DATABASE_NAME = "concertcompanion.db";
    public static final String TABLE_ARTIST = "artist";
    public static final String TABLE_EVENT_ATTENDING = "event_attending";
    public static final String TABLE_EVENT_LOCAL = "event_local";
    public static final String TABLE_EVENT_ALL = "event_all";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String ARTIST_MBID = "artist_mbid";
    private static final String KEY_CREATED_AT = "created_at";

    // Artist columns
    private static final String ARTIST_NAME = "name";

    // Event columns
    private static final String EVENT_ID = "event_id";
    private static final String EVENT_TITLE = "title";
    private static final String EVENT_FORMATTED_LOC = "fmt_loc";
    private static final String EVENT_VENUE_CITY = "city";
    private static final String EVENT_VENUE_PLACE = "place";
    private static final String EVENT_VENUE_REGION = "region";
    private static final String EVENT_VENUE_COUNTRY = "country";
    private static final String EVENT_VENUE_NAME = "venue_name";
    private static final String EVENT_DATE = "date";
    private static final String EVENT_IS_PAST = "is_past";
    private static final String EVENT_TICKET_URL = "ticket_url";
    private static final String EVENT_DESCRIPTION = "description";
    private static final String EVENT_IS_ATTENDING = "is_attending";




    private static final String CREATE_TABLE_ARTIST = "CREATE TABLE " +
            TABLE_ARTIST + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ARTIST_MBID + " TEXT," +
            ARTIST_NAME + " TEXT," +
            KEY_CREATED_AT + " DATETIME," +
            "UNIQUE("+ARTIST_MBID+"))";

    private static final String CREATE_TABLE_EVENT_ATTENDING = "CREATE TABLE " + TABLE_EVENT_ATTENDING + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            EVENT_ID + " TEXT UNIQUE," +
            EVENT_DESCRIPTION + " TEXT," +
            ARTIST_MBID + " TEXT," +
            ARTIST_NAME + " TEXT," +
            EVENT_TITLE + " TEXT," +
            EVENT_DATE + " DATETIME," +
            EVENT_FORMATTED_LOC + " TEXT," +
            EVENT_VENUE_CITY + " TEXT," +
            EVENT_VENUE_COUNTRY + " TEXT," +
            EVENT_VENUE_REGION + " TEXT," +
            EVENT_VENUE_PLACE + " TEXT," +
            EVENT_VENUE_NAME + " TEXT," +
            EVENT_TICKET_URL + " TEXT," +
            EVENT_IS_PAST + " INTEGER," +
            EVENT_IS_ATTENDING + " INTEGER," +
            KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_EVENT_LOCAL = "CREATE TABLE " + TABLE_EVENT_LOCAL + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            EVENT_ID + " TEXT UNIQUE," +
            EVENT_DESCRIPTION + " TEXT," +
            ARTIST_MBID + " TEXT," +
            ARTIST_NAME + " TEXT," +
            EVENT_TITLE + " TEXT," +
            EVENT_DATE + " DATETIME," +
            EVENT_FORMATTED_LOC + " TEXT," +
            EVENT_VENUE_CITY + " TEXT," +
            EVENT_VENUE_COUNTRY + " TEXT," +
            EVENT_VENUE_REGION + " TEXT," +
            EVENT_VENUE_PLACE + " TEXT," +
            EVENT_VENUE_NAME + " TEXT," +
            EVENT_TICKET_URL + " TEXT," +
            EVENT_IS_PAST + " INTEGER," +
            EVENT_IS_ATTENDING + " INTEGER," +
            KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_EVENT_ALL = "CREATE TABLE " + TABLE_EVENT_ALL + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            EVENT_ID + " TEXT UNIQUE," +
            EVENT_DESCRIPTION + " TEXT," +
            ARTIST_MBID + " TEXT," +
            ARTIST_NAME + " TEXT," +
            EVENT_TITLE + " TEXT," +
            EVENT_DATE + " DATETIME," +
            EVENT_FORMATTED_LOC + " TEXT," +
            EVENT_VENUE_CITY + " TEXT," +
            EVENT_VENUE_COUNTRY + " TEXT," +
            EVENT_VENUE_REGION + " TEXT," +
            EVENT_VENUE_PLACE + " TEXT," +
            EVENT_VENUE_NAME + " TEXT," +
            EVENT_TICKET_URL + " TEXT," +
            EVENT_IS_PAST + " INTEGER," +
            EVENT_IS_ATTENDING + " INTEGER," +
            KEY_CREATED_AT + " DATETIME" + ")";


    public static synchronized DatabaseHelper getInstance(Context context){
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ARTIST);
        db.execSQL(CREATE_TABLE_EVENT_ALL);
        db.execSQL(CREATE_TABLE_EVENT_ATTENDING);
        db.execSQL(CREATE_TABLE_EVENT_LOCAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_ATTENDING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_ALL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_LOCAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST);
        // create new tables
        onCreate(db);
    }

    /*
     * Creating a contact
     */
    public long insertEventAttending(BandsInTownEventResult event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_ID, event.getId());
        values.put(EVENT_TITLE, event.getTitle());
        values.put(EVENT_DESCRIPTION, event.getDescription());
        values.put(ARTIST_MBID,event.getArtists().get(0).getMbid());
        values.put(ARTIST_NAME,event.getArtists().get(0).getName());
        values.put(EVENT_DATE, event.getDatetime().toString());
        values.put(EVENT_FORMATTED_LOC, event.getFormattedLocation().toString());
        values.put(EVENT_VENUE_CITY, event.getVenue().getCity());
        values.put(EVENT_VENUE_REGION, event.getVenue().getRegion());
        values.put(EVENT_VENUE_PLACE, event.getVenue().getPlace());
        values.put(EVENT_VENUE_COUNTRY, event.getVenue().getCountry());
        values.put(EVENT_VENUE_NAME, event.getVenue().getName());
        values.put(KEY_CREATED_AT, getCurrentDateTimeAsString());
        if(event.getDatetime().before(new Date())){
            values.put(EVENT_IS_PAST, 0);
        }
        else{
            values.put(EVENT_IS_PAST, 1);
        }
        if(event.isAttending()){
            values.put(EVENT_IS_ATTENDING, 1);
        }
        else{
            values.put(EVENT_IS_ATTENDING, 0);
        }

        // insert row
        long eventId = db.insertWithOnConflict(TABLE_EVENT_ATTENDING, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return eventId;
    }

    public long insertEventLocal(BandsInTownEventResult event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_ID, event.getId());
        values.put(EVENT_TITLE, event.getTitle());
        values.put(EVENT_DESCRIPTION, event.getDescription());
        values.put(ARTIST_MBID,event.getArtists().get(0).getMbid());
        values.put(ARTIST_NAME,event.getArtists().get(0).getName());
        values.put(EVENT_DATE, event.getDatetime().toString());
        values.put(EVENT_FORMATTED_LOC, event.getFormattedLocation().toString());
        values.put(EVENT_VENUE_CITY, event.getVenue().getCity());
        values.put(EVENT_VENUE_REGION, event.getVenue().getRegion());
        values.put(EVENT_VENUE_PLACE, event.getVenue().getPlace());
        values.put(EVENT_VENUE_COUNTRY, event.getVenue().getCountry());
        values.put(EVENT_VENUE_NAME, event.getVenue().getName());
        values.put(KEY_CREATED_AT, getCurrentDateTimeAsString());
        if(event.getDatetime().before(new Date())){
            values.put(EVENT_IS_PAST, 0);
        }
        else{
            values.put(EVENT_IS_PAST, 1);
        }
        if(event.isAttending()){
            values.put(EVENT_IS_ATTENDING, 1);
        }
        else{
            values.put(EVENT_IS_ATTENDING, 0);
        }

        // insert row
        long eventId = db.insertWithOnConflict(TABLE_EVENT_LOCAL, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return eventId;
    }

    public long insertEventAll(BandsInTownEventResult event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(event.getId()!=null){values.put(EVENT_ID, event.getId());}
        if(event.getTitle()!=null){values.put(EVENT_TITLE, event.getTitle());}
        if(event.getDescription()!=null){values.put(EVENT_DESCRIPTION, event.getDescription());}
        if(event.getArtists().get(0).getMbid()!=null){values.put(ARTIST_MBID,event.getArtists().get(0).getMbid());}
        if(event.getArtists().get(0).getName()!=null){values.put(ARTIST_NAME,event.getArtists().get(0).getName());}
        if(event.getDatetime()!=null){values.put(EVENT_DATE, event.getDatetime().toString());}
        if(event.getTicketUrl()!=null){values.put(EVENT_TICKET_URL, event.getTicketUrl().toString());}
        if(event.getFormattedLocation()!=null){values.put(EVENT_FORMATTED_LOC, event.getFormattedLocation());}
        if(event.getVenue()!=null && event.getVenue().getCity()!=null){values.put(EVENT_VENUE_CITY, event.getVenue().getCity());}
        if(event.getVenue()!=null && event.getVenue().getRegion()!=null){values.put(EVENT_VENUE_REGION, event.getVenue().getRegion());}
        if(event.getVenue()!=null && event.getVenue().getPlace()!=null){values.put(EVENT_VENUE_PLACE, event.getVenue().getPlace());}
        if(event.getVenue()!=null && event.getVenue().getCountry()!=null){values.put(EVENT_VENUE_COUNTRY, event.getVenue().getCountry());}
        if(event.getVenue()!=null && event.getVenue().getName()!=null){values.put(EVENT_VENUE_NAME, event.getVenue().getName());}
        values.put(KEY_CREATED_AT, getCurrentDateTimeAsString());
        if(event.getDatetime()!=null){if(event.getDatetime().before(new Date())){
            values.put(EVENT_IS_PAST, 0);
        }}
        else{
            values.put(EVENT_IS_PAST, 1);
        }
        if(event.isAttending()){
            values.put(EVENT_IS_ATTENDING, 1);
        }
        else{
            values.put(EVENT_IS_ATTENDING, 0);
        }

        // insert row
        long eventId = db.insertWithOnConflict(TABLE_EVENT_ALL, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return eventId;
    }

    public void insertEventsAll(ArrayList<BandsInTownEventResult> eventList){
        long id;
        for(BandsInTownEventResult e : eventList){
            id = insertEventAll(e);
        }
    }

    public void insertEventsLocal(ArrayList<BandsInTownEventResult> eventList){
        long id;
        for(BandsInTownEventResult e : eventList){
            id = insertEventLocal(e);
        }
    }

    public void insertEventsAttending(ArrayList<BandsInTownEventResult> eventList){
        long id;
        for(BandsInTownEventResult e : eventList){
            id = insertEventAttending(e);
        }
    }

    public long insertArtist(ArtistDetails artistDetails){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ARTIST_MBID, artistDetails.getMbid());
        values.put(ARTIST_NAME, artistDetails.getName());
        values.put(KEY_CREATED_AT, getCurrentDateTimeAsString());
        //long artistRecordId = db.insert(TABLE_ARTIST, null, values);
        long artistRecordId = db.insertWithOnConflict(TABLE_ARTIST, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return artistRecordId;
    }


    public ArrayList<BandsInTownEventResult> getEventsAttending() {
        ArrayList<BandsInTownEventResult> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT_ATTENDING;
        /* Specified record
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE "
                + KEY_ID + " = " + todo_id;
        */
        Log.e(TAG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                BandsInTownEventResult event = new BandsInTownEventResult();
                event.setId(c.getInt(c.getColumnIndex(EVENT_ID)));
                event.setDatetime(convertToDate(c.getString(c.getColumnIndex(EVENT_DATE))));
                //event.setDatetime();
                event.setTitle(c.getString(c.getColumnIndex(EVENT_TITLE)));
                ArrayList<BandsInTownArtist> artistList = new ArrayList<>();
                BandsInTownArtist artist = new BandsInTownArtist();
                artist.setMbid(c.getString(c.getColumnIndex(ARTIST_MBID)));
                artist.setName(c.getString(c.getColumnIndex(ARTIST_NAME)));
                artistList.add(artist);
                event.setArtists(artistList);
                event.setFormattedLocation(c.getString(c.getColumnIndex(EVENT_FORMATTED_LOC)));
                event.setAttending(true);
                Venue v = new Venue();
                v.setCity(c.getString(c.getColumnIndex(EVENT_VENUE_CITY)));
                v.setName(c.getString(c.getColumnIndex(EVENT_VENUE_NAME)));
                v.setPlace(c.getString(c.getColumnIndex(EVENT_VENUE_PLACE)));
                v.setRegion(c.getString(c.getColumnIndex(EVENT_VENUE_REGION)));
                v.setCountry(c.getString(c.getColumnIndex(EVENT_VENUE_COUNTRY)));
                event.setVenue(v);

                if(c.getColumnIndex(EVENT_IS_ATTENDING)==0){
                    event.setAttending(false);
                }
                else{
                    event.setAttending(true);
                }


                events.add(event);
            } while(c.moveToNext());
            c.moveToFirst();
        }
        Collections.sort(events, new Comparator<BandsInTownEventResult>() {
            @Override
            public int compare(BandsInTownEventResult o1, BandsInTownEventResult o2) {
                return o1.getDatetime().compareTo(o2.getDatetime());
            }
        });
        return events;
    }

    public ArrayList<BandsInTownEventResult> getEventsAll() {
        ArrayList<BandsInTownEventResult> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT_ALL;
        /* Specified record
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE "
                + KEY_ID + " = " + todo_id;
        */
        Log.e(TAG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                BandsInTownEventResult event = new BandsInTownEventResult();
                event.setId(c.getInt(c.getColumnIndex(EVENT_ID)));
                event.setDatetime(convertToDate(c.getString(c.getColumnIndex(EVENT_DATE))));
                //event.setDatetime();
                event.setTitle(c.getString(c.getColumnIndex(EVENT_TITLE)));
                ArrayList<BandsInTownArtist> artistList = new ArrayList<>();
                BandsInTownArtist artist = new BandsInTownArtist();
                artist.setMbid(c.getString(c.getColumnIndex(ARTIST_MBID)));
                artist.setName(c.getString(c.getColumnIndex(ARTIST_NAME)));
                artistList.add(artist);
                event.setArtists(artistList);
                event.setFormattedLocation(c.getString(c.getColumnIndex(EVENT_FORMATTED_LOC)));
                event.setAttending(false);
                Venue v = new Venue();
                v.setCity(c.getString(c.getColumnIndex(EVENT_VENUE_CITY)));
                v.setName(c.getString(c.getColumnIndex(EVENT_VENUE_NAME)));
                v.setPlace(c.getString(c.getColumnIndex(EVENT_VENUE_PLACE)));
                v.setRegion(c.getString(c.getColumnIndex(EVENT_VENUE_REGION)));
                v.setCountry(c.getString(c.getColumnIndex(EVENT_VENUE_COUNTRY)));
                event.setVenue(v);
                events.add(event);
            } while(c.moveToNext());
            c.moveToFirst();
        }
        Collections.sort(events, new Comparator<BandsInTownEventResult>() {
            @Override
            public int compare(BandsInTownEventResult o1, BandsInTownEventResult o2) {
                return o1.getDatetime().compareTo(o2.getDatetime());
            }
        });
        return events;
    }

    public ArrayList<BandsInTownEventResult> getEventsLocal() {
        ArrayList<BandsInTownEventResult> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT_LOCAL;

        /* Specified record
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE "
                + KEY_ID + " = " + todo_id;
        */
        Log.e(TAG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                BandsInTownEventResult event = new BandsInTownEventResult();
                event.setId(c.getInt(c.getColumnIndex(EVENT_ID)));
                event.setDatetime(convertToDate(c.getString(c.getColumnIndex(EVENT_DATE))));
                //event.setDatetime();
                event.setTitle(c.getString(c.getColumnIndex(EVENT_TITLE)));
                ArrayList<BandsInTownArtist> artistList = new ArrayList<>();
                BandsInTownArtist artist = new BandsInTownArtist();
                artist.setMbid(c.getString(c.getColumnIndex(ARTIST_MBID)));
                artist.setName(c.getString(c.getColumnIndex(ARTIST_NAME)));
                artistList.add(artist);
                event.setArtists(artistList);
                event.setFormattedLocation(c.getString(c.getColumnIndex(EVENT_FORMATTED_LOC)));
                event.setAttending(false);
                Venue v = new Venue();
                v.setCity(c.getString(c.getColumnIndex(EVENT_VENUE_CITY)));
                v.setName(c.getString(c.getColumnIndex(EVENT_VENUE_NAME)));
                v.setPlace(c.getString(c.getColumnIndex(EVENT_VENUE_PLACE)));
                v.setRegion(c.getString(c.getColumnIndex(EVENT_VENUE_REGION)));
                v.setCountry(c.getString(c.getColumnIndex(EVENT_VENUE_COUNTRY)));
                event.setVenue(v);
                events.add(event);
            } while(c.moveToNext());
            c.moveToFirst();
        }
        Collections.sort(events, new Comparator<BandsInTownEventResult>() {
            @Override
            public int compare(BandsInTownEventResult o1, BandsInTownEventResult o2) {
                return o1.getDatetime().compareTo(o2.getDatetime());
            }
        });
        return events;
    }

    public ArrayList<ArtistLastFm> getAllArtists() {
        ArrayList<ArtistLastFm> artists = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ARTIST;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                ArtistLastFm artist = new ArtistLastFm();
                artist.setArtist(new Artist());
                artist.getArtist().setMbid(c.getString(c.getColumnIndex(ARTIST_MBID)));
                artist.getArtist().setName(c.getString(c.getColumnIndex(ARTIST_NAME)));
                artists.add(artist);
            } while(c.moveToNext());
            c.moveToFirst();
        }
        return artists;
    }

    public BandsInTownEventResult getEventAttendingById(long eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT_ATTENDING + " WHERE "
                + EVENT_ID + " = " + eventId;
        /* Specified record
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE "
                + KEY_ID + " = " + todo_id;
        */
        Log.e(TAG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            BandsInTownEventResult event = new BandsInTownEventResult();
            event.setId(c.getInt(c.getColumnIndex(EVENT_ID)));
            event.setDatetime(convertToDate(c.getString(c.getColumnIndex(EVENT_DATE))));
            //event.setDatetime();
            event.setTitle(c.getString(c.getColumnIndex(EVENT_TITLE)));
            ArrayList<BandsInTownArtist> artistList = new ArrayList<>();
            BandsInTownArtist artist = new BandsInTownArtist();
            artistList.add(artist);
            artist.setMbid(c.getString(c.getColumnIndex(ARTIST_MBID)));
            event.setArtists(artistList);
            Venue v = new Venue();
            v.setCity(c.getString(c.getColumnIndex(EVENT_VENUE_CITY)));
            v.setName(c.getString(c.getColumnIndex(EVENT_TITLE)));
            v.setPlace(c.getString(c.getColumnIndex(EVENT_VENUE_PLACE)));
            v.setRegion(c.getString(c.getColumnIndex(EVENT_VENUE_REGION)));
            v.setCountry(c.getString(c.getColumnIndex(EVENT_VENUE_COUNTRY)));
            event.setVenue(v);
            return event;
        }
        else{
            return null;
        }
    }

    public void updateEventAllByIdMarkAsAttending(long eventId){
        //use this funciton to find an event in the all table and mark it as attending
        String selectQuery = "UPDATE " + TABLE_EVENT_ALL + " SET "
                + EVENT_IS_ATTENDING + " = "+ 1 + " WHERE "+ EVENT_ID +" = "+eventId;
    }

    public ArtistLastFm getArtistById(String mbid){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ARTIST + " WHERE "
                + ARTIST_MBID + " = '" + mbid+"'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            ArtistLastFm artist = new ArtistLastFm();
            artist.setArtist(new Artist());
            artist.getArtist().setMbid(c.getString(c.getColumnIndex(ARTIST_MBID)));
            artist.getArtist().setName(c.getString(c.getColumnIndex(ARTIST_NAME)));
            return artist;
        }
        else{
            return null;
        }
    }

    public int getCurrentVersion(){
        return DATABASE_VERSION;
    }

    public List<Long> getFavoritedEventIds(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+EVENT_ID+" FROM " + TABLE_EVENT_ATTENDING;
        Cursor c = db.rawQuery(selectQuery, null);
        List<Long> ids = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                long id = c.getInt(c.getColumnIndex(EVENT_ID));
                ids.add(id);
            } while(c.moveToNext());
            c.moveToFirst();
        }
        return ids;
    }

    public List<String> getFavoritedArtistMbids(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+ARTIST_MBID+" FROM " + TABLE_ARTIST;
        Cursor c = db.rawQuery(selectQuery, null);
        List<String> mbids = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                String mbid = c.getString(c.getColumnIndex(ARTIST_MBID));
                mbids.add(mbid);
            } while(c.moveToNext());
            c.moveToFirst();
        }
        return mbids;
    }

    public List<NameMbidPair> getFavoritedNameMbidPairs(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+ARTIST_MBID+","+ARTIST_NAME+" FROM " + TABLE_ARTIST;
        Cursor c = db.rawQuery(selectQuery, null);
        NameMbidPair pair;
        List<NameMbidPair> nmPairs = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                String mbid = c.getString(c.getColumnIndex(ARTIST_MBID));
                String artistName = c.getString(c.getColumnIndex(ARTIST_NAME));
                pair = new NameMbidPair(artistName, mbid);
                nmPairs.add(pair);
            } while(c.moveToNext());
            c.moveToFirst();
        }
        return nmPairs;
    }

    public void deleteEventAttending(long eventId){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_EVENT_ATTENDING,EVENT_ID+"="+eventId,null);
    }

    public void deleteArtist(String artistMbid){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_ARTIST,ARTIST_MBID+"='"+artistMbid+"'",null);
    }

    public void deleteEventsAttendingByArtist(String mbid){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_EVENT_ATTENDING,ARTIST_MBID+"='"+mbid+"'",null);
    }

    public void deleteEventsLocalByArtist(String mbid){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_EVENT_LOCAL,ARTIST_MBID+"='"+mbid+"'",null);
    }

    public void deleteEventsAllByArtist(String mbid){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_EVENT_ALL,ARTIST_MBID+"='"+mbid+"'",null);
    }

    private String getCurrentDateTimeAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM-dd-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String convertDateTimeToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM-dd-yyyy HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    private Date convertToDate(String string){
        DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



}