package com.serviciosyave.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OSMResponse {
    @JsonProperty("address")
    private OSMAddress address;

    // Getters y Setters
    public OSMAddress getAddress() { return address; }
    public void setAddress(OSMAddress address) { this.address = address; }
}