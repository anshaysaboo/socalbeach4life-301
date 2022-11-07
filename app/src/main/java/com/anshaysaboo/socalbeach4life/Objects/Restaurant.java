package com.anshaysaboo.socalbeach4life.Objects;

import com.google.android.gms.maps.model.LatLng;

public class Restaurant {
    private String name;
    private LatLng location;
    private double rating;
    private int reviewCount;
    private String address;
    private String photoReference;
    private int priceLevel;

    public Restaurant(String name, LatLng location, double rating, int reviewCount, String address, String photoReference, int priceLevel ) {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.address = address;
        this.photoReference = photoReference;
        this.priceLevel = priceLevel;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }

    public double getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return "https://maps.googleapis.com/maps/api/place/photo" +
                "?photo_reference=" + this.photoReference +
                "&key=" + Keys.GOOGLE_MAPS_API_KEY +
                "&maxwidth=1600";
    }

    public String getPriceLevelString() {
        if (priceLevel == -1) return "";
        String str = "";
        for (int i = 0; i < priceLevel; i ++) {
            str += "$";
        }
        return str;
    }
}
