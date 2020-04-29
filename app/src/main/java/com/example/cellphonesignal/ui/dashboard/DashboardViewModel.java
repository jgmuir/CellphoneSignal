package com.example.cellphonesignal.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> ssid;
    private MutableLiveData<String> wifiState;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This will be the Wifi screen 'fragment'");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> displaySSID(String ssid_string) {
        ssid.setValue(ssid_string);
        return ssid;
    }

    public String displayWifiState(int result) {
        String wifiStateString = "";
        if(result > -30) {
            wifiStateString = "Excellent";
        } else if(result > -67) {
            wifiStateString = "Very Good";
        } else if(result > -70) {
            wifiStateString = "Okay";
        } else if(result > -80) {
            wifiStateString = "Not Good";
        } else if(result > -90) {
            wifiStateString = "Poor";
        } else {
            wifiStateString = "Practically Unusable";
        }
        return wifiStateString;
    }
}