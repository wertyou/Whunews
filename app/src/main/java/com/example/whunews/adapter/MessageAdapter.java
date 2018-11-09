package com.example.whunews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whunews.R;
import com.example.whunews.bean.Message;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author Administrator
 * Created on 2018/4/5 0005
 * Goal :
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private final List<Message> mMessageList;

    public MessageAdapter(List<Message> messageList) {
        mMessageList = messageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        holder.userName.setText(message.getUserName());
        holder.time.setText(message.getSendTime());
        holder.content.setText(message.getSendContent());
        Glide.with(context).load(message.getUserPhoto()).into(holder.userPhoto);

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View messageView;
        final TextView userName;
        final TextView time;
        final TextView content;
        final CircleImageView userPhoto;

        private ViewHolder(View view) {
            super(view);
            messageView = view;
            userName = view.findViewById(R.id.message_user_name);
            time = view.findViewById(R.id.message_send_time);
            content = view.findViewById(R.id.message_send_content);
            userPhoto = view.findViewById(R.id.message_user_photo);
        }
    }


}
