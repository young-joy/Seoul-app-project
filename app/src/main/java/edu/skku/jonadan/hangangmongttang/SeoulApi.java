package edu.skku.jonadan.hangangmongttang;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SeoulApi {
    @GET("{service}/{start}/{end}")
    Call<SeoulApiResult> getService(@Path("service") String service, @Path("start") int start,
                                    @Path("end") int end);
}
