package com.example.pinezone.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import com.example.pinezone.BasicActivity;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;

import java.util.Timer;

public class SplashActivity extends BasicActivity {
    Timer timer = new Timer();
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBarHide();
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        Runnable runnable;
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                pref = getSharedPreferences("config",MODE_PRIVATE);
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
