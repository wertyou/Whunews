package com.example.whunews.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.whunews.R;
import com.example.whunews.common.Key;
import com.example.whunews.fragment.MessageFragment;
import com.example.whunews.fragment.NewsFragment;
import com.example.whunews.fragment.UserFragment;
import com.example.whunews.fragment.VideoFragment;

import cn.bmob.v3.Bmob;


public class MainActivity extends AppCompatActivity {

    //声明fragment
    private FragmentManager fragmentManager;
    private NewsFragment newsFragment;
    private VideoFragment videoFragment;
    private MessageFragment messageFragment;
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        replaceFragment(newsFragment);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        //初始化Bmob
        Bmob.initialize(this, new Key().getBmobKey());
        //初始化view
        BottomNavigationView navigation = findViewById(R.id.navigation);
        //初始化fragment
        fragmentManager = getSupportFragmentManager();
        newsFragment = new NewsFragment();
        videoFragment = new VideoFragment();
        messageFragment = new MessageFragment();
        userFragment = new UserFragment();
        //初始化监听事件
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * 动态替换碎片！
     *
     * @param fragment 待替换的碎片！
     */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_main_container, fragment);
        transaction.commit();
    }

    /**
     * 菜单监听事件
     */
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_news:
                    replaceFragment(newsFragment);
                    return true;
                case R.id.navigation_video:
                    replaceFragment(videoFragment);
                    return true;
                case R.id.navigation_message:
                    replaceFragment(messageFragment);
                    return true;
                case R.id.navigation_user:
                    replaceFragment(userFragment);
                    return true;
            }
            return false;
        }
    };

}
