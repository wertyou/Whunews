package com.example.whunews.util;

import com.example.whunews.bean.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author Administrator
 * Created on 2018/4/3 0003
 * Goal :
 */

public class Utility {
    /**
     * 将返回的数据转换成JSON，并解析为News实体类
     *
     * @return null or json data
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
