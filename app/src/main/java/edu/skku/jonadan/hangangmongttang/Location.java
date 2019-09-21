package edu.skku.jonadan.hangangmongttang;


import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName(value = "OBJECTID")
    private int objectId;

    @SerializedName(value="ENAME", alternate={
            // Entertain
            "WRNAME", "BKNAME", "DUNAME", "WTAXINAME",

            // Athletic
            "RWNAME", "INNAME", "JONAME", "TRNAME", "BADNAME"
    })
    private String name = "";

    @SerializedName(value = "TEL")
    private String tel = "";

    @SerializedName(value = "LAT")
    private double lat;

    @SerializedName(value = "LNG")
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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
