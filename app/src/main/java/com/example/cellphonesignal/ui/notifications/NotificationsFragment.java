package com.example.cellphonesignal.ui.notifications;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
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
    private View mRoot;

    private final Handler mHandler = new Handler(); //handler that ends graph creation when leaving
    private Runnable runnable1; //runnable that runs graph creation
    private LineGraphSeries<DataPoint> series; //hold our data that is plotted
    private double lastX = 0; //global variable to track what x value we are on

    TelephonyManager mTelephonyManager;
    private MyPhoneStateListener mPhoneStatelistener;
    int mSignalStrength = 0;

    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            mSignalStrength = signalStrength.getGsmSignalStrength();
            mSignalStrength = (2 * mSignalStrength) - 113; // -> dBm
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        mRoot = root;

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
        graphView.setMinY(-130);
        graphView.setMaxY(10); //need to play with this value depending on data received
        graphView.setScrollable(true); //scroll along with values added over time

        graph.addSeries(series); //add data to graph

        mPhoneStatelistener = new MyPhoneStateListener();
        mTelephonyManager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            getActivity().requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        String thing = telephonyManager.getDeviceSoftwareVersion();
        Log.d("Device Software Version", thing);
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
        Log.d("here", "here");
        TelephonyManager tm = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        //SignalStrength signalStrength = tm.getSignalStrength();
        //int result = signalStrength.getGsmSignalStrength();
        //Log.d("Result",  Integer.toString(result));
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //need to request permission properly
            getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }
        CellInfoLte cellinfolte = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
        CellSignalStrengthLte cellSignalStrengthLte = cellinfolte.getCellSignalStrength();
        int result = cellSignalStrengthLte.getDbm();
        Log.d("Result",  Integer.toString(result));
        series.appendData(new DataPoint(lastX, result), true, 240);

        final TextView textCellState = mRoot.findViewById(R.id.cell_state);
        String cellStateString = notificationsViewModel.displayCellState(result);
        textCellState.setText(cellStateString); //show the relative strength of the wifi
        lastX += .5;
    }
}
