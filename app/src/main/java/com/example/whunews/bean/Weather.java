package com.example.whunews.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author Administrator
 * Created on 2018/4/3 0003
 * Goal :
 */

public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
