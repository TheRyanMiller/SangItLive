
package com.rtmillerprojects.sangitlive.model.musicbrainzartistresourcemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeginArea {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("sort-name")
    @Expose
    private String sortName;
    @SerializedName("disambiguation")
    @Expose
    private String disambiguation;

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
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

    /**
     * 
     * @return
     *     The sortName
     */
    public String getSortName() {
        return sortName;
    }

    /**
     * 
     * @param sortName
     *     The sort-name
     */
    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    /**
     * 
     * @return
     *     The disambiguation
     */
    public String getDisambiguation() {
        return disambiguation;
    }

    /**
     * 
     * @param disambiguation
     *     The disambiguation
     */
    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

}
