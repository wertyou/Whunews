package com.example.whunews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.whunews.R;
import com.example.whunews.adapter.NewsAdapter;
import com.example.whunews.bean.News;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Author Administrator
 * Created on 2018/4/2 0002
 * Goal :  搜索新闻Activity
 */

public class SearchNewsActivity extends AppCompatActivity implements View.OnClickListener {

    private final List<News> newsList = new ArrayList<>();
    private EditText editSearch;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch_news);
        initView();
        searchNewsRecycler();
    }

    /**
     * 初始化View
     */
    private void initView() {
        ImageView back = findViewById(R.id.search_back);
        editSearch = findViewById(R.id.search_edit);
        ImageView search = findViewById(R.id.search_image);
        LinearLayout searchBottom = findViewById(R.id.search_bottom);

        back.setOnClickListener(this);
        search.setOnClickListener(this);

    }

    /**
     * 设置新闻adapter
     */
    private void searchNewsRecycler() {
        RecyclerView recyclerView = findViewById(R.id.news_search_recycler);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 搜索新闻
     */
    private void searchNews() {
        String searchText = editSearch.getText().toString();
        BmobQuery<News> query = new BmobQuery<>();
        query.addWhereContains("title", searchText);
        query.order("-createdAt");
        query.findObjects(new FindListener<News>() {
            @Override
            public void done(List<News> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        Toast.makeText(SearchNewsActivity.this, R.string.no_news, Toast.LENGTH_SHORT).show();
                    } else {
                        for (News news : list) {
                            String title = news.getTitle();
                            String time = news.getTime();
                            String author = news.getAuthor();
                            String imageUrl = news.getImageUrl();
                            String content = news.getContent();
                            News mNews = new News(title, author, imageUrl, time, content);
                            newsList.add(mNews);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(SearchNewsActivity.this, R.string.data_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                onBackPressed();
                break;
            case R.id.search_image:
                searchNews();
                break;
            default:
                break;
        }
    }

}
