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

public class ArtistTypeAdapterFactory implements TypeAdapterFactory {

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

                    //For artist page that returns only single result
                    if(jsonObject.has("artist")){
                        JsonArray artists = new JsonArray();
                        try {
                            artists = jsonObject.get("artist").getAsJsonArray();
                        }
                        catch (Exception e) {
                            JsonObject artist = jsonObject.get("artist").getAsJsonObject();
                            jsonObject.remove("artist");
                            artists.add(artist);
                            jsonObject.add("artist", artists);
                        }
                        jsonElement = jsonObject;
                        //return delegate.fromJsonTree(jsonElement);
                    }


                }

                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }
}