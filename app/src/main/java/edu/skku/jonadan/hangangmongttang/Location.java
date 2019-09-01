package edu.skku.jonadan.hangangmongttang;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class Location {

    @PropertyElement(name="OBJECTID")
    private int objectId;

    private String name = "";

    @PropertyElement(name="LAT")
    private double lat;

    @PropertyElement(name="LNG")
    private double lng;

    public Location() {
    }

    public Location(int objectId, String name, double lat, double lng) {
        this.objectId = objectId;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
