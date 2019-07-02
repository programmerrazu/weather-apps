package com.razu.weather.repository;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.razu.weather.model.Weather;
import com.razu.weather.service.WeatherService;
import com.razu.weather.service.RetrofitInstance;
import com.razu.weather.service.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherRepository {

    private static final String TAG = "WeatherRepository";
    private Application application;
    private MutableLiveData<List<Weather>> weatherMutableLiveData;
    private WeatherService weatherService;

    double lat = 23.68;
    double lon = 90.35;
    String BASE_URL = "http://api.openweathermap.org/";
    String APP_ID = "e384f9ac095b2109c751d95296f8ea76";

    public WeatherRepository(Application application) {
        this.application = application;
        if (weatherMutableLiveData == null) {
            weatherMutableLiveData = new MutableLiveData<>();
        }
        weatherService = RetrofitInstance.getRetrofitInstance(BASE_URL).create(WeatherService.class);
    }

    public MutableLiveData<List<Weather>> getWeatherData() {
        weatherService.geWeatherData(lat, lon, 50, APP_ID).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weather = response.body();
                    Log.i(TAG, "data " + weather);
                    assert weather != null;
                    weatherMutableLiveData.setValue(weather.getWeatherList());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.i(TAG, "adsad " + t.getMessage());
            }
        });
        return weatherMutableLiveData;
    }
}