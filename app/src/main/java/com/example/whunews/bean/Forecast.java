package com.example.whunews.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Author Administrator
 * Created on 2018/4/3 0003
 * Goal :
 */

public class Forecast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;

        public String min;
    }

    public class More{

        @SerializedName("txt_d")
        public String info;
    }
}
