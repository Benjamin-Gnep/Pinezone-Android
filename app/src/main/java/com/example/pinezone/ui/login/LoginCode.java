package com.example.pinezone.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pinezone.BasicActivity;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.mob.MobSDK;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginCode extends BasicActivity implements View.OnClickListener {
    EditText editCode;
    Button getCodeBtn;
    String codeNum;
    String phoneNum;

    EventHandler eventHandler;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    private int time=60;
    private boolean flag=true;

    public static void actionStart(Context context,String phone){
        Intent intent = new Intent(context,LoginCode.class);
        intent.putExtra("phoneNum",phone);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_code);
        LoginActivityCollector.addActivity(this);
        initView();

        Intent intent = getIntent();
        phoneNum = intent.getStringExtra("phoneNum");

        MobSDK.submitPolicyGrantResult(true, null);

        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();//创建了一个对象
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);//注册短信回调（记得销毁，避免泄露内存）*/
    }

    protected void onDestroy() {//销毁
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == -1) {
                //修改控件文本进行倒计时  i 以60秒倒计时为例
                getCodeBtn.setText( time+" s 后重试");
            } else if (msg.what == -2) {
                //修改控件文本，进行重新发送验证码
                getCodeBtn.setText("重新发送验证码");
                getCodeBtn.setClickable(true);
                time = 60;
            } else {
                int event=msg.arg1;
                int result=msg.arg2;
                Object data=msg.obj;

                if (result==SMSSDK.RESULT_COMPLETE){//回调成功
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                        Toast.makeText(getApplicationContext(), "验证码输入正确",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginCode.this, MainActivity.class));
                        editor.putString("account",phoneNum);//把账号持久化储存
                        editor.apply();
                        LoginActivityCollector.finishAll();
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取验证码成功
                        Toast.makeText(getApplicationContext(), "验证码已经发送",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {//其他出错情况
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            Toast.makeText(LoginCode.this, des, Toast.LENGTH_LONG).show();
                            return;
                        }
                        LoginCode.this.finish();
                    } catch (Exception e) {
                        //do something
                    }
                }
            }
        }
    };

    @SuppressLint("CommitPrefEdits")
    private void initView() {
        editCode = findViewById(R.id.login_code_number_ed);
        getCodeBtn = findViewById(R.id.login_code_get_code_btn);

        getCodeBtn.setOnClickListener(this);
        editCode.requestFocus();
        pref = getSharedPreferences("config",MODE_PRIVATE);
        editor = pref.edit();
        getCodeBtn.setClickable(false);
        //开始倒计时
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; time > 0; time--) {
                    handler.sendEmptyMessage(-1);
                    if (time <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //倒计时结束执行
                handler.sendEmptyMessage(-2);
            }
        }).start();

        editCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editCode.length() == 6){
                    codeNum = editCode.getText().toString().trim();
                    SMSSDK.submitVerificationCode("86",phoneNum, codeNum);//提交手机号和验证码
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_code_get_code_btn:
                SMSSDK.getVerificationCode("86", phoneNum);//获取你的手机号的验证码
                getCodeBtn.setClickable(false);
                //开始倒计时
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; time > 0; time--) {
                            handler.sendEmptyMessage(-1);
                            if (time <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        //倒计时结束执行
                        handler.sendEmptyMessage(-2);
                    }
                }).start();
                break;
            default:
                break;
        }
    }
}
