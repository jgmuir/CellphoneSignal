package com.example.cellphonesignal.ui.home;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cellphonesignal.R;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View mRoot;

    private Runnable runnable1;
    private final Handler mHandler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mRoot = root;

        final TextView textPhoneType = root.findViewById(R.id.phone_type);
        homeViewModel.getPhoneType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textPhoneType.setText(s);
            }
        });

        final TextView textWifiEnabled = root.findViewById(R.id.wifi_enabled);
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean isWifiOn = wifiManager.isWifiEnabled();
        if(isWifiOn) {
            textWifiEnabled.setText("Yes");
        } else {
            textWifiEnabled.setText("No");
        }

        final TextView textIPAddress = root.findViewById(R.id.ip_address);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        String ip = "";
        try {
            ip = InetAddress.getByAddress(
                    ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array())
                    .getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        textIPAddress.setText(ip);

        //consider removing this or putting something more worthwhile here
        final TextView textCellService = root.findViewById(R.id.cell_service);
        TelephonyManager tm = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        int cellType = tm.getPhoneType();
        Log.d("HomeFrag2", Integer.toString(cellType));
        homeViewModel.getCellService(cellType).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textCellService.setText(s);
                //textCellService.setText(s);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        //lets us start the tracking again without crashing the app
        super.onResume();
        runnable1 = new Runnable() {
            @Override
            public void run() {
                updateEntry();
                mHandler.postDelayed(this, 500);
            }
        };
        mHandler.postDelayed(runnable1, 500);
    }

    @Override
    public void onPause() {
        //allows us to switch among the screens without crashing the app
        mHandler.removeCallbacks(runnable1);
        super.onPause();
    }

    private void updateEntry() {
        final TextView textWifiEnabled = mRoot.findViewById(R.id.wifi_enabled);
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean isWifiOn = wifiManager.isWifiEnabled();
        if(isWifiOn) {
            textWifiEnabled.setText("Yes");
        } else {
            textWifiEnabled.setText("No");
        }
        final TextView textIPAddress = mRoot.findViewById(R.id.ip_address);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        String ip = "";
        try {
            ip = InetAddress.getByAddress(
                    ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array())
                    .getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        textIPAddress.setText(ip);
    }
}
