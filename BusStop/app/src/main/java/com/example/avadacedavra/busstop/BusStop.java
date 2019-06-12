package com.example.avadacedavra.busstop;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class BusStop extends Point {
    public String bus_stop_name;
    public double alarm_dist;
    SharedPreferences prefs;

    public BusStop(double lat, double lng, String stopName, double dist) {
        super(lat, lng);

        this.bus_stop_name = stopName;
        this.alarm_dist = dist;
    }
    public BusStop() {
        super();

    }

    public static BusStop[] load() throws JSONException {
//        JSONObject jsonObject = new JSONObject(data);
        BusStop[] stops = new BusStop[10];
        BusStop fakeStop = new BusStop();

        SharedPreferences prefs = fakeStop.getPreferences(MODE_PRIVATE);
        String names = prefs.getString("names", "");
//        JSONObject jsonObject = new JSONObject(names);
        Log.e("NastyNames", names);
//        lat.setText(latSaved);
//        String lngSaved = prefs.getString("lng", "None");
//        lng.setText(lngSaved);
//        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
        return stops;
    }

    @Override
    public String toString() {
        return bus_stop_name + "\nlat: " + String.valueOf(lat) + "\nlng: " + String.valueOf(lng) + "\n" + String.valueOf(alarm_dist) + " м.";
    }
}
