package com.example.pinezone.squirrel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pinezone.BasicActivity;
import com.example.pinezone.MainActivity;
import com.example.pinezone.R;
import com.example.pinezone.config.ItemGroup;
import com.example.pinezone.config.SquirrelService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetInfoActivity extends BasicActivity {

    private ItemGroup name;
    private ItemGroup pinecone;
    private ItemGroup hp;
    private ItemGroup hpPlus;
    private ItemGroup achievementPlus;
    private ItemGroup companionPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info);
        ConstraintLayout actionBarView = findViewById(R.id.action_bar);
        TextView textView = (TextView) actionBarView.findViewById(R.id.action_bar_title);
        textView.setText("松鼠信息");
        name = findViewById(R.id.name_ig);
        pinecone = findViewById(R.id.pinecone_ig);
        hp = findViewById(R.id.hp_ig);
        hpPlus = findViewById(R.id.hp_plus_ig);
        achievementPlus = findViewById(R.id.achievement_plus_ig);
        companionPlus = findViewById(R.id.companion_plus_ig);
        initInfo();
    }

    private void initInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();

        final SquirrelService squirrelService = retrofit.create(SquirrelService.class);
        Call<Squirrel> call;
        call = squirrelService.getSquirrel(MainActivity.getUid());
        call.enqueue(new Callback<Squirrel>() {
            @Override
            public void onResponse(Call<Squirrel> call, Response<Squirrel> response) {
                response.body().show();
                name.getContentEdt().setText(response.body().getName());
                pinecone.getContentEdt().setText(String.valueOf(response.body().getPinecone()));
                hp.getContentEdt().setText(String.valueOf(response.body().getHp()));
                hpPlus.getContentEdt().setText(String.valueOf(response.body().getFood()));
                achievementPlus.getContentEdt().setText(String.valueOf(response.body().getAchievement()));
                companionPlus.getContentEdt().setText(String.valueOf(response.body().getCompanion()));
            }
            @Override
            public void onFailure(Call<Squirrel> call, Throwable t) {
                Toast.makeText(getContext(),"网络错误",Toast.LENGTH_SHORT).show();
                FoodActivity.progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private Context getContext() {
        return PetInfoActivity.this;
    }
}
