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
import com.example.whunews.adapter.MessageAdapter;
import com.example.whunews.bean.Message;
import com.example.whunews.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Author Administrator
 * Created on 2018/3/16 0016
 * Goal :
 */

public class UserMessageFragment extends Fragment implements View.OnClickListener {
    private View view;
    private SwipeRefreshLayout refresh;
    private MessageAdapter adapter;
    private final List<Message> userMessageList = new ArrayList<>();
    private Context context;
    private TextView message;
    private final User currentUser = BmobUser.getCurrentUser(User.class);
    private TextView tv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_data, container, false);
        initView();
        initUserMessageData();
        userMessageRecycler();
        return view;
    }

    /**
     * 初始化View
     */
    private void initView() {
        TextView title = view.findViewById(R.id.user_title);
        title.setText(R.string.message);
        tv = view.findViewById(R.id.no_title);
        ImageView back = view.findViewById(R.id.user_back);
        back.setOnClickListener(this);
        refresh = view.findViewById(R.id.user_refresh);
        //对刷新设置
        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMessage();
            }
        });
    }

    /**
     * 刷新用户动态
     */
    private void refreshMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initUserMessageData();
                refresh.setRefreshing(false);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initUserMessageData() {
        if (currentUser != null) {
            requestMessageData();
        } else {
            showView(R.string.first_login);
        }
    }

    /**
     * 请求用户动态数据
     */
    private void requestMessageData() {
        userMessageList.clear();
        BmobQuery<Message> query = new BmobQuery<>();
        query.addWhereEqualTo("author", currentUser);
        query.order("-createdAt");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        showView(R.string.no_message_data);
                    } else {
                        for (Message message : list) {
                            String userPhoto = message.getUserPhoto();
                            String userName = message.getUserName();
                            String sendTime = message.getSendTime();
                            String sendContent = message.getSendContent();
                            Message myMessage = new Message(userName, userPhoto, sendTime, sendContent);
                            userMessageList.add(myMessage);
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
     *
     * @param resId 资源Id
     */
    private void showView(int resId) {
        tv.setVisibility(View.VISIBLE);
        tv.setText(resId);
    }

    /**
     * 对recycler的设置
     */
    private void userMessageRecycler() {
        RecyclerView recycler = view.findViewById(R.id.user_recycler);
        recycler.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycler.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(userMessageList);
        recycler.setAdapter(adapter);

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
