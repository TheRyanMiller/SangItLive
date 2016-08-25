package com.rtmillerprojects.sangitlive;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private TextView responseText;
    private TextView titleText;
    private Button searchButton;
    private SongInstanceAdapter siAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private EditText songString;
    SetlistsByArtists responseObj;
    private Button btnChooseArtist;
    String url = "http://api.setlist.fm/rest/0.1/artist/69d9c5ba-7bba-4cb7-ab32-8ccc48ad4f97/setlists.json";
    Gson gson;
    AsyncHttpClient client;
    ListView listView;
    private static final int CONNECTION_TIMEOUT = 150000;
    private static final int DATARETRIEVAL_TIMEOUT = 150000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.songlist);
        btnChooseArtist = (Button) findViewById(R.id.btn_choose_artist);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        songString = (EditText) findViewById(R.id.songSearch);
        searchButton = (Button) findViewById(R.id.searchButton);
        final Context context = this;

        btnChooseArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArtist();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked button", Toast.LENGTH_SHORT).show();
                //requestWebService(url);
                doSearch(songString.getText().toString());
            }
        });

        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                /*
                .registerTypeAdapter(SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean.SongBean.class, new JsonDeserializer<SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean.SongBean>(){
                    @Override
                    public SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean.SongBean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        JsonObject myJson = json.getAsJsonObject();
                        Gson gson = new Gson();
                        JsonArray songs = null;
                        try {
                            songs = myJson.get("song").getAsJsonArray();
                        } catch (Exception e) {
                            JsonObject song = myJson.get("song").getAsJsonObject();
                            songs = new JsonArray();
                            songs.add(song);

                        }
                        SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean.SongBean response = gson.fromJson(songs, SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean.SongBean.class);
                        return response;
                    }
                })
                .registerTypeAdapter(SetlistsByArtists.class, new JsonDeserializer<SetlistsByArtists>(){
                    @Override
                    public SetlistsByArtists deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
                        JsonObject setsObject = arg0.getAsJsonObject();
                        Gson gson = new Gson();
                        SetlistsByArtists response = gson.fromJson(arg0, SetlistsByArtists.class);
                        //SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean sets;
                        int sets;
                        try {
                            sets = setsObject.get("test").getAsInt();
                        } catch (Exception e) {
                            sets = 0;
                        }
                        //movie.setMovieRuntime(runtime);
                        return response;
                    }
                })
                */;

        Gson gson = gsonBuilder.create();
    }




    public void doSearch(String songString) {
        String songName = songString;

        ArrayList<SongPlayedInfo> songInfo = new ArrayList<>();
        InputStream is = this.getResources().openRawResource(R.raw.samplesetlistjson);
        Reader reader1 = new InputStreamReader(is);
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory());
        Gson gson = gsonBuilder.create();
        SetlistsByArtists setlistsByArtists = gson.fromJson(reader1, SetlistsByArtists.class);
        SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean set;
        SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean.SongBean song;
        for (SetlistsByArtists.SetlistsBean.SetlistBean sl : setlistsByArtists.getSetlists().getSetlist()) {
            for (int i = 0; i < sl.getSets().getSet().size(); i++) {
                set = sl.getSets().getSet().get(i);
                for (int k = 0; k < set.getSong().size(); k++) {
                    song = set.getSong().get(k);
                    if (songName.toLowerCase().matches(song.getName().toString().toLowerCase())) {
                        int instanceCounter = songInfo.size();
                        SongPlayedInfo spi = new SongPlayedInfo();
                        spi.setCity(sl.getVenue().getCity().getName()+", "+sl.getVenue().getCity().getCountry().getName());
                        spi.setDate(sl.getEventDate());
                        spi.setSongName(song.getName());
                        spi.setVenue(sl.getVenue().getName());
                        spi.setTourName(sl.getTour());
                        songInfo.add(spi);
                    }
                }
            }
        }

        siAdapter = new SongInstanceAdapter(songInfo, this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(siAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void searchArtist(){
        Intent intent = new Intent(this, ArtistSearchActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        startActivity(intent);
    }
}

    /*
    public static SetlistsByArtists requestWebService(String serviceUrl) {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            InputStream stream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }

            //






            //return gson.fromJson(result.toString(),Response.class);
        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
        }
        //catch (GsonEx e) {
            // response body is no valid JSON string
        //}
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }


        return null;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    /*
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
    */

