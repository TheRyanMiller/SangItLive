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
    private static final int DATABASE_VERSION = 3;

    //Table Defs
    public static final String DATABASE_NAME = "concertcompanion.db";
    public static final String TABLE_ARTIST = "artist";
    public static final String TABLE_EVENT = "event";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String ARTIST_MBID = "artist_mbid";
    private static final String KEY_CREATED_AT = "created_at";

    // Artist columns
    private static final String ARTIST_NAME = "name";

    // Event columns
    private static final String EVENT_ID = "event_id";
    private static final String EVENT_TITLE = "title";
    private static final String EVENT_VENUE_CITY = "city";
    private static final String EVENT_VENUE_PLACE = "place";
    private static final String EVENT_VENUE_REGION = "REGION";
    private static final String EVENT_VENUE_COUNTRY = "country";
    private static final String EVENT_VENUE_NAME = "venue_name";
    private static final String EVENT_DATE = "date";
    private static final String EVENT_IS_PAST = "is_past";


    private static final String CREATE_TABLE_ARTIST = "CREATE TABLE " +
            TABLE_ARTIST + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ARTIST_MBID + " TEXT," +
            ARTIST_NAME + " TEXT," +
            KEY_CREATED_AT + " DATETIME," +
            "UNIQUE("+ARTIST_MBID+"))";

    private static final String CREATE_TABLE_EVENT = "CREATE TABLE " + TABLE_EVENT + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            EVENT_ID + " TEXT UNIQUE," +
            ARTIST_MBID + " TEXT," +
            EVENT_TITLE + " TEXT," +
            EVENT_DATE + " DATETIME," +
            EVENT_VENUE_CITY + " TEXT," +
            EVENT_VENUE_COUNTRY + " TEXT," +
            EVENT_VENUE_REGION + " TEXT," +
            EVENT_VENUE_PLACE + " TEXT," +
            EVENT_VENUE_NAME + " TEXT," +
            EVENT_IS_PAST + " INTEGER," +
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
        db.execSQL(CREATE_TABLE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST);
        // create new tables
        onCreate(db);
    }

    /*
     * Creating a contact
     */
    public long insertEvent(BandsInTownEventResult event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_ID, event.getId());
        values.put(EVENT_TITLE, event.getTitle());
        values.put(ARTIST_MBID,event.getArtists().get(0).getMbid());
        values.put(EVENT_DATE, event.getDatetime().toString());
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
        };

        // insert row
        long contactId = db.insertWithOnConflict(TABLE_EVENT, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return contactId;
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


    public ArrayList<BandsInTownEventResult> getAllEvents() {
        ArrayList<BandsInTownEventResult> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT;
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

    public BandsInTownEventResult getEventById(long eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_EVENT + " WHERE "
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

    public List<Long> getFavoritedEventIds(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+EVENT_ID+" FROM " + TABLE_EVENT;
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


    public void deleteEvent(long eventId){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_EVENT,EVENT_ID+"="+eventId,null);
    }
    public void deleteArtist(String artistMbid){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_ARTIST,ARTIST_MBID+"='"+artistMbid+"'",null);
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



    /*

    GET STUFF BY ID EXAMPLE BELOW

    public AgendaItem getEventById(long recordId) {
        AgendaItem dbAgendaItem = new AgendaItem();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_AGENDA_ITEMS +" WHERE "+ KEY_ID +" = "+ recordId;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            dbAgendaItem.setTitle(c.getString(c.getColumnIndex(EVENT_TITLE)));
            try{
                Date dbDate = new Date(c.getLong(c.getColumnIndex(EVENT_DATE))*1000);
                dbAgendaItem.setDate(dbDate);
            } catch(ParseException e) {
                e.printStackTrace();
            }
            int isRecurring = c.getInt(c.getColumnIndex(EVENT_RECURRING));
            dbAgendaItem.setRecurRate(c.getString(c.getColumnIndex(EVENT_RECURRATE)));
            //Handle date type incompatibilities
            if(isRecurring==1){dbAgendaItem.setRecurring(true);}
            else{dbAgendaItem.setRecurring(false);}

            if(c.getBlob(c.getColumnIndex(EVENT_IMAGE))==null) {}
            else{
                dbAgendaItem.setEventImage(DbBitmapUtility.getImage(c.getBlob(c.getColumnIndex(EVENT_IMAGE))));
            }
        }
        return dbAgendaItem;
    }
   */




}