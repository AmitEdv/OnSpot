package com.example.amit.onspot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Switch mSwitchBtnLocationMonitor;

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

        updateLocationMonitoringView();
    }

    private void initContentView() {
        mSwitchBtnLocationMonitor = (Switch) findViewById(R.id.switch_btn_location_monitor);
        mSwitchBtnLocationMonitor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startLocationMonitoring();
                } else {
                    stopLocationMonitoring();
                }
            }
        });
    }

    private void updateLocationMonitoringView() {
        boolean isMonitoring = mAppService.isServiceRunning();
        mSwitchBtnLocationMonitor.setChecked(isMonitoring);
    }

    /*package*/ void startLocationMonitoring() {
        Log.v(TAG, "startLocationMonitoring");
        startService(mIntentLocationService);

        //TODO - If started successfully
        updateLocationMonitoringView();
    }

    /*package*/ void stopLocationMonitoring() {
        Log.v(TAG, "stopLocationMonitoring");
        stopService(mIntentLocationService);

        //TODO - If stopped successfully
        updateLocationMonitoringView();
    }

}

