
package com.rtmillerprojects.sangitlive.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class BandsInTownEventResult {

    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("datetime")
    @Expose
    Date datetime;
    @SerializedName("formatted_datetime")
    @Expose
    String formattedDatetime;
    @SerializedName("formatted_location")
    @Expose
    String formattedLocation;
    @SerializedName("ticket_url")
    @Expose
    String ticketUrl;
    @SerializedName("ticket_type")
    @Expose
    String ticketType;
    boolean attending;
    @SerializedName("ticket_status")
    @Expose
    String ticketStatus;
    @SerializedName("on_sale_datetime")
    @Expose
    String onSaleDatetime;
    @SerializedName("facebook_rsvp_url")
    @Expose
    String facebookRsvpUrl;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("artists")
    @Expose
    List<BandsInTownArtist> artists = new ArrayList<BandsInTownArtist>();
    @SerializedName("venue")
    @Expose
    Venue venue;

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The datetime
     */
    public Date getDatetime() {
        return datetime;
    }

    /**
     * 
     * @param datetime
     *     The datetime
     */
    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    /**
     * 
     * @return
     *     The formattedDatetime
     */
    public String getFormattedDatetime() {
        return formattedDatetime;
    }

    /**
     * 
     * @param formattedDatetime
     *     The formatted_datetime
     */
    public void setFormattedDatetime(String formattedDatetime) {
        this.formattedDatetime = formattedDatetime;
    }

    /**
     * 
     * @return
     *     The formattedLocation
     */
    public String getFormattedLocation() {
        return formattedLocation;
    }

    /**
     * 
     * @param formattedLocation
     *     The formatted_location
     */
    public void setFormattedLocation(String formattedLocation) {
        this.formattedLocation = formattedLocation;
    }

    /**
     * 
     * @return
     *     The ticketUrl
     */
    public String getTicketUrl() {
        return ticketUrl;
    }

    /**
     * 
     * @param ticketUrl
     *     The ticket_url
     */
    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    /**
     * 
     * @return
     *     The ticketType
     */
    public String getTicketType() {
        return ticketType;
    }

    /**
     * 
     * @param ticketType
     *     The ticket_type
     */
    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    /**
     * 
     * @return
     *     The ticketStatus
     */
    public String getTicketStatus() {
        return ticketStatus;
    }

    /**
     * 
     * @param ticketStatus
     *     The ticket_status
     */
    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    /**
     * 
     * @return
     *     The onSaleDatetime
     */
    public String getOnSaleDatetime() {
        return onSaleDatetime;
    }

    /**
     * 
     * @param onSaleDatetime
     *     The on_sale_datetime
     */
    public void setOnSaleDatetime(String onSaleDatetime) {
        this.onSaleDatetime = onSaleDatetime;
    }

    /**
     * 
     * @return
     *     The facebookRsvpUrl
     */
    public String getFacebookRsvpUrl() {
        return facebookRsvpUrl;
    }

    /**
     * 
     * @param facebookRsvpUrl
     *     The facebook_rsvp_url
     */
    public void setFacebookRsvpUrl(String facebookRsvpUrl) {
        this.facebookRsvpUrl = facebookRsvpUrl;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The artists
     */
    public List<BandsInTownArtist> getArtists() {
        return artists;
    }

    /**
     * 
     * @param artists
     *     The artists
     */
    public void setArtists(List<BandsInTownArtist> artists) {
        this.artists = artists;
    }

    /**
     * 
     * @return
     *     The venue
     */
    public Venue getVenue() {
        return venue;
    }

    /**
     * 
     * @param venue
     *     The venue
     */
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public boolean isAttending() {
        return attending;
    }

    public void setAttending(boolean attending) {
        this.attending = attending;
    }

}
