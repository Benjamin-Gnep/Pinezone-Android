package com.example.pinezone.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.pinezone.BasicActivity;
import com.example.pinezone.R;

public class LoginHome extends BasicActivity implements View.OnClickListener {

    Button loginBtn;
    CheckBox consentCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);
        initView();
        loginBtn.setOnClickListener(this);
        LoginActivityCollector.addActivity(this);
    }

    private void initView() {
        loginBtn = findViewById(R.id.login_home_btn);
        consentCheck = findViewById(R.id.login_home_check);
        setBarColor(this,false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_home_btn:
                if(consentCheck.isChecked()){
                    Intent intent = new Intent(LoginHome.this,LoginPhone.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(v.getContext(),"请先勾选下方同意相关条例",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
    }
}
