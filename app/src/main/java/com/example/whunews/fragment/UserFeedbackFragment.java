package com.example.whunews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whunews.R;
import com.example.whunews.bean.Feedback;
import com.example.whunews.bean.User;
import com.example.whunews.common.Common;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Author Administrator
 * Created on 2018/3/6 0006
 * Goal :
 */

public class UserFeedbackFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView no_title;
    private EditText et;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_feedback_data, container, false);
        initView();
        return view;
    }

    /**
     * 初始化View
     */
    private void initView() {
        no_title = view.findViewById(R.id.no_title);
        ImageView back = view.findViewById(R.id.user_back);
        back.setOnClickListener(this);
        TextView sendFeedback = view.findViewById(R.id.send_feedback);
        sendFeedback.setOnClickListener(this);
        et = view.findViewById(R.id.feedback_content);
    }

    /**
     * 发送反馈意见
     */
    private void sendFeedback() {
        User currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null) {
            String sendTime = new Common().getCurrentTime();
            String sendContent = et.getText().toString();
            Feedback feedback = new Feedback();
            feedback.setSendTime(sendTime);
            feedback.setSendContent(sendContent);
            feedback.setAuthor(currentUser);
            feedback.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        showToast(R.string.send_success);
                        getActivity().onBackPressed();
                        getActivity().finish();
                    } else {
                        showToast(R.string.send_error);
                    }
                }
            });
        } else {
            showToast(R.string.first_login);
        }

    }

    /**
     * 显示界面提示
     *
     * @param resId 资源id
     */
    private void showView(int resId) {
        no_title.setVisibility(View.VISIBLE);
        no_title.setText(resId);
    }

    /**
     * 封装toast
     *
     * @param resId 资源id
     */
    private void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_back:
                getActivity().onBackPressed();
                getActivity().finish();
                break;
            case R.id.send_feedback:
                sendFeedback();
                break;
            default:
                break;
        }
    }
}
