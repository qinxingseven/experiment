package com.example.i.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    private static final String TAG="demo";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log.d(TAG,"onCreate");
    }
     @Override
    protected void onStart() {
        super.onStart();
         log.d(TAG,"onStart);
        // The activity is about to become visible.
    }
    @Override
    protected void onResume() {
        super.onResume();
         log.d(TAG,onResume);
        // The activity has become visible (it is now "resumed").
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
         log.d(TAG,onPause);
    }
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        log.d(TAG,"onStop);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        log.d(TAG,"onDestroy);
    }
          
}
