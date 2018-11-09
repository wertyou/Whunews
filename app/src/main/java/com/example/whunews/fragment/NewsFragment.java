package com.example.whunews.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.whunews.activity.SearchNewsActivity;
import com.example.whunews.adapter.NewsAdapter;
import com.example.whunews.R;
import com.example.whunews.bean.News;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class NewsFragment extends Fragment implements View.OnClickListener {

    private final List<News> newsList = new ArrayList<>();
    private Context context;
    private View view;
    private RollPagerView rollPagerView;
    private SwipeRefreshLayout newsRefresh;
    private NewsAdapter adapter;
    private AVLoadingIndicatorView newsAvi;
    private LinearLayout newsBottom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        initView();
        showAutoScroll();
        initNewsData();
        newsRecycler();
        return view;
    }

    /**
     * 初始化View
     */
    private void initView() {
        newsRefresh = view.findViewById(R.id.news_refresh);
        ImageView search = view.findViewById(R.id.search_news);
        newsAvi = view.findViewById(R.id.newsAvi);
        rollPagerView = view.findViewById(R.id.news_rollPager);
        newsBottom = view.findViewById(R.id.news_bottom);
        newsRefresh.setColorSchemeResources(R.color.colorPrimary);
        search.setOnClickListener(this);
        rollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), "Item " + position + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
        newsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });
    }

    /**
     * 刷新新闻
     */
    private void refreshNews() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //NETWORK_ELSE_CACHE:先从网络读取数据，如果没有，再从缓存中获取。
                requestNewsData(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
               // newsBottom.setVisibility(View.VISIBLE);
                newsRefresh.setRefreshing(false);
                Toast.makeText(getActivity(), R.string.new_data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化新闻数据
     */
    private void initNewsData() {
        //CACHE_ELSE_NETWORK:先从缓存读取数据，如果没有，再从网络获取。
        requestNewsData(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
    }

    /**
     * 请求数据
     *
     * @param cachePolicy 缓存方案
     */
    private void requestNewsData(BmobQuery.CachePolicy cachePolicy) {
        startLoad();
        newsList.clear();
        BmobQuery<News> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setCachePolicy(cachePolicy);
        query.findObjects(new FindListener<News>() {
            @Override
            public void done(List<News> list, BmobException e) {
                if (e == null) {
                    for (News news : list) {
                        String title = news.getTitle();
                        String time = news.getTime();
                        String author = news.getAuthor();
                        String imageUrl = news.getImageUrl();
                        String content = news.getContent();
                        News mNews = new News(title, author, imageUrl, time, content);
                        newsList.add(mNews);
                    }
                    stopLoad();
                    adapter.notifyDataSetChanged();
                    newsBottom.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), R.string.data_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 设置新闻adapter
     */
    private void newsRecycler() {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_news);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);
    }

    private void showAutoScroll() {
        rollPagerView.setPlayDelay(2000);
        rollPagerView.setAnimationDurtion(1000);
        rollPagerView.setHintView(new ColorPointHintView(getContext(), Color.YELLOW, Color.WHITE));
        rollPagerView.setAdapter(new rollAdapter());
    }

    private class rollAdapter extends StaticPagerAdapter {
        private final int[] imageResources = {
                R.mipmap.whu1,
                R.mipmap.whu2,
                R.mipmap.whu3,
        };

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setImageResource(imageResources[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        }

        @Override
        public int getCount() {
            return imageResources.length;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_news:
                Intent intent = new Intent(getActivity(), SearchNewsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 开启加载动画
     */
    private void startLoad() {
        newsAvi.show();
    }

    /**
     * 关闭加载动画
     */
    private void stopLoad() {
        newsAvi.hide();
    }

    @Override
    public void onPause() {
        super.onPause();
        rollPagerView.pause();
        stopLoad();
    }

    @Override
    public void onResume() {
        super.onResume();
        rollPagerView.resume();
        stopLoad();
    }

}
