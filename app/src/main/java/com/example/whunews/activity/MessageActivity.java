package com.example.whunews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whunews.R;
import com.example.whunews.bean.Message;
import com.example.whunews.bean.User;
import com.example.whunews.common.Common;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * Author Administrator
 * Created on 2018/4/4 0004
 * Goal :
 */

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText messageContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        TextView cancel = findViewById(R.id.cancel_message);
        TextView send = findViewById(R.id.send_message);
        messageContent = findViewById(R.id.message_content);

        cancel.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    /**
     * 发表动态
     */
    private  void sendMessage(){
        User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null){
            String userPhoto = (String) BmobUser.getObjectByKey("userPhoto");
            String userName = (String) BmobUser.getObjectByKey("username");
            String sendTime = new Common().getCurrentTime();
            String sendContent=messageContent.getText().toString();
            Message message = new Message();
            message.setUserPhoto(userPhoto);
            message.setUserName(userName);
            message.setSendTime(sendTime);
            message.setSendContent(sendContent);
            message.setAuthor(currentUser);
            message.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        showToast(R.string.send_success);
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    }else {
                        showToast(R.string.send_error);
                    }
                }
            });
        }else {
            showToast(R.string.first_login);
        }
    }

    /**
     * 封装Toast
     */
    private void showToast(int resId){
        Toast.makeText(MessageActivity.this,resId,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_message:
                finish();
                break;
            case R.id.send_message:
                sendMessage();
                break;
            default:
                break;
        }
    }
}
