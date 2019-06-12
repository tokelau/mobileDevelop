package com.example.avadacedavra.busstop;

import android.support.v7.app.AppCompatActivity;

public class Point extends AppCompatActivity {
    public double lat;
    public double lng;

    public Point(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
    public Point() {
        this.lat = 0;
        this.lng = 0;
    }

}
