package com.anshaysaboo.socalbeach4life.Objects;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {
    private int duration;
    private int distance;
    private String durationString;
    private String distanceString;
    private List<LatLng> steps;

    public Route(int duration, int distance, String durationString, String distanceString, List<LatLng> steps) {
        this.duration = duration;
        this.distance = distance;
        this.durationString = durationString;
        this.distanceString = distanceString;
        this.steps = steps;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }

    public String getDurationString() {
        return durationString;
    }

    public String getDistanceString() {
        return distanceString;
    }

    public List<LatLng> getSteps() {
        return steps;
    }
}
