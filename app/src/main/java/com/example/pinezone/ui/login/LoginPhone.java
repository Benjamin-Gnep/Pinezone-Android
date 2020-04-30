package com.example.pinezone.ui.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pinezone.BasicActivity;
import com.example.pinezone.R;
import com.mob.MobSDK;

import cn.smssdk.SMSSDK;

public class LoginPhone extends BasicActivity implements View.OnClickListener {

    Button getCodeBtn;
    EditText editPhone;
    private String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        LoginActivityCollector.addActivity(this);
        initView();
        MobSDK.submitPolicyGrantResult(true, null);
    }

    private void initView() {
        getCodeBtn = findViewById(R.id.login_phone_get_code_btn);
        editPhone = findViewById(R.id.login_phone_number_ed);
        getCodeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phone_number=editPhone.getText().toString();//1
        switch (v.getId()){
            case R.id.login_phone_get_code_btn:
                if (judPhone())//去掉左右空格获取字符串，是正确的手机号
                {
                    SMSSDK.getVerificationCode("86", phone_number);//获取你的手机号的验证码
                    LoginCode.actionStart(LoginPhone.this,phone_number);
                }
                break;
        }
    }

    private boolean judPhone() {//判断手机号是否正确
        //不正确的情况
        if(TextUtils.isEmpty(editPhone.getText().toString().trim()))//对于字符串处理Android为我们提供了一个简单实用的TextUtils类，如果处理比较简单的内容不用去思考正则表达式不妨试试这个在android.text.TextUtils的类，主要的功能如下:
        //是否为空字符 boolean android.text.TextUtils.isEmpty(CharSequence str)
        {
            Toast.makeText(LoginPhone.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            editPhone.requestFocus();//设置是否获得焦点。若有requestFocus()被调用时，后者优先处理。注意在表单中想设置某一个如EditText获取焦点，光设置这个是不行的，需要将这个EditText前面的focusable都设置为false才行。
            return false;
        }
        else if(editPhone.getText().toString().trim().length()!=11)
        {
            Toast.makeText(LoginPhone.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            editPhone.requestFocus();
            return false;
        }
        //正确的情况
        else
        {
            phone_number=editPhone.getText().toString().trim();
            String num="[1][3456789]\\d{9}";
            if(phone_number.matches(num))
                return true;
            else
            {
                Toast.makeText(LoginPhone.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }
}
