
package com.rtmillerprojects.sangitlive;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtmillerprojects.sangitlive.ItemTypeAdapterFactory;
import com.rtmillerprojects.sangitlive.SetlistsByArtists;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class NewConnection extends AsyncTask<String, String, String> {
    private int responseHttp = 0;
    StringBuilder result = new StringBuilder();
    private String flag = "false";
    private HttpURLConnection urlConnection = null;
    int page=1;
    private String mbid;
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
    protected void onPreExecute(){
        //This method can call anything in outer class
        //Done before doInBackground()
        //super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String receive) {
        //This method receives a result
        //Same data type as return from doInBackground
        //Recieves input from doInBackground's output
        if(receive.equalsIgnoreCase("true")){
            //doTimerTask();
        }else
        if(receive.equalsIgnoreCase("false")){
            //showAlert
        }
    }
}