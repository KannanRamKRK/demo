package com.zoom.basic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onPause() {
        super.onPause();
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStop() {
        super.onStop();
        setContentView(R.layout.activity_main);

    }
}
