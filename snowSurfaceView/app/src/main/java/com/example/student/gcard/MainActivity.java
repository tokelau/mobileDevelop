package com.example.student.gcard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

//    MyView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w("Nasty", "onCreate");
    }

    public void onClick(View v) throws ExecutionException, InterruptedException {
        Log.w("Nasty", "onClick");
//        MyTask task = new MyTask();
//        task.execute(new Params());
//
//
//        String result = task.get();
    }
}
