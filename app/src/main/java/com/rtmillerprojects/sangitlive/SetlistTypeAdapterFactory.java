package com.rtmillerprojects.sangitlive;

/**
 * Created by Ryan on 8/23/2016.
 */
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class SetlistTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {

            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {

                JsonElement jsonElement = elementAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    //For sets that return a string
                    if(jsonObject.has("sets")){
                        JsonObject sets = new JsonObject();
                        try {
                            sets = jsonObject.get("sets").getAsJsonObject();
                        }
                        catch (Exception e) {
                            JsonArray set = new JsonArray();
                            sets.add("set",set);
                            jsonObject.add("sets", sets);
                        }
                        jsonElement = jsonObject;
                        //return delegate.fromJsonTree(jsonElement);
                    }

                    if(jsonObject.has("set")){
                        JsonArray sets = new JsonArray();
                        try {
                            sets = jsonObject.get("set").getAsJsonArray();
                        }
                        catch (Exception e) {
                            JsonObject set = jsonObject.get("set").getAsJsonObject();
                            jsonObject.remove("set");
                            sets.add(set);
                            jsonObject.add("set", sets);
                        }
                        jsonElement = jsonObject;
                        //return delegate.fromJsonTree(jsonElement);
                    }

                    if(jsonObject.has("song")){
                        JsonArray songs = new JsonArray();
                        try {
                            songs = jsonObject.get("song").getAsJsonArray();
                        }
                        catch (Exception e) {
                            JsonObject song = jsonObject.get("song").getAsJsonObject();
                            jsonObject.remove("song");
                            songs.add(song);
                            jsonObject.add("song", songs);
                        }
                        jsonElement = jsonObject;
                    }

                }

                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }
}