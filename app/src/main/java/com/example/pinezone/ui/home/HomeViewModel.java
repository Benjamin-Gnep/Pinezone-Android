package com.example.pinezone.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> hText;
    private MutableLiveData<String> sText;

    public HomeViewModel() {
        hText = new MutableLiveData<>();
        hText.setValue("松鼠好像还没回家，再等等吧");

        sText = new MutableLiveData<>();
        sText.setValue("> 今天打算去哪里逛逛呢 <");
    }

    public LiveData<String> getHomeText() {
        return hText;
    }

    public LiveData<String> getSelectionText() {
        return sText;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        hText = null;
        sText = null;
    }
}