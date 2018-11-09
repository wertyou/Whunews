package com.example.whunews.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.example.whunews.R;

/**
 * Author Administrator
 * Created on 2018/3/13 0013
 * Goal :
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_welcome);

        welcome();
    }

    /**
     * 欢迎界面！
     */
    private void welcome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    /**
     * 在延迟跳转中,自动屏蔽后退键！
     *
     * @param keyCode 后退键
     * @param event   事件
     * @return true or false
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 || super.onKeyDown(keyCode, event);
    }
}
