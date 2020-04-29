package com.example.cellphonesignal.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This will be the cellular screen 'fragment'");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public String displayCellState(int result) {
        String cellStateString = "";
        if(result >= -90) {
            cellStateString = "Excellent";
        } else if(result >= -105) {
            cellStateString = "Very Good";
        } else if(result >= -110) {
            cellStateString = "Okay";
        } else if(result >= -119) {
            cellStateString = "Not Good";
        } else {
            cellStateString = "Practically Unusable";
        }
        return cellStateString;
    }
}