package com.kkevn.ledsign.ui.bluetooth;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class BluetoothViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BluetoothViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profiles fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}