package com.zoom.video;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private long downloadID;
    private int mCurrent = 0;
    private static final String PLAYBACK_TIME = "play_time";
    VideoView vv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        vv = (VideoView) findViewById(R.id.videop);

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {


            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.video);

        vv.setVideoURI(uri);

        vv.start();


    }
}



