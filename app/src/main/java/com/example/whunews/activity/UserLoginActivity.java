package com.example.whunews.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whunews.R;
import com.example.whunews.bean.User;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Author Administrator
 * Created on 2018/4/6 0006
 * Goal :
 */

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user_phone, identifyCode;
    private TextView send_message;
    private String phoneNumber, messageContent;
    private CountDownTimer timer;
    private int REQUEST_SMS_PERMISSION;
   // private int randomNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        initView();
        checkSendMessagePermission();
        countDownTime();
        //messageContent();
    }

    private void initView() {
        user_phone = findViewById(R.id.user_phone);
        identifyCode = findViewById(R.id.user_identifying_code);
        send_message = findViewById(R.id.user_send_message);
        Button user_login = findViewById(R.id.user_login);
        ImageView back = findViewById(R.id.login_back);

        send_message.setOnClickListener(this);
        user_login.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    /**
     * 发送短信的权限！
     */
    @SuppressWarnings("StatementWithEmptyBody")
    private void checkSendMessagePermission() {
        //检查是否拥有发送短信的权限
        if (ContextCompat.checkSelfPermission(UserLoginActivity.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            //没有权限，申请权限
            ActivityCompat.requestPermissions(UserLoginActivity.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_SMS_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限处理事情
                Toast.makeText(UserLoginActivity.this, R.string.grant_yes, Toast.LENGTH_SHORT).show();
            } else {
                //拒绝权限
                Toast.makeText(UserLoginActivity.this, R.string.grant_no, Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 验证码计时功能!
     */
    private void countDownTime() {
        timer = new CountDownTimer(20000, 1000) {
            @SuppressLint("NewApi")
            @Override
            public void onTick(long millisUntilFinished) {
                //noinspection deprecation
                send_message.setBackground(getResources().getDrawable(R.drawable.send_message_bg_gray));
                String second = getResources().getString(R.string.second);
                send_message.setText(String.format(second, millisUntilFinished / 1000));
                send_message.setEnabled(false);
            }

            @SuppressLint("NewApi")
            @Override
            public void onFinish() {
                send_message.setEnabled(true);
                //noinspection deprecation
                send_message.setText(R.string.send_again);
                send_message.setBackground(getResources().getDrawable(R.drawable.send_message_bg_blue));
            }
        };
    }

//    /**
//     * 生成6位随机验证码！！！
//     */
//    private void messageContent() {
//        randomNumber = (int) ((Math.random() * 9 + 1) * 100000);
//        String content = getResources().getString(R.string.message_content);
//        messageContent = String.format(content, randomNumber);
//    }

    /**
     * 验证电话号码！
     */
    private void checkUpPhoneNumber() {
        phoneNumber = user_phone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(UserLoginActivity.this, R.string.input_phone, Toast.LENGTH_SHORT).show();
        }
        if (phoneNumber.length() != 11) {
            user_phone.setError(String.valueOf(R.string.not_exit_number));
        } else {
            //sendMessage();
            requestSMSCode();
            Toast.makeText(UserLoginActivity.this, R.string.send_message_success, Toast.LENGTH_SHORT).show();
            timer.start();
        }
    }

    /**
     * 请求短信验证码
     */
    private void requestSMSCode() {
        BmobSMS.requestSMSCode(phoneNumber, "whunews", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                Toast.makeText(UserLoginActivity.this, R.string.send_success, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void oneKeyLogin() {
        String code = identifyCode.getText().toString();
        if (!TextUtils.isEmpty(code)) {
            User.signOrLoginByMobilePhone(phoneNumber, code, new LogInListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (user != null) {
                        Toast.makeText(UserLoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();
                    } else {
                        Toast.makeText(UserLoginActivity.this, R.string.id_code_error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(UserLoginActivity.this,R.string.input_id,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 发送短信！
     */
    @SuppressWarnings("deprecation")
    private void sendMessage() {
        PendingIntent pi = PendingIntent.getActivity(UserLoginActivity.this, 0, new Intent(), 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, messageContent, pi, null);
    }

//    /**
//     * 登录！
//     */
//    private void login() {
//        String stringRandomNumber = String.valueOf(randomNumber);
//        String idNumber = identifyCode.getText().toString();
//        if (idNumber.equals(stringRandomNumber)) {
//            Toast.makeText(UserLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//            sendUserPhone();
//        } else {
//            identifyCode.setError(String.valueOf(R.string.id_code_error));
//        }
//    }

//    /**
//     * 传送用户信息
//     */
//    private void sendUserPhone() {
//        String phone = String.valueOf(user_phone.getText());
//        Intent intent = new Intent();
//        intent.putExtra("phone", phone);
//        setResult(RESULT_OK, intent);
//        finish();
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_send_message:
                checkUpPhoneNumber();
                break;
            case R.id.user_login:
                //login();
                oneKeyLogin();
                break;
            case R.id.login_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

}
