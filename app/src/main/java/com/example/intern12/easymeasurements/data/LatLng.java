package com.example.intern12.easymeasurements.data;



public class LatLng {
    private Double latitude;

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    private Double longitude;

    public LatLng() {
        this.latitude=0.0;
        this.longitude=0.0;
    }
    public LatLng(Double x,Double y){
        this.latitude=x;
        this.longitude=y;
    }
}
