package com.example.cellphonesignal.ui.notifications;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cellphonesignal.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private final Handler mHandler = new Handler(); //handler that ends graph creation when leaving
    private Runnable runnable1; //runnable that runs graph creation
    private LineGraphSeries<DataPoint> series; //hold our data that is plotted
    private double lastX = 0; //global variable to track what x value we are on

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        GraphView graph = (GraphView) root.findViewById(R.id.wifiStrengthGraph); //create graph
        Viewport graphView = graph.getViewport();
        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
        series = new LineGraphSeries<DataPoint>(); //intialize the data that will go onto graph

        //set title and its size
        graph.setTitle("Cell Signal Strength");
        graph.setTitleTextSize(48);

        //set axis titles
        gridLabelRenderer.setHorizontalAxisTitle("Time (seconds)");
        gridLabelRenderer.setVerticalAxisTitle("Signal Strength");

        //set boundaries (tinker with values)
        graphView.setXAxisBoundsManual(true);
        graphView.setMinX(0.0);
        graphView.setMaxX(120.0);
        graphView.setYAxisBoundsManual(true);
        graphView.setMinY(0.0);
        graphView.setMaxY(1.1); //need to play with this value depending on data received
        graphView.setScrollable(true); //scroll along with values added over time

        graph.addSeries(series); //add data to graph
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        runnable1 = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.P)
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
        mHandler.removeCallbacks(runnable1);
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void addEntry() {
        TelephonyManager tm = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        //tm.getSignalStrength();
        int level = tm.getSignalStrength().getLevel();
        series.appendData(new DataPoint(lastX, level), true, 240);
        lastX += .5;
    }
}
