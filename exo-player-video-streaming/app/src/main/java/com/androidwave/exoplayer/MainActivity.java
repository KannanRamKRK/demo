package com.androidwave.exoplayer;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button buttonPlayUrlVideo;
    Button buttonPlayDefaultVideo;
    ProgressDialog mProgressBar;

    private long downloadID;

    private static String DOWNLOAD_FILE_PATH = "";
    Permissions permissions = new Permissions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        permissions.askPermissionAndWriteFile(this);

        buttonPlayDefaultVideo = findViewById(R.id.buttonPlayDefaultVideo);
        buttonPlayUrlVideo = findViewById(R.id.buttonPlayUrlVideo);
        mProgressBar = new ProgressDialog(this);
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buttonPlayUrlVideo.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

        buttonPlayDefaultVideo.setOnClickListener(buttonClickListeenr);
        buttonPlayUrlVideo.setOnClickListener(buttonClickListeenr);

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        start_download();

    }


    private void start_download() {
        showProgressBar();
        beginDownload(VideoPlayerConfig.STREAMING_URL);
    }

    private View.OnClickListener buttonClickListeenr = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.buttonPlayUrlVideo:
                    start_download();
                    break;
                case R.id.buttonPlayDefaultVideo:
                    Intent mIntent = ExoPlayerActivity.getStartIntent(getApplicationContext(), VideoPlayerConfig.DEFAULT_VIDEO_URL);
                    startActivity(mIntent);
                    break;
            }
        }
    };


    private void showDialogPrompt() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInputURL = (EditText) promptsView
                .findViewById(R.id.editTextDialogUrlInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                          new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                boolean isURL = Patterns.WEB_URL.matcher(userInputURL.getText().toString().trim()).matches();
                                if (isURL) {
                                    Intent mIntent = ExoPlayerActivity.getStartIntent(MainActivity.this, userInputURL.getText().toString().trim());
                                    startActivity(mIntent);
                                } else {
                                    Toast.makeText(MainActivity.this, getString(R.string.error_message_url_not_valid), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(getApplicationContext(), "complete", Toast.LENGTH_SHORT).show();
                hideProgressBar();
                Intent mIntent = ExoPlayerActivity.getStartIntent(getApplicationContext(), DOWNLOAD_FILE_PATH);
                startActivity(mIntent);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    private void beginDownload(String path) {
        // File file=new File(getExternalFilesDir(null),"Dummy");
        String file_name = path.substring(path.lastIndexOf("/") + 1, path.length());
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), file_name);
        if (file.exists()) {
            file.delete();
        }
        DOWNLOAD_FILE_PATH = file.getPath();
        /*
        Create a DownloadManager.Request with all the information necessary to start the download
        "http://speedtest.ftp.otenet.gr/files/test10Mb.db"
         */
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path))
                .setTitle(file_name)// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                .setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
        final DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        if (downloadManager != null) {
            downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

                boolean downloading = true;

                while (downloading) {

                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadID);

                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_FAILED) {
                        mProgressBar.dismiss();
                    }

                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mProgressBar.setProgress((int) dl_progress);

                        }
                    });

                    //  Log.d(Constants.MAIN_VIEW_ACTIVITY, statusMessage(cursor));
                    cursor.close();
                }

            }
        }).start();
    }

    private void showProgressBar() {
        mProgressBar.setCancelable(false);
        mProgressBar.show();
    }

    private void hideProgressBar() {
        mProgressBar.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],@NonNull int[] grantResults) {
        if (requestCode ==   200) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start_download();
            } else {
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
