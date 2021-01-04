package com.webtutsplus.collegefinder;

public class College {
    private String mTitle;
    private String mCity;
    private String mLatitude;
    private String mLongitude;
    private String mDescription;
    private String mEstablished;
    private String mImageURL;

    public College(String title, String city, String latitude, String longitude, String description, String established, String imageURL) {
        this.mTitle = title;
        this.mCity = city;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mDescription = description;
        this.mEstablished = established;
        this.mImageURL = imageURL;
    }

    public String getmCity() {
        return this.mCity;
    }

    public String getmLatitude() {
        return this.mLatitude;
    }

    public String getmLongitude() {
        return this.mLongitude;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmEstablished() {
        return mEstablished;
    }

    public String getmImageURL() {
        return mImageURL;
    }
}
