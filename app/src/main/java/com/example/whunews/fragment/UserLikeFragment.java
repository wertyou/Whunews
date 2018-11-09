package com.example.whunews.fragment;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.whunews.R;
import com.example.whunews.adapter.LikeAdapter;
import com.example.whunews.bean.Likes;
import com.example.whunews.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Author Administrator
 * Created on 2018/3/6 0006
 * Goal :
 */

public class UserLikeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private SwipeRefreshLayout swipe;
    private Context context;
    private LikeAdapter adapter;
    private final List<Likes> likesList = new ArrayList<>();
    private final User currentUser = BmobUser.getCurrentUser(User.class);
    private TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_data, container, false);
        initView();
        initLikesData();
        userLikeRecycler();
        return view;
    }

    /**
     * 初始化View
     */
    private void initView() {
        TextView title = view.findViewById(R.id.user_title);
        title.setText(R.string.like);
        tv = view.findViewById(R.id.no_title);
        ImageView back = view.findViewById(R.id.user_back);
        back.setOnClickListener(this);
        swipe = view.findViewById(R.id.user_refresh);
        swipe.setColorSchemeResources(R.color.colorPrimary);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLikesData();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initLikesData() {
        if (currentUser != null) {
            requestLikesData();
        } else {
            showView(R.string.first_login);
        }
    }

    /**
     * 请求数据
     */
    private void requestLikesData() {
        likesList.clear();
        BmobQuery<Likes> query = new BmobQuery<>();
        query.addWhereEqualTo("likeAuthor", currentUser);
        query.order("-createdAt");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<Likes>() {
            @Override
            public void done(List<Likes> list, BmobException e) {
                if (e == null) {
                    if (list.size()==0){
                        showView(R.string.no_like_data);
                    }else {
                        for (Likes likes : list) {
                            String title = likes.getTitle();
                            String time = likes.getTime();
                            String author = likes.getAuthor();
                            String imageUrl = likes.getImageUrl();
                            String content = likes.getContent();
                            Likes mLikes = new Likes(title, author, imageUrl, time, content);
                            likesList.add(mLikes);
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), R.string.new_data, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showView(R.string.data_error);
                }
            }
        });
    }

    /**
     * 显示提示内容
     * @param resId  资源名称
     */
    private void showView(int resId){
        tv.setVisibility(View.VISIBLE);
        tv.setText(resId);
    }

    /**
     * 设置Adapter
     */
    private void userLikeRecycler() {
        RecyclerView recycler = view.findViewById(R.id.user_recycler);
        recycler.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycler.setLayoutManager(layoutManager);
        adapter = new LikeAdapter(likesList);
        recycler.setAdapter(adapter);
    }

    /**
     * 刷新数据
     */
    private void refreshLikesData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initLikesData();
                swipe.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_back:
                getActivity().onBackPressed();
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
