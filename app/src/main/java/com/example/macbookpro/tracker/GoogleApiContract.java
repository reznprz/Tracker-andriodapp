package com.example.macbookpro.tracker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by macbookpro on 2/20/18.
 */

public interface GoogleApiContract {

    @GET("json?")
    Call<GeoCoded_WayPoints> getDirection(@Query("origin") String origin, @Query("destination") String destination, @Query("waypoints") String waypoints, @Query("key") String key);


}
