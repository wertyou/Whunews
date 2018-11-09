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
import com.example.whunews.bean.Likes;

import java.util.List;

/**
 * Author Administrator
 * Created on 2018/5/16 0016
 * Goal :
 */
public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder>{
    private final List<Likes> mLikesList;
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

    public LikeAdapter(List<Likes> newsList) {
        mLikesList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        final ViewHolder holder = new LikeAdapter.ViewHolder(view);
        holder.newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Likes likes = mLikesList.get(position);
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.NEWS_TITLE, likes.getTitle());
                intent.putExtra(NewsDetailActivity.NEWS_AUTHOR, likes.getAuthor());
                intent.putExtra(NewsDetailActivity.NEWS_TIME, likes.getTime());
                intent.putExtra(NewsDetailActivity.NEWS_CONTENT, likes.getContent());
                intent.putExtra(NewsDetailActivity.NEWS_IMAGE,likes.getImageUrl());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Likes likes = mLikesList.get(position);
        holder.title.setText(likes.getTitle());
        holder.author.setText(likes.getAuthor());
        holder.time.setText(likes.getTime());
        Glide.with(mContext).load(likes.getImageUrl()).into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return mLikesList.size();
    }
}
