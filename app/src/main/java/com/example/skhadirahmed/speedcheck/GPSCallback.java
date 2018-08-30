package com.example.skhadirahmed.speedcheck;

import android.location.Location;

public interface GPSCallback {
    public abstract void onGPSUpdate(Location location);
}