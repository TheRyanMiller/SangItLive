package com.rtmillerprojects.sangitlive.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.EventBus;
import com.rtmillerprojects.sangitlive.model.BandsInTownEventResult;
import com.rtmillerprojects.sangitlive.model.EventCalls.CheckInternetStatus;
import com.rtmillerprojects.sangitlive.model.EventCalls.ReturnInternetStatus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ryan on 10/10/2016.
 */


public class ServiceInternetStatus {

    private static final String TAG = "SERVICE INTERNET STATUS";
    Context context;

    public ServiceInternetStatus(Context context) {
        this.context = context;
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Subscribe
    public void hasInternetAccess(CheckInternetStatus cis) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://clients3.google.com/generate_204")
                .build();


        if (isNetworkAvailable(cis.context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();



                Log.e(TAG, "Successfully connected");
                boolean connected = (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
                String message;
                if(connected){
                    message = "Connected.";
                }
                else{message = "Connected to network but not internet.";}
                EventBus.post(new ReturnInternetStatus(connected,message));
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
                EventBus.post(new ReturnInternetStatus(false,"Error which checking internet connection"));
            }
        } else {
            Log.d(TAG, "Not connected to any network!");
            EventBus.post(new ReturnInternetStatus(false,"No network connection detected."));
        }
    }


}
