package edu.skku.jonadan.hangangmongttang;

public class Location {
    private Integer objectId;
    private String name;
    private Double lat;
    private Double lng;

    public Location() {
    }

    public Location(Integer objectId, String name, Double lat, Double lng) {
        this.objectId = objectId;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
