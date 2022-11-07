package com.anshaysaboo.socalbeach4life.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Objects;

public class Restaurant implements Parcelable {
    private String name;
    private String id;
    private LatLng location;
    private double rating;
    private int reviewCount;
    private String address;
    private String photoReference;
    private int priceLevel;

    public Restaurant(String name, String id, LatLng location, double rating, int reviewCount, String address, String photoReference, int priceLevel ) {
        this.name = name;
        this.id = id;
        this.location = location;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.address = address;
        this.photoReference = photoReference;
        this.priceLevel = priceLevel;
    }

    protected Restaurant(Parcel in) {
        name = in.readString();
        id = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
        rating = in.readDouble();
        reviewCount = in.readInt();
        address = in.readString();
        photoReference = in.readString();
        priceLevel = in.readInt();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

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

    public String getId() {
        return id;
    }

    public String getPriceLevelString() {
        if (priceLevel == -1) return "";
        String str = "";
        for (int i = 0; i < priceLevel; i ++) {
            str += "$";
        }
        return str;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, location, rating, reviewCount, address, photoReference, priceLevel);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeParcelable(location, 0);
        parcel.writeDouble(rating);
        parcel.writeInt(reviewCount);
        parcel.writeString(address);
        parcel.writeString(photoReference);
        parcel.writeInt(priceLevel);
    }

    public static class Details {
        private String description;
        private String website;
        private List<String> hours;

        public Details(String description, String website, List<String> hours) {
            this.description = description;
            this.website = website;
            this.hours = hours;
        }

        public String getDescription() {
            return description;
        }

        public String getWebsite() {
            return website;
        }

        public List<String> getHours() {
            return hours;
        }
    }
}
