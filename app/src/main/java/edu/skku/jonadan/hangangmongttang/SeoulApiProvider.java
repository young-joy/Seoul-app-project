package edu.skku.jonadan.hangangmongttang;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeoulApiProvider {

    public enum SERVICE_CODE {
        PARK, TOILET, SHOP, WATER,
        QUAY, WATER_LEISURE, BOAT, DUCK_BOAT, WATER_TAXI, PLAYGROUND,
        ROCK, SKATE, JOKGU, TRACK, BADMINTON
    }

    public final static int SERVICE_PAD = 1000000;

    private final Retrofit retrofit;
    private final SeoulApi api;
    private final OkHttpClient client = createClient();

    private final String BASE_URL = "http://openAPI.seoul.go.kr:8088/";
    private final String APP_KEY = "436851527a756c6c3435456f5a4d4d";

    public SeoulApiProvider() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(initUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        this.api = this.retrofit.create(SeoulApi.class);
    }

    private String initUrl() {
        String url = BASE_URL;
        url += APP_KEY;
        url += "/json/";
        return url;
    }

    private static OkHttpClient createClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

    public ArrayList<Call<SeoulApiResult>> callToilet() {
        ArrayList<Call<SeoulApiResult>> callToilets = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            callToilets.add(
                    api.getLocation("GeoInfoPublicToiletWGS",
                            1000*i+1, 1000*i+1000));
        }
        return callToilets;
    }

    public Call<SeoulApiResult> callShop() {
        Call<SeoulApiResult> callShop =
                api.getLocation("GeoInfoStoreWGS", 1, 1000);
        return callShop;
    }

    public Call<SeoulApiResult> callWater() {
        Call<SeoulApiResult> callWater =
                api.getLocation("GeoInfoDrinkWaterWGS", 1, 1000);
        return callWater;
    }

    public ArrayList<Call<SeoulApiResult>> callEntertain() {
        ArrayList<Call<SeoulApiResult>> callEntertains = new ArrayList<>();
        // Dock
        callEntertains.add(api.getLocation("GeoInfoQuayWGS", 1, 1000));
        // Water Leisure
        callEntertains.add(api.getLocation("GeoInfoWaterLeisureWGS", 1, 1000));
        // Boat
        callEntertains.add(api.getLocation("GeoInfoBoatStorageWGS", 1, 1000));
        // Duck Boat
        callEntertains.add(api.getLocation("GeoInfoDuckBoatWGS", 1, 1000));
        // Water Taxi
        callEntertains.add(api.getLocation("GeoInfoWaterTaxiWGS", 1, 1000));
        // Play ground
        callEntertains.add(api.getLocation("GeoInfoPlaygroundWGS", 1, 1000));
        return callEntertains;
    }

    public ArrayList<Call<SeoulApiResult>> callAthletic() {
        ArrayList<Call<SeoulApiResult>> callAthletics = new ArrayList<>();
        // Rock climb
        callAthletics.add(api.getLocation("GeoInfoRockClimbWGS", 1, 1000));
        // Inline skate
        callAthletics.add(api.getLocation("GeoInfoInlineSkateWGS", 1, 1000));
        // Jokgu
        callAthletics.add(api.getLocation("GeoInfoJokguWGS", 1, 1000));
        // Track
        callAthletics.add(api.getLocation("GeoInfoTrackWGS", 1, 1000));
        // Badminton
        callAthletics.add(api.getLocation("GeoInfoBadmintonWGS", 1, 1000));
        return callAthletics;
    }
}
