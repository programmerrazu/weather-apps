package com.razu.weather.service;

import com.google.gson.annotations.SerializedName;
import com.razu.weather.model.Main;
import com.razu.weather.model.Weather;
import com.razu.weather.model.Weathers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WeatherResponse implements Serializable {

    @SerializedName("message")
    private String message;

    @SerializedName("cod")
    private String code;

    @SerializedName("count")
    private int count;

    @SerializedName("weather")
    private List<Weathers> weathersList = new ArrayList<Weathers>();

    @SerializedName("list")
    private List<Weather> weatherList = new ArrayList<Weather>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public List<Weathers> getWeathersList() {
        return weathersList;
    }

    public void setWeathersList(List<Weathers> weathersList) {
        this.weathersList = weathersList;
    }

    @SerializedName("main")
    private Main main;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}