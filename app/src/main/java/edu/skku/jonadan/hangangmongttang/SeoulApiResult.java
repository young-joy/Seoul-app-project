package edu.skku.jonadan.hangangmongttang;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SeoulApiResult {

    @SerializedName(value = "GeoInfoPublicToiletWGS", alternate={
            "GeoInfoStoreWGS", "GeoInfoDrinkWaterWGS",

            // Entertain
            "GeoInfoQuayWGS", "GeoInfoWaterLeisureWGS",
            "GeoInfoBoatStorageWGS", "GeoInfoDuckBoatWGS",
            "GeoInfoWaterTaxiWGS", "GeoInfoPlaygroundWGS",

            // Athletic
            "GeoInfoRockClimbWGS", "GeoInfoInlineSkateWGS",
            "GeoInfoJokguWGS", "GeoInfoTrackWGS", "GeoInfoBadmintonWGS"
    })
    private SeoulApiService service;

    public SeoulApiService getService() {
        return service;
    }

    public void setService(SeoulApiService service) {
        this.service = service;
    }

    public class SeoulApiService {

        @SerializedName(value = "row")
        private ArrayList<Location> items;

        public ArrayList<Location> getItems() {
            return items;
        }

        public void setItems(ArrayList<Location> items) {
            this.items = items;
        }
    }
}
