
package com.rtmillerprojects.sangitlive.model.GoogleLocation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class CustomLocationResult {

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityAbv() {
        return cityAbv;
    }

    public void setCityAbv(String cityAbv) {
        this.cityAbv = cityAbv;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateAbv() {
        return stateAbv;
    }

    public void setStateAbv(String stateAbv) {
        this.stateAbv = stateAbv;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryAbv() {
        return countryAbv;
    }

    public void setCountryAbv(String countryAbv) {
        this.countryAbv = countryAbv;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String zip;
    public String city;
    public String cityAbv;
    public String state;
    public String stateAbv;
    public String country;
    public String countryAbv;
    public String formattedAddress;
    public String county;
    public String countyAbv;

    @Override
    public String toString() {
        return formattedAddress;
    }
}
