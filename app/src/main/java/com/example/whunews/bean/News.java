package com.example.whunews.bean;


import cn.bmob.v3.BmobObject;

/**
 * Author Administrator
 * Created on 2018/3/4 0004
 * Goal :
 */

public class News extends BmobObject {
    private String title;
    private String author;
    private String imageUrl;
    private String time;
    private String content;
    private User likeAuthor;
    private Boolean like;

    /**
     * 构造器
     *
     * @param title    新闻标题
     * @param author   新闻作者
     * @param imageUrl 图片id
     * @param time     发布时间
     * @param content  新闻内容
     */
    public News(String title, String author, String imageUrl, String time, String content) {
        this.title = title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.time = time;
        this.content = content;
    }

    public News() {
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

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }
}
