package com.razu.weather.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("data/2.5/find?")
    Call<WeatherResponse> geWeatherData(@Query("lat") double lat, @Query("lon") double lng, @Query("cnt") int count, @Query("appid") String appId);
}