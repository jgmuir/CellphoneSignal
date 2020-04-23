package com.example.cellphonesignal.ui.notifications;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    LineGraphSeries<DataPoint> series;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        double x,y; //hold the values of x and y
        x = 0.0;
        GraphView graph = (GraphView) root.findViewById(R.id.wifiStrengthGraph);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMaxX(120);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(1.1);
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        series = new LineGraphSeries<DataPoint>();
        series.setTitle("Wifi Signal Strength");
        for(int i = 0; i < 1200; i++) {
            x += 0.1; //must go at this step to make graph look continuous
            if(wifiManager.isWifiEnabled()) {
                //get real time working first, then do wifiManager stuff
                y = 1;
            } else {
                y = 0;
            }
            series.appendData(new DataPoint(x,y), true, 1200);
        }
        graph.addSeries(series);
        return root;
    }
}
