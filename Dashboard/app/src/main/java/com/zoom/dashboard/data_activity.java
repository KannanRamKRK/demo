package com.zoom.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class data_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
       // Button btdata=findViewById(R.id.mobdata);
    }
    public void mobiled(View v){
        IntentFilter filter= new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");


    }
}
