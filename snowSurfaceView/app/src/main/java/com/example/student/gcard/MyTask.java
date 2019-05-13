package com.example.student.gcard;

import android.os.AsyncTask;
import android.util.Log;


public class MyTask extends AsyncTask<Params, Void, String> {
    @Override
    protected void onPostExecute(String s) {
        Log.w("Nasty", "onPostExecute");
    }

    @Override
    protected String doInBackground(Params... params) {
        Log.w("Nasty", "doInBackground");
        return "{}";
    }
}
