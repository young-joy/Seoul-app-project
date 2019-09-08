package edu.skku.jonadan.hangangmongttang;

import android.content.res.Resources;

import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

public class SeoulApiProvider {

    private final Retrofit retrofit;
    private final SeoulApi api;
    private final OkHttpClient client = createClient();

    private final String BASE_URL = "http://openAPI.seoul.go.kr:8088/";
    private final String APP_KEY = "436851527a756c6c3435456f5a4d4d";
    private final int MAXIMUM_RQ = 999;

    public SeoulApiProvider() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(initUrl())
                .addConverterFactory(TikXmlConverterFactory.create(
                        new TikXml.Builder().exceptionOnUnreadXml(false).build()))
                .client(client)
                .build();
        this.api = this.retrofit.create(SeoulApi.class);
    }

    private String initUrl() {
        String url = BASE_URL;
        url += APP_KEY;
        url += "/xml/";
        return url;
    }

    private static OkHttpClient createClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

    public Call<SeoulApiResult> callToilet(int startIdx) {
        Call<SeoulApiResult> callToilet =
                api.getLocation("GeoInfoPublicToiletWGS", startIdx, startIdx + MAXIMUM_RQ);
        return callToilet;
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
}
