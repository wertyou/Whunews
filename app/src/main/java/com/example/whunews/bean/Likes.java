package com.example.whunews.bean;

import cn.bmob.v3.BmobObject;

/**
 * Author Administrator
 * Created on 2018/5/16 0016
 * Goal :
 */
public class Likes extends BmobObject {
    private String title;
    private String author;
    private String imageUrl;
    private String time;
    private String content;
    private User likeAuthor;


    public Likes() {

    }

    public Likes(String title, String author, String imageUrl, String time, String content) {
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.time = time;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getLikeAuthor() {
        return likeAuthor;
    }

    public void setLikeAuthor(User likeAuthor) {
        this.likeAuthor = likeAuthor;
    }
}
