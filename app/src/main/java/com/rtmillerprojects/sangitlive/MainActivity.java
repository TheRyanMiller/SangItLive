package com.rtmillerprojects.sangitlive;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private TextView responseText;
    private TextView titleText;
    private Button searchButton;
    Response responseObj;
    String url = "http://api.setlist.fm/rest/0.1/artist/69d9c5ba-7bba-4cb7-ab32-8ccc48ad4f97/setlists.json";
    Gson gson;
    CustomAdapter adapter;
    AsyncHttpClient client;
    ListView listView;
    private static final int CONNECTION_TIMEOUT = 150000;
    private static final int DATARETRIEVAL_TIMEOUT = 150000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.songlist);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        responseText = (TextView) findViewById(R.id.response);
        searchButton = (Button) findViewById(R.id.searchButton);
        final Context context = this;

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"clicked button",Toast.LENGTH_SHORT).show();
                requestWebService(url);
                String abc = "3";
            }
        });
    }

    public static Response requestWebService(String serviceUrl) {
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
            Gson gson = new Gson();
            Gson gsonBuilder = new GsonBuilder()
                    .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                    /*
                    SetList.fm returns an object when there's a valid value for sets, but they
                    return an empty string when the value is null.  Because they return different
                    JSON primitive types we have to create a custom TypeAdapter for our Result.class.
                    */
                    .registerTypeAdapter(Response.class, new JsonDeserializer<Response>(){
                        @Override
                        public Response deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
                            JsonObject setsObject = arg0.getAsJsonObject();
                            Gson gson = new Gson();
                            Response response = gson.fromJson(arg0, Response.class);
                            Response.SetlistsBean.SetlistBean.SetsBean sets;
                            try {
                                sets = response..get("runtime").getAsInt();
                            } catch (Exception e) {
                                sets = 0;
                            }
                            movie.setMovieRuntime(runtime);
                            return movie;
                    })
            return gson.fromJson(result.toString(),Response.class);

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
}
