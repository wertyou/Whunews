package com.example.whunews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whunews.R;
import com.example.whunews.bean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Author Administrator
 * Created on 2018/5/6 0006
 * Goal :
 */
public class EditDataActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editUserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        initView();
    }

    /**
     * 初始化用户信息
     */
    private void initView() {
        ImageView back = findViewById(R.id.edit_back);
        editUserName = findViewById(R.id.edit_user_name);
        Button sure = findViewById(R.id.edit_sure);

        back.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    /**
     * 编辑用户昵称
     */
    private void editUserName() {
        User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null) {
            String userName = editUserName.getText().toString();
            if (!TextUtils.isEmpty(userName)) {
                User user = new User();
                user.setUsername(userName);
                user.update(currentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            //TODO 更新message表中改用户的名称。
                            finish();
                        } else {
                            showToast(R.string.update_name_error);
                        }
                    }
                });

            } else {
                showToast(R.string.nick_is_null);
            }
        } else {
            showToast(R.string.first_login);
        }
    }

    /**
     * 重写Toast
     *
     * @param info 显示文字
     */
    private void showToast(int info) {
        Toast.makeText(EditDataActivity.this, info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_back:
                onBackPressed();
                finish();
                break;
            case R.id.edit_sure:
                editUserName();
                break;
            default:
                break;
        }
    }
}
