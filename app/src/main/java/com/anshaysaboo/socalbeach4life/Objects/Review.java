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

    public Review(String author, String body, String beachId, String wouldRecommend, int rating, Date date, String beachName) {
        this.author = author;
        this.body = body;
        this.beachId = beachId;
        this.wouldRecommend = wouldRecommend;
        this.rating = rating;
        this.date = date;
        this.beachName = beachName;
    }

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
}
