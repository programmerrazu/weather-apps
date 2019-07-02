package com.razu.weather.service;

import com.google.gson.annotations.SerializedName;
import com.razu.weather.model.Weather;

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
}