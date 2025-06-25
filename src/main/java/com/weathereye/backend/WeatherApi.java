package com.weathereye.backend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("{city}")
    Call<String> getWeather(
            @Path("city") String city,
            @Query("unitGroup") String unitGroup,
            @Query("key") String key,
            @Query("contentType") String contentType
    );
}
