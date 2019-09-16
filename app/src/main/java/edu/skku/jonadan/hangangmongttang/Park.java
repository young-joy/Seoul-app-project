package edu.skku.jonadan.hangangmongttang;

import java.util.ArrayList;

public class Park extends Location {

    ArrayList<ParkingLot> parkingLots;

    public Park(int objectId, String name, double lat, double lng) {
        super(objectId, name, lat, lng);
    }

    public Park(int objectId, String name, double lat, double lng, ArrayList<ParkingLot> parkingLots) {
        super(objectId, name, lat, lng);
        this.parkingLots = parkingLots;
    }

    public ArrayList<ParkingLot> getParkingLots() {
        return parkingLots;
    }

    public void setParkingLots(ArrayList<ParkingLot> parkingLots) {
        this.parkingLots = parkingLots;
    }
}
