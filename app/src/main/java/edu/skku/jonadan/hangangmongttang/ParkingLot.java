package edu.skku.jonadan.hangangmongttang;

public class ParkingLot extends Location {

    private int capacity = 0;

    public static final int PARKING_LOT_ID = 10000;

    public ParkingLot(int objectId, String name, double lat, double lng) {
        super(objectId, name, lat, lng);
    }

    public ParkingLot(int objectId, String name, double lat, double lng, int capacity) {
        super(objectId, name, lat, lng);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
