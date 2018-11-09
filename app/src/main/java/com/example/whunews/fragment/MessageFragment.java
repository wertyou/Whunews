package com.example.whunews.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whunews.R;
import com.example.whunews.activity.MessageActivity;
import com.example.whunews.adapter.MessageAdapter;
import com.example.whunews.bean.Message;

import com.example.whunews.bean.User;
import com.example.whunews.common.Common;
import com.example.whunews.common.PopupWindowFromBottom;
import com.wang.avi.AVLoadingIndicatorView;


import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class MessageFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ImageView bgImage;
    private SwipeRefreshLayout swipe;
    private PopupWindowFromBottom popupWindowFromBottom;
    private WindowManager.LayoutParams params;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private static final int MESSAGE_CONTENT = 3;
    private String path;
    private final String messagePhoto = "messagePhoto";
    private Context context;
    private final List<Message> messageList = new ArrayList<>();
    private MessageAdapter adapter;
    private LinearLayout messageBottom;
    //TODO 解决点击外部关闭弹出框  屏幕亮度仍未0.7的bug
    private Boolean isDark = false;
    private final User currentUser = BmobUser.getCurrentUser(User.class);
    private CircleImageView userPhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        initView();
        initImageData();
        //initMessageData();
        requestMessageData(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        messageRecycler();
        return view;
    }

    /**
     * 初始化View
     */
    private void initView() {
        userPhoto = view.findViewById(R.id.message_photo);

        bgImage = view.findViewById(R.id.message_background_image);
        messageBottom = view.findViewById(R.id.message_bottom);
        swipe = view.findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.colorPrimary);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMessage();
            }
        });
        FloatingActionButton fab = view.findViewById(R.id.message_fab);
        AVLoadingIndicatorView avi = view.findViewById(R.id.avi);
        fab.setOnClickListener(this);
        bgImage.setOnClickListener(this);
    }

    /**
     * 初始化图片
     */
    private void initImageData() {

        //初始化背景图片
        //先从SharedPreferences 中读取数据
        SharedPreferences pref = getActivity().getSharedPreferences(messagePhoto, Context.MODE_PRIVATE);
        String mPath = pref.getString("messagePath",null);

        //如果没有数据,就读取给定的默认值。
        if (mPath==null) {
            Glide.with(getActivity()).load(new Common().imagePath()).into(bgImage);
        } else {
            //如果有数据,就读取数据。
            File pathFile = new File(mPath);
            Glide.with(getActivity()).load(pathFile).into(bgImage);
        }

        //初始化用户头像图片
        initUserPhoto();
    }

    /**
     * 初始化用户头像图片
     */
    private void initUserPhoto(){
        if (currentUser!=null){
            String imagePath = (String) BmobUser.getObjectByKey("userPhoto");
            if (imagePath != null) {
                Glide.with(getActivity()).load(imagePath).into(userPhoto);
            }else {
                Glide.with(getActivity()).load(new Common().imagePath()).into(userPhoto);
            }

        }else {
            Glide.with(getActivity()).load(new Common().imagePath()).into(userPhoto);
        }
    }

    /**
     * 请求数据
     * @param cachePolicy 缓存策略
     */
    private void requestMessageData(BmobQuery.CachePolicy cachePolicy){
        messageList.clear();
        BmobQuery<Message> query = new BmobQuery<>();
        query.order("-createdAt");
        query.setCachePolicy(cachePolicy);
        query.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if (e == null){
                    for (Message message:list){
                        String userPhoto = message.getUserPhoto();
                        String userName = message.getUserName();
                        String sendTime = message.getSendTime();
                        String sendContent = message.getSendContent();
                        Message myMessage= new Message(userName,userPhoto,sendTime,sendContent);
                        messageList.add(myMessage);
                    }
                    adapter.notifyDataSetChanged();
                    messageBottom.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    /**
     * 设置Message的Adapter
     */
    private void messageRecycler() {
        RecyclerView messageRecycler = view.findViewById(R.id.message_recycler);
        messageRecycler.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        messageRecycler.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(messageList);
        messageRecycler.setAdapter(adapter);
    }


    /**
     * 弹出底部对话框
     */
    private void showPopupWindow() {
        popupWindowFromBottom = new PopupWindowFromBottom(getActivity(), itemsOnClick);
        popupWindowFromBottom.showAtLocation(getActivity().findViewById(R.id.fragment_message),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = getActivity().getWindow().getAttributes();
        params.alpha = 0.7f;
        getActivity().getWindow().setAttributes(params);
    }

    /**
     * 关闭底部对话框
     */
    private void closePopupWindow() {
        //保证点击按钮后关闭弹出框
        popupWindowFromBottom.dismiss();
        params = getActivity().getWindow().getAttributes();
        params.alpha = 1.0f;
        getActivity().getWindow().setAttributes(params);
    }


    /**
     * 从相册选取照片
     */
    private void selectPhoto() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);  //打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "you refuse the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }


    /**
     * 拍照  获取图片
     */
    private void takePhoto() {
        String mFilePath = Environment.getExternalStorageDirectory().getPath() + "/";
        //将当前的拍照时间作为图片的文件名称
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String filename = simpleDateFormat.format(new Date()) + ".jpg";
        path = mFilePath + filename;
        File file = new File(path);
        Uri photoURI;
        //解决三星7.x或其他7.x系列的手机拍照失败或应用崩溃的bug.解决4.2.2(oppo)/4.x系统拍照执行不到设置显示图片的bug
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { //7.0以下的系统用 Uri.fromFile(file)
            photoURI = Uri.fromFile(file);
        } else {                                            //7.0以上的系统用下面这种方案
            photoURI = FileProvider.getUriForFile(getActivity(), "com.example.whunews.fileprovider", file);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (path != null) {
                        //Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));   用Bitmap会出现Bug
                        File takePhotoFile = new File(path);

                        //使用SharedPreferences持久化存储数据!
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences(
                                messagePhoto, Context.MODE_PRIVATE).edit();
                        editor.putString("messagePath", path);
                        editor.apply();
                        Glide.with(getActivity()).load(takePhotoFile).into(bgImage);
                    }
                }
                break;

            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        //4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case MESSAGE_CONTENT:

                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            //如果是document类型的Uri,则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri != null ? uri.getAuthority() : null)) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection
                );
            } else if (uri != null) {
                if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);

                } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                    imagePath = getImagePath(uri, null);
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    imagePath = uri.getPath();
                }
            }
            displayImage(imagePath);
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 处理图片路径
     *
     * @param uri       uri
     * @param selection selection
     * @return path
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 显示图片
     *
     * @param imagePath 照片路径
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            //用Bitmap会出现严重的Bug   改用File了
            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            File selectPhotoFile = new File(imagePath);

            //使用SharedPreferences持久化存储数据!
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(
                    messagePhoto, Context.MODE_PRIVATE).edit();
            editor.putString("messagePath", imagePath);
            editor.apply();
            Glide.with(getActivity()).load(selectPhotoFile).into(bgImage);
        } else {
            Toast.makeText(getActivity(), "fail to get image", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 刷新动态!
     */
    private void refreshMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestMessageData(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                swipe.setRefreshing(false);
                Toast.makeText(getActivity(), R.string.new_data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_background_image:
                showPopupWindow();
                break;
            case R.id.swipe:
                refreshMessage();
                break;
            case R.id.message_fab:
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivityForResult(intent, MESSAGE_CONTENT);
                break;
            default:
                break;
        }
    }

    private final View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.take_photo:
                    takePhoto();
                    closePopupWindow();
                    break;
                case R.id.select_photo:
                    selectPhoto();
                    closePopupWindow();
                    break;
                case R.id.cancel:
                    closePopupWindow();
                    break;
                default:
                    break;
            }
        }
    };
}
