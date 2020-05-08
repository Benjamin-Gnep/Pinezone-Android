package com.example.pinezone.ui.mine;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.pinezone.MainActivity;
import com.example.pinezone.config.UserService;
import com.example.pinezone.user.User;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MineViewModel extends AndroidViewModel {
    private static final String TAG = "MineViewModel";

    private SharedPreferences pref;

    private MutableLiveData<String> userName;
    private MutableLiveData<String> userSign;
    private MutableLiveData<String> userImage;
    private MutableLiveData<String> userGrade;
    private MutableLiveData<String> userSex;
    private MutableLiveData<String> userLocation;
    private MutableLiveData<String> userFans;
    private MutableLiveData<String> userFollows;
    private MutableLiveData<String> userArticle;

    public MineViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        userName = new MutableLiveData<>();
        userSign = new MutableLiveData<>();
        userImage = new MutableLiveData<>();
        userGrade = new MutableLiveData<>();
        userSex = new MutableLiveData<>();
        userLocation = new MutableLiveData<>();
        userFans = new MutableLiveData<>();
        userFollows = new MutableLiveData<>();
        userArticle = new MutableLiveData<>();
        pref = application.getSharedPreferences("setting", Context.MODE_PRIVATE);
        initView();

    }

    private void initView() {
        //TODO:从数据库中获取数据，类型为json
//        userName.setValue("阿苯不加奶盖°");
//        userSign.setValue("嚼一口跳脱的味道，还要换上好看的衣裳");
//        userSex.setValue("男");
//        userGrade.setValue("2017级");
//        userLocation.setValue("福建 龙岩");
//        userFollows.setValue("25");
//        userFans.setValue("145");
//        userArticle.setValue("34");
//        userImage.setValue(R.drawable.user);

        userName.setValue(pref.getString("name",""));
        userSign.setValue(pref.getString("profile",""));
        if(pref.getInt("sex",1) == 1){
            userSex.setValue("男");
        } else {
            userSex.setValue("女");
        }
        userGrade.setValue(pref.getInt("grade",2017) + "级");
        userLocation.setValue("福建 龙岩");
        userFollows.setValue("25");
        userFans.setValue("145");
        userArticle.setValue("34");
        userImage.setValue(pref.getString("path",""));

    }


    LiveData<String> getUserName() { return userName; }

    LiveData<String> getUserSign() { return userSign; }

    MutableLiveData<String> getUserImage() {
        return userImage;
    }

    MutableLiveData<String> getUserArticle() {
        return userArticle;
    }

    MutableLiveData<String> getUserFans() {
        return userFans;
    }

    MutableLiveData<String> getUserFollows() {
        return userFollows;
    }

    MutableLiveData<String> getUserGrade() {
        return userGrade;
    }

    MutableLiveData<String> getUserLocation() {
        return userLocation;
    }

    MutableLiveData<String> getUserSex() {
        return userSex;
    }



    void refresh(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://111.230.173.4:8081/v1/")
                .build();

        UserService userService = retrofit.create(UserService.class);

        Call<ResponseBody> call = userService.getUser(2231);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    String s = response.body().string();
                    Gson gson = new Gson();

                    User user = gson.fromJson(s,User.class);

                    userName.setValue(user.getName());
                    userSign.setValue(user.getProfile());
                    if(user.getSex() == 1){
                        userSex.setValue("男");
                    } else {
                        userSex.setValue("女");
                    }
                    userGrade.setValue(user.getLevel() + "级");


                    user.userShow();

                    Log.e(TAG, "onResponse: " + s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: "+t.toString() );
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
