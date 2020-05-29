package com.example.pinezone.ui.mine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pinezone.BasicActivity;
import com.example.pinezone.R;
import com.example.pinezone.config.GlideEngine;
import com.example.pinezone.config.GridImageAdapter;
import com.example.pinezone.config.ItemGroup;
import com.example.pinezone.ui.publish.PublishArticle;
import com.example.pinezone.ui.setting.SettingsActivity;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.makeramen.roundedimageview.RoundedImageView;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.mob.MobSDK.getContext;

public class MineInfo extends BasicActivity {
    private static final String TAG = "MineInfo";
    private SharedPreferences pref;
    private Context context;
    private ImageButton button;
    private SharedPreferences.Editor editor;
    private ItemGroup userName;
    private ItemGroup userProfile;
    private ItemGroup userSex;
    private ItemGroup userGrade;
    private ItemGroup userCity;
    RoundedImageView userImage;
    private AlertDialog sexDialog; //单选框
    private AlertDialog gradeDialog;
    private MineViewModel mineViewModel;

    private PictureParameterStyle mPictureParameterStyle;
    private PictureCropParameterStyle mCropParameterStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_info);
        initView();
        getWhiteStyle();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    Log.e("TAG", "onActivityResult: username" );
                    userName.getContentEdt().setText(data.getStringExtra("text"));
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    userProfile.getContentEdt().setText(data.getStringExtra("text"));
                }
                break;
        }
    }

    private void initView() {
        context = this;
        mineViewModel = new ViewModelProvider(this).get(MineViewModel.class);
        pref = getSharedPreferences("setting",MODE_PRIVATE);
        editor = pref.edit();
        userName = findViewById(R.id.name_ig);
        userProfile = findViewById(R.id.profile_ig);
        userSex = findViewById(R.id.select_sex_ig);
        userGrade = findViewById(R.id.grade_ig);
        userCity = findViewById(R.id.select_city_ig);
        userImage = findViewById(R.id.user_image);
        button = findViewById(R.id.changeImage);
//
//        editor.putInt("id",user.getId());
//        editor.putString("name",user.getName());
//        editor.putInt("sex",user.getSex());
//        editor.putString("profile",user.getProfile());
//        editor.putInt("grade",user.getLevel());
//        editor.apply();
        userImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        userName.getContentEdt().setText(pref.getString("name",null));
        userProfile.getContentEdt().setText(pref.getString("profile",null));
        userGrade.getContentEdt().setText(String.valueOf(pref.getInt("grade",2016)));
        userCity.getContentEdt().setText("福建 龙岩");
        if(pref.getInt("sex",1) == 0){
            userSex.getContentEdt().setText("未知");
        } else if(pref.getInt("sex",1) == 1){
            userSex.getContentEdt().setText("男");
        }else{
            userSex.getContentEdt().setText("女");
        }
        mineViewModel.getUserImage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String s) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    Log.e("TAG", s );
                    Glide.with(context).load(s).into(userImage);
                }
            }
        });
        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineInfo.this,EditTextActivity.class);
                intent.putExtra("title","修改昵称");
                intent.putExtra("userName",userName.getContentEdt().getText().toString());
                startActivityForResult(intent,1);
            }
        });
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineInfo.this,EditTextActivity.class);
                intent.putExtra("title","修改简介");
                intent.putExtra("userName",userProfile.getContentEdt().getText().toString());
                startActivityForResult(intent,2);
            }
        });

        userSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexDialog();
            }
        });
        userGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGradeDialog();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(MineInfo.this)
                        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                        .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                        .maxSelectNum(1)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
//                        .isOriginalImageControl(true)
                        .isCompress(true)// 是否压缩
                        .withAspectRatio(1,1)//裁剪比例
                        .compressQuality(30)// 图片压缩后输出质量 0~ 100
                        .isEnableCrop(true)// 是否裁剪
//                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                        .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                        .imageSpanCount(4)// 每行显示个数
                        .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                        .forResult(new MyResultCallback());
            }
        });
    }

    public void showSexDialog() {
        final String[] items = {"男", "女", "未知"};
        final String[] selected = new String[1];
        selected[0] = items[0];
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择性别");
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selected[0] = items[i];
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userSex.getContentEdt().setText(selected[0]);
                sexDialog.dismiss();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sexDialog.dismiss();
            }
        });
        sexDialog = alertBuilder.create();
        sexDialog.show();
    }

    public void showGradeDialog() {
        final String[] items = {"2016", "2017", "2018","2019","2020"};
        final String[] selected = new String[1];
        selected[0] = items[0];
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择年级");
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selected[0] = items[i];
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userGrade.getContentEdt().setText(selected[0]);
                gradeDialog.dismiss();
            }
        });

        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gradeDialog.dismiss();
            }
        });
        gradeDialog = alertBuilder.create();
        gradeDialog.show();
    }

    private void getWhiteStyle() {
        // 相册主题
        mPictureParameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = true;
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = false;
        // 相册状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#FFFFFF");
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#FFFFFF");
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.ic_arrow_up;
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.ic_arrow_down;
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.oval_blank;
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.ic_back;
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(getContext(), R.color.app_color_black);
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(getContext(), R.color.app_color_black);
        // 选择相册目录背景样式
        mPictureParameterStyle.pictureAlbumStyle = R.drawable.picture_new_item_select_bg;
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_fa);
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.oval_num;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(getContext(), R.color.subColor);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_9b);
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(getContext(), R.color.subColor);
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_9b);
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(getContext(), R.color.app_color_53575e);
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_black_delete;
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
//        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureRightDefaultText = "";
//        // 自定义相册未完成文本内容
//        mPictureParameterStyle.pictureUnCompleteText = "";
//        // 自定义相册完成文本内容
//        mPictureParameterStyle.pictureCompleteText = "";
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";

//        // 自定义相册标题字体大小
//        mPictureParameterStyle.pictureTitleTextSize = 18;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 14;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 14;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 14;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 14;

        // 裁剪主题
        mCropParameterStyle = new PictureCropParameterStyle(
                ContextCompat.getColor(getContext(), R.color.app_color_white),
                ContextCompat.getColor(getContext(), R.color.app_color_white),
                ContextCompat.getColor(getContext(), R.color.app_color_black),
                mPictureParameterStyle.isChangeStatusBarFontColor);
    }
    /**
     * 返回结果回调
     */
    private class MyResultCallback implements OnResultCallbackListener<LocalMedia> {
        public MyResultCallback() {
            super();
        }

        @Override
        public void onResult(List<LocalMedia> result) {
            for (LocalMedia media : result) {
                Log.i(TAG, "是否压缩:" + media.isCompressed());
                Log.i(TAG, "压缩:" + media.getCompressPath());
                Log.i(TAG, "原图:" + media.getPath());
                Log.i(TAG, "是否裁剪:" + media.isCut());
                Log.i(TAG, "裁剪:" + media.getCutPath());
                Log.i(TAG, "是否开启原图:" + media.isOriginal());
                Log.i(TAG, "原图路径:" + media.getOriginalPath());
                Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());
                Log.i(TAG, "宽高: " + media.getWidth() + "x" + media.getHeight());
                Log.i(TAG, "Size: " + media.getSize());
                //可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
            }
            // 例如 LocalMedia 里面返回五种path
            // 1.media.getPath(); 原图path
            // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
            // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
            // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
            // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路径；注意：.isAndroidQTransform 为false 此字段将返回空
            // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
            Glide.with(getContext()).load(result.get(0).getCompressPath()).into(userImage);
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "PictureSelector Cancel");
        }
    }

    public Context getContext() {
        return this;
    }
}
