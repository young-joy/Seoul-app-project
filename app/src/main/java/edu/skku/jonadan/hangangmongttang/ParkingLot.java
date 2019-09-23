package edu.skku.jonadan.hangangmongttang;

import java.util.ArrayList;
import java.util.Arrays;

public class ParkingLot extends Location {

    private int capacity = 0;

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
