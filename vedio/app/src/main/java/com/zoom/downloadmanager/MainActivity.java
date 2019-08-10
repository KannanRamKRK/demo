package com.zoom.downloadmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private  static final String VIDEO_DOWNLOAD_PATH ="https://06jiaus6k6.execute-api.us-east-2.amazonaws.com/dev/video/status";
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView=findViewById(R.id.videoview);


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                startService(DownloadVdo.getDownloadService(this,VIDEO_DOWNLOAD_PATH,DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));
                mediaPlayer.setLooping(true);

            }
        });
    }
}
