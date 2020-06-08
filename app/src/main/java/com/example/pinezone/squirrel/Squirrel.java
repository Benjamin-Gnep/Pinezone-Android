package com.example.pinezone.squirrel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.pinezone.MainActivity;
import com.example.pinezone.config.SquirrelService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Squirrel {
    private int id;
    private int uid;
    private String name;
    private int hp;
    private int pinecone;
    private float food;
    private float achievement;
    private float companion;
    private int state;

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public float getAchievement() {
        return achievement;
    }

    public int getHp() {
        return hp;
    }

    public float getFood() {
        return food;
    }

    public float getCompanion() {
        return companion;
    }

    public int getPinecone() {
        return pinecone;
    }

    public void show(){
        Log.e("TAG", id + " " + name + " " + hp + " "  + pinecone + " " +
                food + " " + achievement + " " + companion );
    }

    public static void addPinecone(Context context, int num) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .build();

        final int[] pinecone = new int[1];
        final SquirrelService squirrelService = retrofit.create(SquirrelService.class);
        Call<Squirrel> call = squirrelService.getSquirrel(MainActivity.getUid());
        call.enqueue(new Callback<Squirrel>() {
            @Override
            public void onResponse(Call<Squirrel> call, Response<Squirrel> response) {
                pinecone[0] = response.body().getPinecone();
                int x = pinecone[0] + num;
                ListAdapter.setPinecone(x);
                Toast.makeText(context,"收集到" + num + "颗松果",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Squirrel> call, Throwable t) {
            }
        });

    }
}
