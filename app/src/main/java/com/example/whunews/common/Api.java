package com.example.whunews.common;

/**
 * Author Administrator
 * Created on 2018/4/13 0013
 * Goal :
 */

class Api {
    // 写好对应的函数接口地址,便于和后台对接数据时修改方便！
    private final String newsApi = "getNews";
    private final String videoApi = "getVideo";
    private final String messageApi = "getMessage";
    private final String userMessageApi = "getUserMessage";
    private final String userLikeNewsApi = "getUserLikeNews";
    private final String userCommentApi = "getUserComment";

    /**
     * 获取本地服务器电脑ip地址
     * ip地址要更换   手机一定要练上自己电脑的wifi 才能保证数据的实时性。
     *
     * @return ip
     */
    private String getIpAddress() {

        return "http://172.16.114.43/";
    }

    /**
     * 获取新闻接口数据的地址
     *
     * @return newsApi
     */
    public String getNewsApi() {
        return getIpAddress() + newsApi;
    }

    /**
     * 获取视频接口数据的地址
     *
     * @return videoApi
     */
    public String getVideoApi() {

        return getIpAddress() + videoApi;
    }

    /**
     * 获取动态数据的地址
     *
     * @return messageApi
     */
    public String getMessageApi() {

        return getIpAddress() + messageApi;
    }

    /**
     * 获取用户动态数据的地址
     *
     * @return userMessageApi
     */
    public String getUserMessageApi() {

        return getIpAddress() + userMessageApi;
    }

    /**
     * 获取用户收藏新闻的地址
     *
     * @return userLikeNewsApi
     */
    public String getUserLikeNewsApi() {

        return getIpAddress() + userLikeNewsApi;
    }

    /**
     * 获取用户评论的地址
     *
     * @return userCommentApi
     */
    public String getUserCommentApi() {
        return getIpAddress() + userCommentApi;
    }

}
