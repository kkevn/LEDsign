package com.kkevn.ledsign.ui.profiles;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ProfilesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfilesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profiles fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}