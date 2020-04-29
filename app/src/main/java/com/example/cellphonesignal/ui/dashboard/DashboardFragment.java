package com.example.cellphonesignal.ui.dashboard;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cellphonesignal.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    View mRoot;

    private final Handler mHandler = new Handler(); //handler that ends graph creation when leaving
    private Runnable runnable1; //runnable that runs graph creation
    private LineGraphSeries<DataPoint> series; //hold our data that is plotted
    private double lastX = 0; //global variable to track what x value we are on

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mRoot = root;
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        GraphView graph = (GraphView) root.findViewById(R.id.wifiStrengthGraph); //create graph
        Viewport graphView = graph.getViewport(); //get its viewport
        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer(); //get its grids
        series = new LineGraphSeries<DataPoint>(); //initialize the data that will go onto graph

        //set title and its size
        graph.setTitle("Wifi Signal Strength");
        graph.setTitleTextSize(48);

        //set axis titles
        gridLabelRenderer.setHorizontalAxisTitle("Time (seconds)");
        gridLabelRenderer.setVerticalAxisTitle("Signal Strength (dBm)");

        //set boundaries of graph
        graphView.setXAxisBoundsManual(true);
        graphView.setMinX(0.0);
        graphView.setMaxX(120.0);
        graphView.setYAxisBoundsManual(true);
        graphView.setMinY(-130);
        graphView.setMaxY(0.1);
        graphView.setScrollable(true); //scroll along with values added over time

        graph.addSeries(series); //add data to graph
        return root;
    }

    @Override
    public void onResume() {
        //lets us start the tracking again without crashing the app
        super.onResume();
        runnable1 = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                addEntry();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addEntry() {
        //get wifiManager to get its values
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int result = wifiManager.getConnectionInfo().getRssi();
        series.appendData(new DataPoint(lastX, result), true, 240);
        Log.d("RSSI", Integer.toString(result)); //for debugging and checking values

        final TextView textWifiState = mRoot.findViewById(R.id.wifi_state);
        String wifiStateString = dashboardViewModel.displayWifiState(result);
        textWifiState.setText(wifiStateString); //show the relative strength of the wifi

        String ssid = wifiManager.getConnectionInfo().getSSID();
        final TextView textSSID = mRoot.findViewById(R.id.ssid);
        textSSID.setText(ssid); //show the wifi network name
        lastX += .5; //update by .5 increments
    }
}
