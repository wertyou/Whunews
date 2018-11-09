package com.example.whunews.bean;

import cn.bmob.v3.BmobObject;

/**
 * Author Administrator
 * Created on 2018/5/22 0022
 * Goal :
 */
public class Feedback extends BmobObject {

    private String sendTime;
    private String sendContent;
    private User author;

    public Feedback(String sendTime, String sendContent) {

        this.sendTime = sendTime;
        this.sendContent = sendContent;
    }

    public Feedback(){
        this.setTableName("Feedback");
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
