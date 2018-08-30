package com.example.skhadirahmed.speedcheck;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity implements GPSCallback {
    private GPSManager gpsManager = null;
    private double speed = 0.0;
    boolean isGPSEnabled = false;
    LocationManager locationManager;
    double currentSpeed, kmphSpeed;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.speed);
//        Location location = null;
//        location.setSpeed((float) 200);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 904);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCurrentSpeed(View view) {
        textView.setText("Loading ...");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsManager = new GPSManager(this);
        isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            gpsManager.startListening(this);
            gpsManager.setGPSCallback(this);
        } else {
            gpsManager.showSettingsAlert();
        }
    }

    @Override
    public void onGPSUpdate(Location location) {
        speed = location.getSpeed();
        currentSpeed = round(speed, 3, BigDecimal.ROUND_HALF_UP);
        kmphSpeed = round((currentSpeed * 3.6), 3, BigDecimal.ROUND_HALF_UP);
        textView.setText("speed is " + kmphSpeed);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsManager.stopListening();
        gpsManager.setGPSCallback(null);
        gpsManager = null;
    }

    public static double round(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }
}
