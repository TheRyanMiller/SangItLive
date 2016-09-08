
package com.rtmillerprojects.sangitlive.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class BandsInTownArtist {

    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("mbid")
    @Expose
    String mbid;
    @SerializedName("image_url")
    @Expose
    String imageUrl;
    @SerializedName("thumb_url")
    @Expose
    String thumbUrl;
    @SerializedName("facebook_tour_dates_url")
    @Expose
    String facebookTourDatesUrl;
    @SerializedName("facebook_page_url")
    @Expose
    String facebookPageUrl;
    @SerializedName("tracker_count")
    @Expose
    Integer trackerCount;
    @SerializedName("url")
    @Expose
    String url;
    @SerializedName("website")
    @Expose
    String website;

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
     *     The mbid
     */
    public String getMbid() {
        return mbid;
    }

    /**
     * 
     * @param mbid
     *     The mbid
     */
    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    /**
     * 
     * @return
     *     The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 
     * @param imageUrl
     *     The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 
     * @return
     *     The thumbUrl
     */
    public String getThumbUrl() {
        return thumbUrl;
    }

    /**
     * 
     * @param thumbUrl
     *     The thumb_url
     */
    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    /**
     * 
     * @return
     *     The facebookTourDatesUrl
     */
    public String getFacebookTourDatesUrl() {
        return facebookTourDatesUrl;
    }

    /**
     * 
     * @param facebookTourDatesUrl
     *     The facebook_tour_dates_url
     */
    public void setFacebookTourDatesUrl(String facebookTourDatesUrl) {
        this.facebookTourDatesUrl = facebookTourDatesUrl;
    }

    /**
     * 
     * @return
     *     The facebookPageUrl
     */
    public String getFacebookPageUrl() {
        return facebookPageUrl;
    }

    /**
     * 
     * @param facebookPageUrl
     *     The facebook_page_url
     */
    public void setFacebookPageUrl(String facebookPageUrl) {
        this.facebookPageUrl = facebookPageUrl;
    }

    /**
     * 
     * @return
     *     The trackerCount
     */
    public Integer getTrackerCount() {
        return trackerCount;
    }

    /**
     * 
     * @param trackerCount
     *     The tracker_count
     */
    public void setTrackerCount(Integer trackerCount) {
        this.trackerCount = trackerCount;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * 
     * @param website
     *     The website
     */
    public void setWebsite(String website) {
        this.website = website;
    }

}
