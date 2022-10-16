package com.anshaysaboo.socalbeach4life.Objects;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class ParkingLot {
    private String name;
    private LatLng location;

    public ParkingLot(String name, LatLng location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }
}
