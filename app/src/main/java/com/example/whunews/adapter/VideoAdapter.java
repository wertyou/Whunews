package com.example.whunews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.whunews.R;
import com.example.whunews.bean.Video;
import com.example.whunews.common.Common;

import java.net.URL;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.mob.tools.utils.Strings.getString;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private static Context mContext;
    private final List<Video> mVideoList;
    private static String videoTitle;
    private static String videoUrl;
    private static String imageUrl;


    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView videoShare;
        final ImageView videoImage;
        final TextView videoAuthor;
        final JCVideoPlayerStandard jcVideoPlayerStandard;

        private ViewHolder(View view) {
            super(view);
            videoAuthor = view.findViewById(R.id.videoAuthor);
            videoImage = view.findViewById(R.id.videoImage);
            videoShare = view.findViewById(R.id.videoShare);
            jcVideoPlayerStandard = view.findViewById(R.id.video_list);
            videoShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showShare();
                }
            });
        }
    }

    public VideoAdapter(List<Video> videoList) {
        mVideoList = videoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = mVideoList.get(position);
        videoTitle = video.getVideoTitle();
        videoUrl = video.getVideoUrl();
        imageUrl = video.getVideoImage();
        holder.videoAuthor.setText(video.getVideoAuthor());
        Glide.with(mContext).load(video.getImageId()).into(holder.videoImage);
        holder.jcVideoPlayerStandard.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_LIST, videoTitle);

        Common common = new Common();
        URL url = common.stringToUrl(imageUrl);
        //noinspection deprecation
        Glide.with(mContext).load(url).into(holder.jcVideoPlayerStandard.thumbImageView);

    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    /**
     * 新闻分享事件!
     */
    private static void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl QQ和QQ空间跳转链接
        //oks.setTitleUrl("");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(videoTitle);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(imageUrl);
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(videoUrl);
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(mContext);
    }
}
