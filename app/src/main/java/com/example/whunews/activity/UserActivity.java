package com.example.whunews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.whunews.R;
import com.example.whunews.fragment.UserFeedbackFragment;
import com.example.whunews.fragment.UserLikeFragment;
import com.example.whunews.fragment.UserLoginFragment;
import com.example.whunews.fragment.UserMessageFragment;


public class UserActivity extends AppCompatActivity {


    private FragmentManager fragmentManager;
    private UserLoginFragment userLoginFragment;
    private UserLikeFragment userLikeFragment;
    private UserFeedbackFragment userFeedbackFragment;
    private UserMessageFragment userMessageFragment;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        replaceLayout();
    }

    /**
     * 初始化组件！
     */
    private void initView() {
        fragmentManager = getSupportFragmentManager();
        userLoginFragment = new UserLoginFragment();
        userLikeFragment = new UserLikeFragment();
        userFeedbackFragment = new UserFeedbackFragment();
        userMessageFragment = new UserMessageFragment();
    }

    /**
     * 根据不同的参数跳转不同的页面布局！
     */
    private void replaceLayout() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        //判断跳转！
        switch (type) {
            case "LOGIN":
                replaceFragment(userLoginFragment);
                break;
            case "LIKE":
                replaceFragment(userLikeFragment);
                break;
            case "MESSAGE":
                replaceFragment(userMessageFragment);
                break;
            case "COMMENT":
                replaceFragment(userFeedbackFragment);
                break;
            default:
                break;
        }
    }

    /**
     * 替换碎片
     *
     * @param fragment 需要替换的碎片
     */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.user_fragment, fragment);
        transaction.commit();
    }


}
