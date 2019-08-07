package com.toxic.salonapp;

import com.google.firebase.database.ServerValue;


public class SalonPost {

    private String postKey;
    private String id;
    private String image_url;
    private String desc;
    private String lokasi_direct;
    private Object timestamp;


    public SalonPost(String lokasi_direct, String id, String image_url, String desc) {
        this.id = id;
        this.image_url = image_url;
        this.desc = desc;
        this.lokasi_direct = lokasi_direct;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public SalonPost() {
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getUser_id() {
        return id;
    }

    public void setUser_id(String id) {
        this.id = id;
    }

    public String getLokasi_direct(){
        return lokasi_direct;
    }

    public void setLokasi_direct(){
        this.lokasi_direct = lokasi_direct;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timestamp = timeStamp;
    }


}

