package com.zoom.downloadmanager.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.zoom.downloadmanager.R;

public class MainActivity extends AppCompatActivity  {
    VideoView videoView;
    private static final String VIDEO_DOWNLOAD ="https://dw-digital-signage.s3.us-east-2.amazonaws.com/eduar.mp4";


    private static final int PERMISSION_STORAGE_CODE=1000;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView=findViewById(R.id.videoview);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){

              String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
              requestPermissions(permissions,PERMISSION_STORAGE_CODE);

          }else{
              startDownload();
          }
        }else {
            startDownload();
        }
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.video);
        videoView.setVideoURI(uri);
        videoView.start();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      switch (requestCode){
          case PERMISSION_STORAGE_CODE:{
              if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                  startDownload();
              }
              else {
                  Toast.makeText(this,"permission denied", Toast.LENGTH_SHORT).show();
              }
          }
      }
    }

    private void startDownload() {
        Uri uri = Uri.parse(VIDEO_DOWNLOAD);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalFilesDir(this,Environment.DIRECTORY_DOWNLOADS,"eduar.mp4");
        ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);

    }
}