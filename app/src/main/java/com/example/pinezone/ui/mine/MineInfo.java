package com.example.pinezone.ui.mine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pinezone.BasicActivity;
import com.example.pinezone.R;
import com.example.pinezone.config.ItemGroup;
import com.example.pinezone.ui.setting.SettingsActivity;
import com.makeramen.roundedimageview.RoundedImageView;

public class MineInfo extends BasicActivity {
    private SharedPreferences pref;
    private Context context;
    private SharedPreferences.Editor editor;
    private ItemGroup userName;
    private ItemGroup userProfile;
    private ItemGroup userSex;
    private ItemGroup userGrade;
    private ItemGroup userCity;
    private RoundedImageView userImage;
    private AlertDialog sexDialog; //单选框
    private AlertDialog gradeDialog;
    private MineViewModel mineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_info);
        initView();

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
//
//        editor.putInt("id",user.getId());
//        editor.putString("name",user.getName());
//        editor.putInt("sex",user.getSex());
//        editor.putString("profile",user.getProfile());
//        editor.putInt("grade",user.getLevel());
//        editor.apply();

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
}
