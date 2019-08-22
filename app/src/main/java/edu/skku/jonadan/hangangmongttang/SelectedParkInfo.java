package edu.skku.jonadan.hangangmongttang;

public class SelectedParkInfo {
    private static String name = "";
    private static String img_src = "";
    private static String location = "";
    private static String number = "";
    private static String attraction = "";
    private static String facility = "";

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        SelectedParkInfo.name = name;
    }

    public static String getImg_src() {
        return img_src;
    }

    public static void setImg_src(String img_src) {
        SelectedParkInfo.img_src = img_src;
    }

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String location) {
        SelectedParkInfo.location = location;
    }

    public static String getNumber() {
        return number;
    }

    public static void setNumber(String number) {
        SelectedParkInfo.number = number;
    }

    public static String getAttraction() {
        return attraction;
    }

    public static void setAttraction(String attraction) {
        SelectedParkInfo.attraction = attraction;
    }

    public static String getFacility() {
        return facility;
    }

    public static void setFacility(String facility) {
        SelectedParkInfo.facility = facility;
    }
}
