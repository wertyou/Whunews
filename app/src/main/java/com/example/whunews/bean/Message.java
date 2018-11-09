package com.example.whunews.bean;

import cn.bmob.v3.BmobObject;

/**
 * Author Administrator
 * Created on 2018/4/5 0005
 * Goal :
 */

public class Message extends BmobObject {
    private String userName;
    private String userPhoto;
    private String sendTime;
    private String sendContent;
    private User author;

    public Message(String userName, String userPhoto, String sendTime, String sendContent) {
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.sendTime = sendTime;
        this.sendContent = sendContent;
    }

    public Message() {
        this.setTableName("Message");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
