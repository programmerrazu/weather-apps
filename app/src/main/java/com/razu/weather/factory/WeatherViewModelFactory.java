package com.razu.weather.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.razu.weather.repository.WeatherRepository;
import com.razu.weather.viewModel.WeatherViewModel;

public class WeatherViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private WeatherRepository weatherRepository;

    public WeatherViewModelFactory(Application application, WeatherRepository weatherRepository) {
        this.application = application;
        this.weatherRepository = weatherRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WeatherViewModel(application, weatherRepository);
    }
}