
package com.rtmillerprojects.sangitlive.model.musicbrainzartistresourcemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Url {

    @SerializedName("resource")
    @Expose
    private String resource;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * 
     * @return
     *     The resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * 
     * @param resource
     *     The resource
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

}
