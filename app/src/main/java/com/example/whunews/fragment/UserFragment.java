package com.example.whunews.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.whunews.R;
import com.example.whunews.activity.EditDataActivity;
import com.example.whunews.activity.UserActivity;
import com.example.whunews.activity.UserLoginActivity;
import com.example.whunews.activity.WeatherActivity;
import com.example.whunews.bean.User;
import com.example.whunews.common.Common;
import com.example.whunews.common.PopupWindowFromBottom;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class UserFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView login_user_name;
    private CircleImageView login_user_image;
    private TextView location;
    private TextView weather;
    private LocationClient mLocationClient = null;
    private final MyLocationListener myLocationListener = new MyLocationListener();
    private String detailAddress;
    private final int WEATHER = 1;
    private final int EDIT_DATA = 2;
    private static final int TAKE_PHOTO = 3;
    private static final int CHOOSE_PHOTO = 4;
    private final int LOGIN = 5;
    private PopupWindowFromBottom popupWindowFromBottom;
    private WindowManager.LayoutParams params;
    private String path;
    private User currentUser;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLocationClient = new LocationClient(getContext());
        //noinspection deprecation
        mLocationClient.registerLocationListener(myLocationListener);
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initView();
        initUserInfo();
        initWeatherDegree();
        showLocation();
        return view;
    }

    /**
     * 初始化View
     */
    private void initView() {
        ImageView user_like_image = view.findViewById(R.id.user_like_image);
        ImageView user_comment_image = view.findViewById(R.id.user_comment_image);
        ImageView user_message_image = view.findViewById(R.id.user_message_image);
        LinearLayout update = view.findViewById(R.id.update);
        LinearLayout edit = view.findViewById(R.id.edit);
        LinearLayout login_out = view.findViewById(R.id.login_out);
        login_user_name = view.findViewById(R.id.user_login_name);
        login_user_image = view.findViewById(R.id.user_login_image);
        location = view.findViewById(R.id.user_location);
        weather = view.findViewById(R.id.user_weather);

        login_user_image.setOnClickListener(this);
        login_user_name.setOnClickListener(this);
        user_like_image.setOnClickListener(this);
        user_comment_image.setOnClickListener(this);
        user_message_image.setOnClickListener(this);
        edit.setOnClickListener(this);
        update.setOnClickListener(this);
        login_out.setOnClickListener(this);
        location.setOnClickListener(this);
        weather.setOnClickListener(this);
    }

    /**
     * 初始化用户信息
     */
    private void initUserInfo() {
        getCurrentUser();
        String photoImage = new Common().imagePath();
        if (currentUser != null) {
            String userName = (String) BmobUser.getObjectByKey("username");
            login_user_name.setText(userName);
            String photoPath = (String) BmobUser.getObjectByKey("userPhoto");
            if (photoPath != null) {
                Glide.with(getActivity()).load(photoPath).into(login_user_image);
            } else {
                Glide.with(getActivity()).load(photoImage).into(login_user_image);
            }
        } else {
            login_user_name.setText(R.string.login);
            Glide.with(getActivity()).load(photoImage).into(login_user_image);
        }
    }

    /**
     * 获取当前用户
     */
    private void getCurrentUser() {
        currentUser = BmobUser.getCurrentUser(User.class);
    }

    /**
     * 初始化WeatherDegree
     */
    private void initWeatherDegree() {
        SharedPreferences pref = getActivity().getSharedPreferences("degree", Context.MODE_PRIVATE);
        String degree = pref.getString("degree", "20℃");
        weather.setText(degree);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_login_image:
                showPopupWindow();
                break;
            case R.id.user_login_name:
                Intent loginIntent = new Intent(getActivity(), UserLoginActivity.class);
                startActivityForResult(loginIntent, LOGIN);
                break;
            case R.id.user_like_image:
                String LIKE = "LIKE";
                startTheActivity(LIKE);
                break;
            case R.id.user_message_image:
                String MESSAGE = "MESSAGE";
                startTheActivity(MESSAGE);
                break;
            case R.id.user_comment_image:
                String COMMENT = "COMMENT";
                startTheActivity(COMMENT);
                break;
            case R.id.edit:
                Intent editIntent = new Intent(getActivity(), EditDataActivity.class);
                startActivityForResult(editIntent, EDIT_DATA);
                break;
            case R.id.update:
                showUpdateDialog();
                break;
            case R.id.login_out:
                showLoginOutDialog();
                break;
            case R.id.user_location:
                //TODO 显示地图位置
                break;
            case R.id.user_weather:
                String weatherId = "CN101200101";
                Intent weatherIntent = new Intent(getActivity(), WeatherActivity.class);
                weatherIntent.putExtra("weather_id", weatherId);
                startActivityForResult(weatherIntent, WEATHER);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences.Editor editor = null;
        switch (requestCode) {
            case WEATHER:
                if (resultCode == RESULT_OK) {
                    String returnDegree = data.getStringExtra("degree");
                    editor = getActivity().getSharedPreferences(
                            "degree", Context.MODE_PRIVATE).edit();
                    editor.putString("degree", returnDegree);
                    weather.setText(returnDegree);
                }
                break;
            case EDIT_DATA:
                initUserInfo();
                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (path != null) {
                        //Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));   用Bitmap会出现Bug
                        File takePhotoFile = new File(path);

                        //使用SharedPreferences持久化存储数据!
                        editor = getActivity().getSharedPreferences(
                                "userPhoto", Context.MODE_PRIVATE).edit();
                        editor.putString("userPath", path);
                        editor.apply();
                        Glide.with(getActivity()).load(takePhotoFile).into(login_user_image);
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
            case LOGIN:
                initUserInfo();
                break;
            default:
                break;
        }
        if (editor != null) {
            editor.apply();
        }
    }

    /**
     * 退出登录
     */
    private void loginOut() {
        if (currentUser == null) {
            Toast.makeText(getActivity(), R.string.no_login, Toast.LENGTH_SHORT).show();
        } else {
            BmobUser.logOut();
            initUserInfo();
            Toast.makeText(getActivity(), R.string.login_out_toast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 更新对话框！
     */
    private void showUpdateDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(R.string.version_update);
        dialog.setMessage(R.string.update_message);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    /**
     * 退出对话框
     */
    private void showLoginOutDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(R.string.login_out);
        dialog.setMessage(R.string.is_login_out);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.dialog_sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loginOut();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    /**
     * 分装开启Activity
     *
     * @param data 传递过去的数据
     */
    private void startTheActivity(String data) {
        Intent intent = new Intent(getContext(), UserActivity.class);
        intent.putExtra("type", data);
        startActivity(intent);
    }


    /**
     * 加载地理位置
     */
    private void showLocation() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            requestLocation();
        }
    }

    /**
     * 发起请求定位
     */
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(10000);
        //option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(getActivity(), "发生位置错误", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            case 2:
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

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder address = new StringBuilder();
            String currentPosition = bdLocation.getCity();
            address.append(bdLocation.getDistrict());
            address.append(bdLocation.getStreet());
            detailAddress = String.valueOf(address);

            //字符串截取处理   去掉xx市中的市
            String position = currentPosition.substring(0, currentPosition.indexOf("市"));
            location.setText(position);


        }
    }

    /**
     * 公开详细地址
     */
    public String getAddress() {
        return detailAddress;
    }

    /**
     * 弹出底部对话框
     */
    private void showPopupWindow() {
        popupWindowFromBottom = new PopupWindowFromBottom(getActivity(), itemsOnClick);
        popupWindowFromBottom.showAtLocation(getActivity().findViewById(R.id.fragment_user),
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

    /**
     * 拍照  获取图片
     */
    private void takePhoto() {
        String mFilePath = Environment.getExternalStorageDirectory().getPath() + "/";
        //将当前的拍照时间作为图片的文件名称
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String filename = simpleDateFormat.format(new Date()) + ".jpg";
        path = mFilePath + filename;
        // 将路径存入到数据库
        uploadImage(path);
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

    /**
     * 从相册选取照片
     */
    private void selectPhoto() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
            //将路径插入数据库
            if (imagePath != null) {
                uploadImage(imagePath);
            }
            //显示图片
            displayImage(imagePath);
        }
    }

    /**
     * 上传图片到后台文件
     *
     * @param path 图片路径
     */
    private void uploadImage(String path) {
        if (currentUser != null) {
            final BmobFile bmobFile = new BmobFile(new File(path));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //删除原来的图片数据  考虑到速度问题，暂时放弃
                        //deleteImage();
                        //保存绝对路径到数据库字段
                        currentUser.setUserPhoto(bmobFile.getFileUrl());
                        currentUser.update(currentUser.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e != null) {
                                    Toast.makeText(getActivity(), "上传数据失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "上传文件失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.first_login, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 更换头像背景时，先删除该图片
     */
    private void deleteImage() {
        if (currentUser != null) {
            String userPhoto = (String) BmobUser.getObjectByKey("userPhoto");
            if (userPhoto != null) {
                BmobFile file = new BmobFile();
                file.setUrl(userPhoto);
                file.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
//                        if(e==null){
//                            Toast.makeText(getActivity(),"文件删除成功",Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(getActivity(),"文件删除失败",Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
            }
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

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
                    "userPhoto", Context.MODE_PRIVATE).edit();
            editor.putString("userPath", imagePath);
            editor.apply();
            Glide.with(getActivity()).load(selectPhotoFile).into(login_user_image);
        } else {
            Toast.makeText(getActivity(), "fail to get image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}
