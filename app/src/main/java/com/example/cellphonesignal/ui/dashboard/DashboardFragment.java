package com.example.cellphonesignal.ui.dashboard;

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
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

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
        //graphView.setMinX(0.0);
        //graphView.setMaxX(10);
        graphView.setYAxisBoundsManual(true);
        graphView.setMinY(0.0);
        graphView.setMaxY(10.1);
        graphView.setScrollable(true);
        graph.setTitle("Wifi Signal Strength");
        //TODO: adjust labels on horizontal axis to go by .5 increments

        graph.addSeries(series);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        //here we are going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {
            @Override
            public void run() {
                //we add 100 new entries
                for(int i = 0; i < 1000; i++) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addEntry();
                        }
                    });

                    //sleep to slow down thread execution (error if not in try/catch)
                    try {
                        Thread.sleep(500); //update by half second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void addEntry() {
        //for testing, will add random values
        //in real usage, will add proper values from WifiManager

        //this line says: we choose to display max of 10 points on viewport and we scroll to end
        series.appendData(new DataPoint(lastX, RANDOM.nextDouble() * 10d), true, 12);
        lastX += .5;
    }
}
