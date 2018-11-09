package com.example.whunews.common;


import com.example.whunews.bean.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.bmob.v3.BmobUser;


/**
 * Author Administrator
 * Created on 2018/3/6 0006
 * Goal :
 */

public class Common {
    /**
     * 把字符串转换成URL
     */
    public URL stringToUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 获取默认的图片路径
     *
     * @return 默认图片的URL
     */
    public String imagePath() {
        return "http://p6ajn4vht.bkt.clouddn.com/cherry.jpg";
    }

    /**
     * 获取当前的时间
     *
     * @return 年/月/日
     */
    public String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 判断用户是否已经登录
     *
     * @return true or false
     */
    public Boolean isLogin() {
        User currentUser = BmobUser.getCurrentUser(User.class);
        return currentUser != null;
    }
}
