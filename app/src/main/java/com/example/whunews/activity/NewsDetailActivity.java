package com.example.whunews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whunews.R;
import com.example.whunews.bean.Likes;
import com.example.whunews.bean.User;
import com.example.whunews.common.Common;
import com.example.whunews.common.PopupWindowNewsContent;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.onekeyshare.OnekeyShare;


public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String NEWS_TITLE = "news_title";
    public static final String NEWS_CONTENT = "news_content";
    public static final String NEWS_AUTHOR = "news_author";
    public static final String NEWS_TIME = "news_time";
    public static final String NEWS_IMAGE = "news_image";
    private WindowManager.LayoutParams params;
    private String newsTitle;
    private String newsContentUrl;
    private String newsImageUrl;
    private String newsAuthor;
    private String newsTime;
    private WebView webView;
    private PopupWindowNewsContent popup;
    private ImageView newsLike;
    private Common common = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initView();
        loadData();
        webView();
    }

    /**
     * 初始化View
     */
    private void initView() {

        ImageView share = findViewById(R.id.news_share);
        ImageView back = findViewById(R.id.news_back);
        webView = findViewById(R.id.news_webView);
        newsLike = findViewById(R.id.news_like);
        TextView newsComment = findViewById(R.id.news_comment);
        common = new Common();

        share.setOnClickListener(this);
        back.setOnClickListener(this);
        newsLike.setOnClickListener(this);
        newsComment.setOnClickListener(this);
    }

    private void loadData() {
        Intent intent = getIntent();
        newsTitle = intent.getStringExtra(NEWS_TITLE);
        newsAuthor = intent.getStringExtra(NEWS_AUTHOR);
        newsTime = intent.getStringExtra(NEWS_TIME);
        newsContentUrl = intent.getStringExtra(NEWS_CONTENT);
        newsImageUrl = intent.getStringExtra(NEWS_IMAGE);

        TextView news_title = findViewById(R.id.newsTitle);
        TextView news_author = findViewById(R.id.newsAuthor);
        TextView news_content = findViewById(R.id.newsContent);

        news_title.setText(newsTitle);
        news_author.setText(newsAuthor);
        news_content.setText(newsContentUrl);

    }

    /**
     * webView加载网页内容！
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void webView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(newsContentUrl);
    }

    /**
     * 新闻分享事件!
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(newsContentUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(newsTitle);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(newsImageUrl);
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 显示评论弹出框
     */
    private void showCommentPopupWindow() {
        popup = new PopupWindowNewsContent(NewsDetailActivity.this, itemsOnClick);
        popup.showAtLocation(findViewById(R.id.news_content),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().setAttributes(params);
    }

    /**
     * 关闭对话弹出框
     */
    private void closeCommentPopupWindow() {
        //保证点击按钮后关闭弹出框
        popup.dismiss();
        params = getWindow().getAttributes();
        params.alpha = 1.0f;
        getWindow().setAttributes(params);
    }

    /**
     * 设置收藏新闻数据
     */
    private void likeNews() {

        User user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            Likes likes = new Likes();
            likes.setAuthor(newsAuthor);
            likes.setTitle(newsTitle);
            likes.setTime(newsTime);
            likes.setImageUrl(newsImageUrl);
            likes.setContent(newsContentUrl);
            likes.setLikeAuthor(user);
            likes.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        showToast(R.string.like_success);
                        newsLike.setImageResource(R.mipmap.like);
                    } else {
                        showToast(R.string.like_error);
                    }
                }
            });
        } else {
            showToast(R.string.first_login);
        }
    }

    private final View.OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.send:
                    if (common.isLogin()) {
                        //TODO  将用户评论的内容存入到数据库
                        closeCommentPopupWindow();
                        showToast(R.string.send_success);
                    } else {
                        showToast(R.string.first_login);
                    }
                    break;
                case R.id.cancel:
                    if (common.isLogin()) {
                        closeCommentPopupWindow();
                        showToast(R.string.send_cancel);
                    } else {
                        showToast(R.string.first_login);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.news_back:
                onBackPressed();
                break;
            case R.id.news_share:
                showShare();
                break;
            case R.id.news_comment:
                showCommentPopupWindow();
                break;
            case R.id.news_like:
                likeNews();
                break;
            default:
                break;
        }
    }

    /**
     * 封装Toast
     *
     * @param resId 资源ID
     */
    private void showToast(int resId) {
        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }
}
