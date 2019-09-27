package edu.skku.jonadan.hangangmongttang;

public class ParkListItem {
    private int pid;
    private String name;
    private String img_src;
    private String location;
    private String number;
    private String attraction;
    private String facility;

    public ParkListItem(int pid, String name, String img_src, String location, String number, String attraction, String facility) {
        this.pid = pid;
        this.name = name;
        this.img_src = img_src;
        this.location = location;
        this.number = number;
        this.attraction = attraction;
        this.facility = facility;
    }


    public int getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public String getImg_src() {
        return img_src;
    }

    public String getLocation() {
        return location;
    }

    public String getNumber() {
        return number;
    }

    public String getAttraction() {
        return attraction;
    }

    public String getFacility() {
        return facility;
    }
}
