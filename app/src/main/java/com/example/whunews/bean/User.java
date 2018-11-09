package com.example.whunews.bean;

import cn.bmob.v3.BmobUser;

/**
 * Author Administrator
 * Created on 2018/5/4 0004
 * Goal :
 */
public class User extends BmobUser {
    private String phoneMobileNumber;
    private String userPhoto;

    public String getPhoneMobileNumber() {
        return phoneMobileNumber;
    }

    public void setPhoneMobileNumber(String phoneMobileNumber) {
        this.phoneMobileNumber = phoneMobileNumber;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
