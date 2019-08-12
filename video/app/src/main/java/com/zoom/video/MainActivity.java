package com.zoom.video;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity {
    VideoView vv;
    String video_url =
    "https://dw-digital-signage.s3.us-east-2.amazonaws.com/eduar.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        vv = (VideoView) findViewById(R.id.videop);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(video_url);
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

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
    class DownloadTask extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("downloading..");
            progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String path = params[0];
            int file_length = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();
                File new_folder = new File("sdcard/videoDemo");
                if (!new_folder.exists()) {
                    new_folder.mkdir();
                }

                File input_file = new File(new_folder, "DotworldVdo2.mp4");
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;
                OutputStream outputStream = new FileOutputStream(input_file);
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    outputStream.write(data, 0, count);
                    int progress =(int) total*100/file_length;
                    publishProgress(progress);
                }
                inputStream.close();
                outputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "downloaded";


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        progressDialog.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.hide();
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            String path = "sdcard/VideoDemo/DotworldVdo2.mp4";

        }
    }


}


