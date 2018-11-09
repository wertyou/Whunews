package com.example.whunews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whunews.R;
import com.example.whunews.activity.NewsDetailActivity;
import com.example.whunews.bean.News;

import java.util.List;

/**
 * Author Administrator
 * Created on 2018/3/4 0004
 * Goal :
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final List<News> mNewsList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View newsView;
        final TextView title;
        final TextView author;
        final TextView time;
        final ImageView newsImage;

        private ViewHolder(View view) {
            super(view);
            newsView = view;
            title = view.findViewById(R.id.news_title);
            author = view.findViewById(R.id.news_author);
            newsImage = view.findViewById(R.id.news_image);
            time = view.findViewById(R.id.news_time);
        }
    }

    public NewsAdapter(List<News> newsList) {
        mNewsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                News news = mNewsList.get(position);
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.NEWS_TITLE, news.getTitle());
                intent.putExtra(NewsDetailActivity.NEWS_AUTHOR, news.getAuthor());
                intent.putExtra(NewsDetailActivity.NEWS_TIME, news.getTime());
                intent.putExtra(NewsDetailActivity.NEWS_CONTENT, news.getContent());
                intent.putExtra(NewsDetailActivity.NEWS_IMAGE,news.getImageUrl());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mNewsList.get(position);
        holder.title.setText(news.getTitle());
        holder.author.setText(news.getAuthor());
        holder.time.setText(news.getTime());
        Glide.with(mContext).load(news.getImageUrl()).into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}
