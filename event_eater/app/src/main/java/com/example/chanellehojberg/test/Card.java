package com.example.chanellehojberg.test;

/**
 * Created by ChanelleHojberg on 3/9/18.
 */

public class Card {

    private String description;
    private String location;
    private String imgURL;
    private String title;
    private String url;

    public Card(String imgURL, String title, String description, String location, String url) {
        this.imgURL = imgURL;
        this.title = title;
        this.description = description;
        this.location = location;
        this.url = url;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getURL() {
        return this.url;
    }

    public void setURL(String url) { this.url = url; }




}

