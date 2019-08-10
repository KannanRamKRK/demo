package com.zoom.downloadmanager;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

public class DownloadVdo extends IntentService {
    private static final String DOWNLOAD_PATH="com.zoom.DownloadManager_DownloadVdo_Download_path";
    private static final String DESTINATION_PATH="com.zoom.DownloadManager_DownloadVdo_Destination_path";

    public DownloadVdo(){
        super.("DownloadVdo");

    }
    public static Intent getDownloadService(final @NonNull Context callingClassContext, final @NonNull String downloadPath,
                                            final  @NonNull String destinationPath){
        return new Intent(callingClassContext,DownloadVdo.class).putExtra(DOWNLOAD_PATH,downloadPath)
                .putExtra(DESTINATION_PATH,destinationPath);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String downloadPath = intent.getStringExtra(DOWNLOAD_PATH);
        String destinationPath= intent.getStringExtra(DESTINATION_PATH);
        startDownload(downloadPath,destinationPath);
    }

    private void startDownload(String downloadpath, String destinationpath) {
        Uri uri = Uri.parse(downloadpath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("Downloading");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(destinationpath, uri.getLastPathSegment());
        ((DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
    }
}
