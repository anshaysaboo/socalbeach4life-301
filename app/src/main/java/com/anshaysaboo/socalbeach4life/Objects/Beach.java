package com.anshaysaboo.socalbeach4life.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.io.Serializable;
import java.util.Objects;

public class Beach implements Parcelable {
    private String name;
    private LatLng location;
    private double rating;
    private int reviewCount;
    private String address;
    private String imageUrl;
    private String id;
    private String yelpUrl;

    public Beach(String name, LatLng location, double rating, int reviewCount, String address, String imageUrl, String id, String yelpUrl) {
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.address = address;
        this.imageUrl = imageUrl;
        this.id = id;
        this.yelpUrl = yelpUrl;
    }

    protected Beach(Parcel in) {
        name = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
        rating = in.readDouble();
        reviewCount = in.readInt();
        address = in.readString();
        imageUrl = in.readString();
        id = in.readString();
        yelpUrl = in.readString();
    }

    public static final Creator<Beach> CREATOR = new Creator<Beach>() {
        @Override
        public Beach createFromParcel(Parcel in) {
            return new Beach(in);
        }

        @Override
        public Beach[] newArray(int size) {
            return new Beach[size];
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
        return imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getYelpUrl() {
        return yelpUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beach beach = (Beach) o;
        return name.equals(beach.name) && location.equals(beach.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeParcelable(location, 0);
        parcel.writeDouble(rating);
        parcel.writeInt(reviewCount);
        parcel.writeString(address);
        parcel.writeString(imageUrl);
        parcel.writeString(id);
        parcel.writeString(yelpUrl);
    }
}
