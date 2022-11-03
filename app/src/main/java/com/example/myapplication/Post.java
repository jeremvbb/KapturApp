package com.example.myapplication;

import java.util.Date;

public class Post {
    String name;
    String description;
    String url;
    String place;
    Date date;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String email) {
        this.url = url;
    }

    public String getPlace() {

        return place;
    }

    public void setPlace(String place) {

        this.place = place;

    }



    public Post(String name, String url, String place, String description){
        this.name=name;
        this.url=url;
        this.place=place;
        this.description=description;

    };
}
