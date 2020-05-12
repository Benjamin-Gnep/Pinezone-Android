package com.example.pinezone.ui.publish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pinezone.BasicActivity;
import com.example.pinezone.R;
import com.example.pinezone.config.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

public class PublishType extends BasicActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_CHOOSE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_type);
        Button canteen = findViewById(R.id.publish_canteen);
        Button store = findViewById(R.id.publish_store);
        Button play = findViewById(R.id.publish_play);
        Button study = findViewById(R.id.publish_study);
        Button gym = findViewById(R.id.publish_gym);
        canteen.setOnClickListener(this);
        store.setOnClickListener(this);
        play.setOnClickListener(this);
        study.setOnClickListener(this);
        gym.setOnClickListener(this);
        PublishActivityCollector.addActivity(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.publish_canteen:
                PublishArticle.StartActivity(this,1);
                break;
            case R.id.publish_store:
                PublishArticle.StartActivity(this,2);
                break;
            case R.id.publish_play:
                PublishArticle.StartActivity(this,3);
                break;
            case R.id.publish_study:
                PublishArticle.StartActivity(this,4);
                break;
            case R.id.publish_gym:
                PublishArticle.StartActivity(this,5);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PublishActivityCollector.removeActivity(this);
    }

}
