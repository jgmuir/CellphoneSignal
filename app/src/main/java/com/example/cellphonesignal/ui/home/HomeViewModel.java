package com.example.cellphonesignal.ui.home;

import android.app.Activity;
import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    //maybe a different data type?
    private MutableLiveData<String> phoneType;
    private MutableLiveData<String> wifiEnabled;
    private MutableLiveData<String> ipAddress;
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
        ipAddress = new MutableLiveData<>();
        cellService = new MutableLiveData<>();
    }

    public LiveData<String> getExText() { return mText; }

    public LiveData<String> getPhoneType() { return phoneType; }
    public LiveData<String> getWifiEnabled(boolean isWifiOn) {
        if(isWifiOn) {
            wifiEnabled.setValue("Yes");
            return wifiEnabled;
        } else {
            wifiEnabled.setValue("No");
            return wifiEnabled;
        }
    }
    public LiveData<String> getIPAddress(String macaddr) {
        ipAddress.setValue(macaddr);
        return ipAddress;
    }
    public LiveData<String> getCellService(int cellType) {
        if(cellType == 1) {
            cellService.setValue("GSM");
        } else  if(cellType == 2) {
            cellService.setValue("CDMA");
        } else if(cellType == 3) {
            cellService.setValue("SIP");
        } else {
            cellService.setValue("Unknown");
        }
        return cellService;
    }
}