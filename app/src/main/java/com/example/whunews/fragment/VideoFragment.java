package com.example.whunews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.whunews.adapter.VideoAdapter;
import com.example.whunews.R;
import com.example.whunews.bean.Video;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


public class VideoFragment extends Fragment {
    private View view;
    private VideoAdapter adapter;
    private final List<Video> videoList = new ArrayList<>();
    private SwipeRefreshLayout videoRefresh;
    private android.content.Context context;
    private AVLoadingIndicatorView videoAvi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        initView();
        initVideoData();
        videoRecycler();
        return view;
    }

    /**
     * 初始化View
     */
    private void initView() {
        videoRefresh = view.findViewById(R.id.video_refresh);
        videoAvi = view.findViewById(R.id.videoAvi);
        videoRefresh.setColorSchemeResources(R.color.colorPrimary);
        videoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshVideo();
            }
        });
    }

    /**
     * 初始化video数据
     */
    private void initVideoData() {
        requestVideoData(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
    }

    /**
     * 请求video数据
     *
     * @param cachePolicy 缓存方案
     */
    private void requestVideoData(BmobQuery.CachePolicy cachePolicy) {
        startLoad();
        videoList.clear();
        BmobQuery<Video> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setCachePolicy(cachePolicy);
        query.findObjects(new FindListener<Video>() {
            @Override
            public void done(List<Video> list, BmobException e) {
                if (e == null) {
                    for (Video video : list) {
                        String videoUrl = video.getVideoUrl();
                        String videoAuthor = video.getVideoAuthor();
                        String videoTitle = video.getVideoTitle();
                        String videoImage = video.getVideoImage();
                        Video myVideo = new Video(videoUrl, videoAuthor, videoTitle, videoImage);
                        videoList.add(myVideo);
                    }
                    stopLoad();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), R.string.data_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 刷新video
     */
    private void refreshVideo() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestVideoData(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                videoRefresh.setRefreshing(false);
                Toast.makeText(getActivity(), R.string.new_data, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void videoRecycler() {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_video);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VideoAdapter(videoList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 开启加载动画
     */
    private void startLoad() {
        videoAvi.show();
    }

    /**
     * 关闭加载动画
     */
    private void stopLoad() {
        videoAvi.hide();
    }


    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        stopLoad();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
        stopLoad();
    }

}
