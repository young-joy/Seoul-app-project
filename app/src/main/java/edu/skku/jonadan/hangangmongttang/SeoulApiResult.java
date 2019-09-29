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
            "GeoInfoJokguWGS", "GeoInfoTrackWGS", "GeoInfoBadmintonWGS",
    })
    private SeoulApiWGS serviceWGS;

    @SerializedName(value = "ForecastWarningMinuteParticleOfDustService", alternate = {
            "ForecastWarningUltrafineParticleOfDustService"
    })
    private SeoulApiForecast serviceForecast;

    public SeoulApiWGS getServiceWGS() {
        return serviceWGS;
    }

    public void setServiceWGS(SeoulApiWGS serviceWGS) {
        this.serviceWGS = serviceWGS;
    }

    public SeoulApiForecast getServiceForecast() {
        return serviceForecast;
    }

    public void setServiceForecast(SeoulApiForecast serviceForecast) {
        this.serviceForecast = serviceForecast;
    }

    public class SeoulApiWGS {

        @SerializedName(value = "row")
        private ArrayList<Location> items;

        public ArrayList<Location> getItems() {
            return items;
        }

        public void setItems(ArrayList<Location> items) {
            this.items = items;
        }
    }

    public class SeoulApiForecast {

        @SerializedName(value = "row")
        private ArrayList<Dust> items;

        public ArrayList<Dust> getItems() {
            return items;
        }

        public void setItems(ArrayList<Dust> items) {
            this.items = items;
        }
    }
}
