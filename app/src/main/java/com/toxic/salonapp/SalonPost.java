package com.toxic.salonapp;

import com.google.firebase.database.ServerValue;


public class SalonPost {

    private String postKey;
    private String id;
    private String image_url;
    private String image_fac;
    private String image_spa;
    private String desc;
    private String nama_salon;
    private String nomor_wa;
    private String lokasi_direct;
    private String dewasa;
    private String email;
    private String anak;
    private String spa;
    private String facial;
    public double latitude;
    public double longitude;
    private Object timestamp;


    public SalonPost(
            String nama_salon,
            String nomor_wa,
            String email,
            String lokasi_direct,
            String id,
            String image_url,
            String image_fac,
            String image_spa,
            String desc,
            String anak,
            String dewasa,
            String spa,
            double latitude,
            double longitude,
            String facial) {
        this.id = id;
        this.image_url = image_url;
        this.image_fac = image_fac;
        this.image_spa = image_spa;
        this.desc = desc;
        this.lokasi_direct = lokasi_direct;
        this.anak = anak;
        this.dewasa = dewasa;
        this.spa = spa;
        this.email = email;
        this.facial = facial;
        this.nama_salon = nama_salon;
        this.nomor_wa = nomor_wa;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama_salon() {
        return nama_salon;
    }

    public void setNama_salon(String nama_salon) {
        this.nama_salon = nama_salon;
    }

    public String getNomor_wa() {
        return nomor_wa;
    }

    public void setNomorwa(String nomor_wa) {
        this.nomor_wa = nomor_wa;
    }



    public String getLokasi_direct(){
        return lokasi_direct;
    }

    public void setLokasi_direct(){
        this.lokasi_direct = lokasi_direct;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_fac() {
        return image_fac;
    }

    public void setImage_fac(String image_fac) {
        this.image_fac = image_fac;
    }

    public String getImage_spa() {
        return image_spa;
    }

    public void setImage_spa(String image_spa) {
        this.image_spa = image_spa;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDewasa(){
        return dewasa;
    }

    public void setDewasa(){
        this.dewasa = dewasa;
    }

    public String getAnak(){
        return anak;
    }

    public void setAnak(){
        this.anak = anak;
    }

    public String getSpa(){
        return spa;
    }

    public void setSpa(){
        this.spa = spa;
    }

    public String getFacial(){
        return facial;
    }

    public void setFacial(){
        this.facial = facial;
    }

    public Object getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timestamp = timeStamp;
    }


}

