package com.serviciosyave.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OSMAddress {
    // Campos comunes en Venezuela
    @JsonProperty("city")
    private String city;

    @JsonProperty("town")
    private String town;

    @JsonProperty("village")
    private String village;

    @JsonProperty("state")
    private String state;

    @JsonProperty("region")
    private String region;

    // Getters y Setters
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}