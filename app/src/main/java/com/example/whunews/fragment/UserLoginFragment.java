package com.example.whunews.fragment;

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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whunews.R;


public class UserLoginFragment extends Fragment implements View.OnClickListener {

    private EditText user_phone, identifyCode;
    private TextView send_message;
    private String phoneNumber, messageContent;
    private CountDownTimer timer;
    private int REQUEST_SMS_PERMISSION;
    private int randomNumber;
    private View view;
    private Boolean userLogin = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_user_login, container, false);
        initView();
        checkSendMessagePermission();
        countDownTime();
        messageContent();
        return view;
    }

    private void initView() {
        user_phone = view.findViewById(R.id.user_phone);
        identifyCode = view.findViewById(R.id.user_identifying_code);
        send_message = view.findViewById(R.id.user_send_message);
        Button user_login = view.findViewById(R.id.user_login);

        send_message.setOnClickListener(this);
        user_login.setOnClickListener(this);
    }

    /**
     * 发送短信的权限！
     */
    @SuppressWarnings("StatementWithEmptyBody")
    private void checkSendMessagePermission() {
        //检查是否拥有发送短信的权限
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            //没有权限，申请权限
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_SMS_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限处理事情
                Toast.makeText(getActivity(), R.string.grant_yes, Toast.LENGTH_SHORT).show();
            } else {
                //拒绝权限
                Toast.makeText(getActivity(), R.string.grant_no, Toast.LENGTH_SHORT).show();
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

    /**
     * 生成6位随机验证码！！！
     */
    private void messageContent() {
        randomNumber = (int) ((Math.random() * 9 + 1) * 100000);
        String content = getResources().getString(R.string.message_content);
        messageContent = String.format(content, randomNumber);
    }

    /**
     * 验证电话号码！
     */
    private void checkUpPhoneNumber() {
        phoneNumber = user_phone.getText().toString();
        //注意在Fragment和Activity中，Toast使用区别
        if (phoneNumber.length() != 11) {
            user_phone.setError(String.valueOf(R.string.not_exit_number));
        } else {
            sendMessage();
            Toast.makeText(getActivity(), R.string.send_success, Toast.LENGTH_SHORT).show();
            timer.start();
        }
    }

    /**
     * 发送短信！
     */
    @SuppressWarnings("deprecation")
    private void sendMessage() {
        PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, new Intent(), 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, messageContent, pi, null);
    }

    /**
     * 登录！
     */
    private void login() {
        String stringRandomNumber = String.valueOf(randomNumber);
        String idNumber = identifyCode.getText().toString();
        if (idNumber.equals(stringRandomNumber)) {
            //TODO 将数据和数据库打交道！
            Toast.makeText(getActivity(), R.string.login_success, Toast.LENGTH_SHORT).show();
            userLogin = true;
            getActivity().finish();

        } else {
            identifyCode.setError(String.valueOf(R.string.id_code_error));
        }
    }

//    /**
//     * 得到用户的电话号码
//     * @return   null or phone
//     */
//    public String getUserPhone() {
//        if (userLogin) {
//            return String.valueOf(user_phone.getText());
//        }else {
//            return null;
//        }
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
                login();
                break;
            default:
                break;
        }
    }


}
