package com.rtmillerprojects.sangitlive;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView responseText;
    private TextView titleText;
    private int page = 1;
    private Button searchButton;
    private SetAdapter slAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private EditText songString;
    private TextView resultsMsg;
    private SetlistsByArtists responseObj;
    private Context context;
    private Button btnChooseArtist;
    private SetlistsByArtists setlistsByArtists;
    private String url = "http://api.setlist.fm/rest/0.1/artist/69d9c5ba-7bba-4cb7-ab32-8ccc48ad4f97/setlists.json";
    private Gson gson;
    private String mbid;
    private AsyncHttpClient client;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ListView listView;
    private static final int CONNECTION_TIMEOUT = 150000;
    private static final int DATARETRIEVAL_TIMEOUT = 150000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String artistName = intent.getStringExtra("artistName");
        mbid = intent.getStringExtra("mbid");
        page=1;
        if(mbid!=null){
            setlistsByArtists = getJsonFromRest(mbid, page);
        }
        else{
            /* Testing */
            artistName = "Radiohead";
            mbid = "a74b1b7f-71a5-4011-9441-d0b5e4122711";
        }
        setlistsByArtists = getJsonFromRest(mbid, page);
        page++;

        recyclerView = (RecyclerView) findViewById(R.id.songlist);
        btnChooseArtist = (Button) findViewById(R.id.btn_choose_artist);
        resultsMsg = (TextView) findViewById(R.id.results_detail);

        songString = (EditText) findViewById(R.id.songSearch);
        searchButton = (Button) findViewById(R.id.searchButton);
        final Context context = this;
        if(artistName!=null) {
            btnChooseArtist.setText(artistName);
        }
        else{
            btnChooseArtist.setText("Choose an artist");
        }
        btnChooseArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchArtist();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //requestWebService(url);
                if(setlistsByArtists!=null) {
                    doSearch(songString.getText().toString(), setlistsByArtists);
                }
                else{
                    Toast.makeText(context,"No results found for this artist",Toast.LENGTH_SHORT).show();
                }

                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }

        });

        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory());
        Gson gson = gsonBuilder.create();
    }

    public SetlistsByArtists getJsonFromRest(String mbid, int page){

        String urlQuery = null;
        try {
            urlQuery = "http://api.setlist.fm/rest/0.1/search/setlists.json?artistMbid=" + URLEncoder.encode(mbid, "UTF-8");
            urlQuery+="&p="+ page;
            NewConnection cxn = new NewConnection();
            cxn.execute(urlQuery);
            setlistsByArtists = cxn.getSetlistsByArtists();
            return setlistsByArtists;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            if(setlistsByArtists==null) {
                return null;
            }
            else{return setlistsByArtists;}
        }
    }


    public void doSearch(String songString, final SetlistsByArtists setlistsByArtists) {
        String songName = songString;

        ArrayList<SetInfo> setInfo = new ArrayList<>();

        SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean set;
        //SetlistsByArtists.SetlistsBean.SetlistBean.SetsBean.SetBean.SongBean song;
        String song;
        ArrayList<String> songs = new ArrayList<>();
        int instanceCounter = 0;
        int totalSetsCounted = 0;
        for (SetlistsByArtists.SetlistsBean.SetlistBean sl : setlistsByArtists.getSetlists().getSetlist()) {
            //PUT SETS OUT HERE1!!!
            SetInfo si = new SetInfo();
            si.setCity(sl.getVenue().getCity().getName()+", "+sl.getVenue().getCity().getCountry().getName());
            si.setDate(sl.getEventDate());
            si.setVenue(sl.getVenue().getName());
            si.setTourName(sl.getTour());
            si.setWasPlayed(false);
            for (int i = 0; i < sl.getSets().getSet().size(); i++) {
                set = sl.getSets().getSet().get(i);
                for (int k = 0; k < set.getSong().size(); k++) {
                    song = set.getSong().get(k).getName().toLowerCase();
                    songs.add(set.getSong().get(k).getName());
                    if (songName.toLowerCase().equals(song) && !song.equals("")) {
                        instanceCounter++;
                        si.setWasPlayed(true);
                    }
                    //Need to add song to set songs array
                }
            }
            si.setSongs(songs);
            songs.clear();
            setInfo.add(si);
            totalSetsCounted++;
            //NEED TO ADD SI
        }

        resultsMsg.setText("This song matched "+instanceCounter+" of "+totalSetsCounted+" sets searched");

        slAdapter = new SetAdapter(setInfo, this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(slAdapter);
        recyclerView.setLayoutManager(layoutManager);
        final Context context = this;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            Toast.makeText(context,"KEEP SCROLLING BABY!",Toast.LENGTH_SHORT).show();
                            SetlistsByArtists rSetlistsByArtists = getJsonFromRest(mbid, page);
                            page++;
                            List<SetlistsByArtists.SetlistsBean.SetlistBean> slb = setlistsByArtists.getSetlists().getSetlist();
                            for(SetlistsByArtists.SetlistsBean.SetlistBean item : rSetlistsByArtists.getSetlists().getSetlist()){
                                slb.add(item);
                            }
                            SetlistsByArtists.SetlistsBean sb = setlistsByArtists.getSetlists();
                            sb.setSetlist(slb);
                            setlistsByArtists.setSetlists(sb);
                            recyclerView.getAdapter().notifyDataSetChanged();
                            //slAdapter.notifyDataSetChanged();
                            slAdapter.notifyItemRangeChanged(0,sb.getSetlist().size());
                            slAdapter.notifyDataSetChanged();
                            //ADD NEW RESULTS
                            // setlistsByArtists.getSetlists().set
                        }
                    }
                }
            }
        });
    }

    public void searchArtist(){
        Intent intent = new Intent(this, ArtistSearchActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        startActivity(intent);
        //startActivityForResult(intent,ARTIST_SEARCH_CODE);
    }

    public SetlistsByArtists getJsonFromFile(String mbid, int page) {

        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory());
        Gson gson = gsonBuilder.create();
        InputStream is = this.getResources().openRawResource(R.raw.samplesetlistjson);
        Reader reader = new InputStreamReader(is);
        SetlistsByArtists localSetlistsByArtists = gson.fromJson(reader, SetlistsByArtists.class);

        return localSetlistsByArtists;
    }

    public class NewConnection extends AsyncTask<String, Void, String> {
        private int responseHttp = 0;
        StringBuilder result = new StringBuilder();
        private String flag = "false";
        private HttpURLConnection urlConnection = null;
        private String pMbid = mbid +"&p="+ page;
        private SetlistsByArtists setlistsByArtists;
        public SetlistsByArtists getSetlistsByArtists() {
            return setlistsByArtists;
        }


        @Override
        protected String doInBackground(String... urlString) {
            // TODO Auto-generated method stub
            try {
                URL urlToRequest = new URL(urlString[0]);
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
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                /* STOCK CODE FROM SO
                URL url = new URL(urlString[0]);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(2000);
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                responseHttp = httpConnection.getResponseCode();
                if (responseHttp == HttpURLConnection.HTTP_OK) {
                    flag = "true";
                } else {
                    flag = "false";
                }
                */
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(result!=null && result.toString()!=""){
                String resultString = result.toString();
                GsonBuilder gsonBuilder = new GsonBuilder()
                        .registerTypeAdapterFactory(new ItemTypeAdapterFactory());
                Gson gson = gsonBuilder.create();
                setlistsByArtists = gson.fromJson(resultString, SetlistsByArtists.class);
                page++;

            }
            else{
                setlistsByArtists = null;
            }

            return flag;
        }
        @Override
        protected void onPostExecute(String receive) {
            if(receive.equalsIgnoreCase("true")){
                //doTimerTask();
            }else
            if(receive.equalsIgnoreCase("false")){
                //showAlert
            }
        }
    }
}

