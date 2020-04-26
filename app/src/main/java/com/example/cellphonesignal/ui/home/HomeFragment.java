package com.example.cellphonesignal.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cellphonesignal.R;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView exText = root.findViewById(R.id.text_home);
        homeViewModel.getExText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                exText.setText(s);
            }
        });

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
        homeViewModel.getWifiEnabled(isWifiOn).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textWifiEnabled.setText(s);
            }
        });

        final TextView textMACAddress = root.findViewById(R.id.mac_address);
        String macaddr = wifiManager.getConnectionInfo().getMacAddress();
        homeViewModel.getMacAddress(macaddr).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textMACAddress.setText(s); //must test on real device
            }
        });

        //consider removing this or putting something more worthwhile here
        final TextView textCellService = root.findViewById(R.id.cell_service);
        homeViewModel.getCellService().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                TelephonyManager tm = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                String networkOperator = tm.getNetworkOperatorName();
                textCellService.setText(networkOperator);
                //textCellService.setText(s);
            }
        });

        return root;
    }
}
