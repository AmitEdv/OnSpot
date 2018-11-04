package com.example.amit.onspot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Switch mSwitchBtnLocationMonitor;

    private boolean mIsLocationMonitoring = false;
    private AppService mAppService;
    private Intent mIntentLocationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        mAppService = new AppService();
        mIntentLocationService = new Intent(MainActivity.this, AppService.class);
        initContentView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initContentView() {
        mSwitchBtnLocationMonitor = (Switch) findViewById(R.id.switch_btn_location_monitor);
        mSwitchBtnLocationMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean shouldMonitorLocation = mSwitchBtnLocationMonitor.isChecked();
                if (shouldMonitorLocation) {
                    startLocationMonitoring();
                } else {
                    stopLocationMonitoring();
                }
            }
        });

        updateLocationMonitoringView();
    }

    private void updateLocationMonitoringView()
    {
        boolean isMonitoring = mIsLocationMonitoring;
        mSwitchBtnLocationMonitor.setChecked(isMonitoring);
    }

    /*package*/ void startLocationMonitoring() {
        Log.v(TAG, "startLocationMonitoring");
        startService(mIntentLocationService);

        //TODO - If started successfully
        mIsLocationMonitoring = true;
        updateLocationMonitoringView();
    }

    /*package*/ void stopLocationMonitoring() {
        Log.v(TAG, "stopLocationMonitoring");
        stopService(mIntentLocationService);

        //TODO - If stopped successfully
        mIsLocationMonitoring = false;
        updateLocationMonitoringView();
    }

}

