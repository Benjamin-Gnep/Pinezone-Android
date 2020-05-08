package com.example.pinezone.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.pinezone.BasicActivity;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;

public class SplashActivity extends BasicActivity {
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBarHide();
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pref = getSharedPreferences("setting",MODE_PRIVATE);
                String history = pref.getString("account","");
                Intent intent;
                if(history.equals("")){
                    //从闪屏界面跳转到登录界面
                    intent = new Intent(SplashActivity.this, LoginHome.class);
                }else {
                    //从闪屏界面跳转到主界面
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 2000);//延迟3S后发送handler信息
    }
}
