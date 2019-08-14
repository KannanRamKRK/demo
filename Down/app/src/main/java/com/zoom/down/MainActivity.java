package com.zoom.down;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    VideoView videoView;
    private static final String VIDEO_DOWNLOAD = "https://dw-digital-signage.s3.us-east-2.amazonaws.com/eduar.mp4";
    private long downloadID;
    private static final int PERMISSION_STORAGE_CODE = 1000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.videoview);
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_STORAGE_CODE);

            } else {
                startDownload("eduar.mp4", VIDEO_DOWNLOAD);
            }
        } else {
            startDownload("eduar.mp4", VIDEO_DOWNLOAD);
        }
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                File file = new File(getExternalFilesDir(null), "eduar.mp4");
                if (file.exists()) {
                    //openFile();
                } else {
                    mediaPlayer.setLooping(false);
                }

            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.requestFocus();
              //  Toast.makeText(getApplicationContext(), "complete", Toast.LENGTH_SHORT).show();
               openFile();
            }
        });

          Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.video);
        videoView.setVideoURI(uri);
        videoView.start();


    }

    public void openFile() {

        String video_path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + "eduar.mp4";
        Uri uri = Uri.parse(video_path);
        videoView.setVideoURI(uri);
        videoView.setFocusable(true);
        videoView.start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownload("eduar.mp4", VIDEO_DOWNLOAD);
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startDownload(String path, String url) {
        File file = new File(getExternalFilesDir(null), path);
        if (!file.exists()) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            //String fileName=url.substring (url.lastIndexOf('/')+1,url.length());
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setTitle("Downloading..");
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "eduar.mp4");
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            downloadID = downloadManager.enqueue(request);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);

    }
}
