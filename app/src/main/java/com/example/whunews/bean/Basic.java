package com.example.whunews.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Author Administrator
 * Created on 2018/4/3 0003
 * Goal :
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{

        @SerializedName("loc")
        public String updateTime;
    }
}
