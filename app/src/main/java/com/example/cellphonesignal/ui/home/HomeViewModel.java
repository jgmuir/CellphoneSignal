package com.example.cellphonesignal.ui.home;

import android.app.Activity;
import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static androidx.core.content.ContextCompat.getSystemService;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    //maybe a different data type?
    private MutableLiveData<String> phoneType;
    private MutableLiveData<String> wifiEnabled;
    private MutableLiveData<String> macAddress;
    private MutableLiveData<String> cellService;
    private Activity context;

    public HomeViewModel() {
        //example
        mText = new MutableLiveData<>();
        mText.setValue("This will be the home screen 'fragment'");

        //placeholder values now, add functions to get true values
        phoneType = new MutableLiveData<>();
        phoneType.setValue(Build.MODEL);
        wifiEnabled = new MutableLiveData<>();
        macAddress = new MutableLiveData<>();
        cellService = new MutableLiveData<>();
        cellService.setValue("Yes");
    }

    public LiveData<String> getExText() { return mText; }

    public LiveData<String> getPhoneType() { return phoneType; }
    public LiveData<String> getWifiEnabled() { return wifiEnabled; }
    public LiveData<String> getMacAddress() { return macAddress; }
    public LiveData<String> getCellService() { return cellService; }
}