package com.example.whunews.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Author Administrator
 * Created on 2018/4/3 0003
 * Goal :
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{

        @SerializedName("txt")
        public String info;
    }
}
