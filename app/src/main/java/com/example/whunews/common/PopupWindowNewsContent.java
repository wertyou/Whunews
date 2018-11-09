package com.example.whunews.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.whunews.R;

/**
 * Author Administrator
 * Created on 2018/4/6 0006
 * Goal :
 */

public class PopupWindowNewsContent extends PopupWindow {
    @SuppressLint("InflateParams")
    public PopupWindowNewsContent(Activity context, View.OnClickListener itemsOnclick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = null;
        if (inflater != null) {
            mMenuView = inflater.inflate(R.layout.popup_comment, null);
        }
        //设置PopupWindow的View
        this.setContentView(mMenuView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.Animation);
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        TextView send,cancel;
        EditText comment;
        if (mMenuView != null) {
            send = mMenuView.findViewById(R.id.send);
            cancel=mMenuView.findViewById(R.id.cancel);
            comment = mMenuView.findViewById(R.id.news_comment);

            send.setOnClickListener(itemsOnclick);
            cancel.setOnClickListener(itemsOnclick);
            comment.setOnClickListener(itemsOnclick);
        }
    }
}
