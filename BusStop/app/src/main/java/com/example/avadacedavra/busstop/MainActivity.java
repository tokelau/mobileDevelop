package com.example.avadacedavra.busstop;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    EditText lat, lng;
    Button btnSave, btnLoad, btnAdd;
    ListView listView;
    BusStop[] stops;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat = (EditText) findViewById(R.id.lat);
        lng = (EditText) findViewById(R.id.lng);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(this);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listView);

        BusStop trilissera = new BusStop(52.272933, 104.305735, "Трилиссера", 110);
        BusStop central = new BusStop(52.281005, 104.296430, "Центральный рынок", 150);
        BusStop mcrUnion = new BusStop(52.252109, 104.263114, "Мкр. Союз",  190);
        stops = new BusStop[10];
        for (int i = 0; i < 10; i++) {
            stops[i] = new BusStop();
        }
        stops[0] = trilissera;
        stops[1] = central;
        stops[2] = mcrUnion;

        try {
            save(stops);
            BusStop[] gotStops = new BusStop[10];
            gotStops = load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<BusStop> arrayAdapter = new ArrayAdapter<BusStop>(this, android.R.layout.simple_list_item_1, stops);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdd:
                Log.e("NastyLat", "123");
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lat.setText(String.valueOf(loc.getLatitude()));
                lng.setText(String.valueOf(loc.getLongitude()));
                break;
            case R.id.btnSave:
                for (int i = 9; i > 0; i--) {
                    stops[i] = stops[i - 1];
                }

                EditText bsn = (EditText) findViewById(R.id.bus_stop_name);
                EditText dist = (EditText) findViewById(R.id.alarm_dist);

                stops[0] = new BusStop(Double.valueOf(lat.getText().toString()), Double.valueOf(lng.getText().toString()), bsn.getText().toString(),
                        Double.valueOf(dist.getText().toString()));
                save(stops);
                break;
            case R.id.btnLoad:
                try {
                    BusStop[] gotStops = new BusStop[10];
                    gotStops = load();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<BusStop> arrayAdapter = new ArrayAdapter<BusStop>(this, android.R.layout.simple_list_item_1, stops);
                listView.setAdapter(arrayAdapter);
                break;
            default:
                break;
        }
    }

    private BusStop[] load() throws JSONException {
        BusStop[] stops = new BusStop[10];
        for(int i = 0; i < 10; i++) {
            stops[i] = new BusStop();
        }
        String[] names = new String[10];

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String namesSaved = prefs.getString("names", "None");
        JSONObject jsonObject = new JSONObject(namesSaved);
        JSONArray text = jsonObject.getJSONArray("names");

        for(int i = 0; i < 10; i++) {
            names[i] = text.get(i).toString();
            if (names[i] != "null") {
                String data = prefs.getString(names[i], "None");
                if (data != "None") {
                    JSONObject _jsonObject = new JSONObject(data);
                    String bus_stop_name = _jsonObject.getString("bus_stop_name");
                    double lat = Double.valueOf(_jsonObject.getString("lat"));
                    double lng = Double.valueOf(_jsonObject.getString("lng"));
                    double dist = Double.valueOf(_jsonObject.getString("dist"));
                    stops[i] = new BusStop(lat, lng, bus_stop_name, dist);
                }

            }
        }

        return stops;
    }

    private void save(BusStop[] stops) {
        String names = " {'names' : [";

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor ed = prefs.edit();

        for(int i = 0; i < 9; i++) {
            String data = "{'bus_stop_name': '" + stops[i].bus_stop_name +
                    " ', 'lat': '" + stops[i].lat + " ', 'lng': '" + stops[i].lng + "', 'dist': '" + stops[i].alarm_dist + "'}";
            ed.putString(stops[i].bus_stop_name, data);

            names +=  "'" + stops[i].bus_stop_name + "', ";
        }
        names += "'" + stops[9].bus_stop_name  + "']}";

        ed.putString("names", names);
        ed.commit();

        Log.e("Nasty Write", names);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
