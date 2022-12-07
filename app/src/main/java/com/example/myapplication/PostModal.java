package com.example.myapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostModal {

        // variables for storing data
        // of our recycler view item
        private String id;
        private String media_type;
        private String permalink;
        private String media_url;
        private String username;
        private String place;
        private String caption;
        private String timestamp;

        public String getAuthor_url() {
            return author_url;
        }

        public void setAuthor_url(String author_url) {
            this.author_url = author_url;
        }

        public int getLikesCount() {
            return likesCount;
        }

        public void setLikesCount(int likesCount) {
            this.likesCount = likesCount;
        }
        public String getPlace() {
        return place;
    }

        public void setPlace(String place) {
        this.place = place;
    }

        private String author_url;
        private int likesCount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMedia_type() {
            return media_type;
        }

        public void setMedia_type(String media_type) {
            this.media_type = media_type;
        }

        public String getPermalink() {
            return permalink;
        }

        public void setPermalink(String permalink) {
            this.permalink = permalink;
        }

        public String getMedia_url() {
            System.out.println(media_url);
            return media_url;
        }

        public void setMedia_url(String media_url) {
            this.media_url = media_url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public Date getTimestamp() throws ParseException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            Date date1=sdf.parse(timestamp);
            return date1;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public PostModal(String id, String place, String permalink, String media_url, String username,
                          String caption, String timestamp) {
            this.id = id;
            this.place = place;
            this.permalink = permalink;
            this.media_url = media_url;
            this.username = username;
            this.caption = caption;
            this.timestamp = timestamp;

        }


}
