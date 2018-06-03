package com.example.intern12.easymeasurements.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by intern12 on 15.05.2017.
 */

public class Masurari {
    private String name;
    private String cultura,proprietar="";
    private double size;
    private double startPointLat,startPointLng;

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    private boolean isSelected;

    private String dataBaseKey;
    private String imgPath; // ? how do I do that
    private Date lastUpdated;

    public Masurari() {
    }

    public Masurari(String id, String name, double size, String cultura, String proprietar, double latPrim,double lngPrim) {
        this.name = name;
        this.cultura = cultura;
        this.size = size;
        this.dataBaseKey = id;
        this.proprietar=proprietar;
        this.startPointLat= latPrim;
        this.startPointLng=lngPrim;
    }

    public String getCultura() {
        return cultura;
    }

    public void setCultura(String cultura) {
        this.cultura = cultura;
    }

    public String getDataBaseKey() {
        return dataBaseKey;
    }

    public void setDataBaseKey(String dataBaseKey) {
        this.dataBaseKey = dataBaseKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getProprietar() {
        return proprietar;
    }

    public void setProprietar(String proprietar) {
        this.proprietar = proprietar;
    }

    public double getStartPointLat() {
        return startPointLat;
    }

    public double getStartPointLng(){
        return startPointLng;
    }

    public void setStartPoint(double lat,double lng) {

        startPointLat=lat;
        startPointLng=lng;
    }
}
