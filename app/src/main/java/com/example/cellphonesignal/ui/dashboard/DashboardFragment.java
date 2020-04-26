package com.example.cellphonesignal.ui.dashboard;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private final Handler mHandler = new Handler();
    private Runnable runnable1;
    private Runnable runnable2;
    private LineGraphSeries<DataPoint> series; //hold our data that is plotted

    //for demonstration, will remove later
    private static final Random RANDOM = new Random();
    private double lastX = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        double x,y; //hold the values of x and y
        x = 0.0;
        GraphView graph = (GraphView) root.findViewById(R.id.wifiStrengthGraph); //create graph
        Viewport graphView = graph.getViewport();
        series = new LineGraphSeries<DataPoint>();
        //set boundaries (tinker with values)
        graphView.setXAxisBoundsManual(true);
        graphView.setMinX(0.0);
        graphView.setMaxX(120.0);
        graphView.setYAxisBoundsManual(true);
        graphView.setMinY(0.0);
        graphView.setMaxY(10.1);
        graphView.setScrollable(true);
        graph.setTitle("Wifi Signal Strength");
        graph.setTitleTextSize(64);
        //TODO: adjust labels on horizontal axis to go by .5 increments

        graph.addSeries(series);
        return root;
    }

    @Override
    public void onResume() {
        //lets us start the tracking again without crashing the app
        super.onResume();

        runnable1 = new Runnable() {
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

    private void addEntry() {
        //for testing, will add random values
        //in real usage, will add proper values from WifiManager

        //this line says: we choose to display max of 10 points on viewport and we scroll to end
        series.appendData(new DataPoint(lastX, RANDOM.nextDouble() * 10d), true, 20);
        lastX += .5;
    }
}
