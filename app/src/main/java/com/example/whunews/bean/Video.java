package com.example.whunews.bean;

import cn.bmob.v3.BmobObject;

public class Video extends BmobObject{
    private final String videoUrl;
    private final String videoAuthor;
    private final String videoTitle;
    private String imageId;
    private final String videoImage;

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getImageId() {
        return imageId;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public String getVideoAuthor() {
        return videoAuthor;
    }


    public Video(String videoUrl, String videoAuthor, String videoTitle,  String videoImage) {
        this.videoUrl = videoUrl;
        this.videoAuthor = videoAuthor;
        this.videoTitle = videoTitle;
        this.videoImage = videoImage;
    }


}
