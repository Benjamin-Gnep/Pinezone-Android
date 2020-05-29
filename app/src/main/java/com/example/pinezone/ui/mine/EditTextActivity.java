package com.example.pinezone.ui.mine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pinezone.BasicActivity;
import com.example.pinezone.R;

public class EditTextActivity extends BasicActivity {
    private EditText editText;
    private View actionBar;
    private Button backButton;
    private Button centerButton;
    private Button okay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        Intent intent = getIntent();
        actionBar = findViewById(R.id.action_threebutton_bar);
        editText = findViewById(R.id.edit_text);
        editText.setText(intent.getStringExtra("userName"));
        centerButton = actionBar.findViewById(R.id.action_bar_title);
        centerButton.setClickable(false);
        centerButton.setText(intent.getStringExtra("title"));
        okay = actionBar.findViewById(R.id.next_button);
        okay.setText("保存");
        backButton = actionBar.findViewById(R.id.back_button);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(intent.getStringExtra("title").equals("修改昵称")
                        && editText.getText().length() > 12){
                    Toast.makeText(EditTextActivity.this,
                            "昵称过长",Toast.LENGTH_SHORT).show();
                }
                if(intent.getStringExtra("title").equals("修改简介")
                        && editText.getText().length() > 40){
                    Toast.makeText(EditTextActivity.this,
                            "简介过长",Toast.LENGTH_SHORT).show();
                }
                intent.putExtra("text",editText.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
