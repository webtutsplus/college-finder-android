package com.example.collegefinder;

public class College {
    private String mCity;
    private String mLatitude;
    private String mLongitude;

    public College(String city, String latitude, String longitude) {
        this.mCity = city;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
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
}
