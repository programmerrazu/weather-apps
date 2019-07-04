package com.razu.weather.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.razu.weather.model.Weather;
import com.razu.weather.repository.WeatherRepository;
import com.razu.weather.service.WeatherResponse;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {

    private WeatherRepository weatherRepository;

    public WeatherViewModel(@NonNull Application application, WeatherRepository weatherRepository) {
        super(application);
        this.weatherRepository = weatherRepository;
    }

    public MutableLiveData<List<Weather>> getWeatherResponseData() {
        return weatherRepository.getWeatherData();
    }

    public MutableLiveData<WeatherResponse> getCurrentWeatherResponseData(double lat, double lng) {
        return weatherRepository.getCurrentWeatherData(lat, lng);
    }
}