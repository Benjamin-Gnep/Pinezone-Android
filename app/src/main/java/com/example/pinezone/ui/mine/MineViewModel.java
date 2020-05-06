package com.example.pinezone.ui.mine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pinezone.R;

public class MineViewModel extends ViewModel {
    private MutableLiveData<String> userName;
    private MutableLiveData<String> userSign;
    private MutableLiveData<Integer> userImage;
    private MutableLiveData<String> userGrade;
    private MutableLiveData<String> userSex;
    private MutableLiveData<String> userLocation;
    private MutableLiveData<String> userFans;
    private MutableLiveData<String> userFollows;
    private MutableLiveData<String> userArticle;

    public MineViewModel() {
        userName = new MutableLiveData<>();
        userSign = new MutableLiveData<>();
        userImage = new MutableLiveData<>();
        userGrade = new MutableLiveData<>();
        userSex = new MutableLiveData<>();
        userLocation = new MutableLiveData<>();
        userFans = new MutableLiveData<>();
        userFollows = new MutableLiveData<>();
        userArticle = new MutableLiveData<>();

        initView();

    }

    private void initView() {
        //TODO:从数据库中获取数据，类型为json
        userName.setValue("阿苯不加奶盖°");
        userSign.setValue("嚼一口跳脱的味道，还要换上好看的衣裳");
        userSex.setValue("男");
        userGrade.setValue("2017级");
        userLocation.setValue("福建 龙岩");
        userFollows.setValue("25");
        userFans.setValue("145");
        userArticle.setValue("34");
        userImage.setValue(R.drawable.user);
    }


    public LiveData<String> getUserName() { return userName; }

    public LiveData<String> getUserSign() { return userSign; }

    public MutableLiveData<Integer> getUserImage() {
        return userImage;
    }

    public MutableLiveData<String> getUserArticle() {
        return userArticle;
    }

    public MutableLiveData<String> getUserFans() {
        return userFans;
    }

    public MutableLiveData<String> getUserFollows() {
        return userFollows;
    }

    public MutableLiveData<String> getUserGrade() {
        return userGrade;
    }

    public MutableLiveData<String> getUserLocation() {
        return userLocation;
    }

    public MutableLiveData<String> getUserSex() {
        return userSex;
    }



    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
