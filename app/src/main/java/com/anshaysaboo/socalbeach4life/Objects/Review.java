package com.anshaysaboo.socalbeach4life.Objects;

import java.util.Date;

public class Review {
    private String author;
    private String body;
    private String beachId;
    private String wouldRecommend;
    private int rating;
    private Date date;
    private String beachName;
    private String authorEmail;

    private String firebaseId;

    public Review(String author, String body, String beachId, String wouldRecommend, int rating, Date date, String beachName, String authorEmail) {
        this.author = author;
        this.body = body;
        this.beachId = beachId;
        this.wouldRecommend = wouldRecommend;
        this.rating = rating;
        this.date = date;
        this.beachName = beachName;
        this.authorEmail = authorEmail;
    }

    public Review() {}

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public String getBeachId() {
        return beachId;
    }

    public String getWouldRecommend() {
        return wouldRecommend;
    }

    public int getRating() {
        return rating;
    }

    public Date getDate() {
        return date;
    }

    public String getBeachName() {
        return beachName;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }
}
