package com.naveen.gqlandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloSubscriptionCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.naveen.gqlandroid.helpers.Utils;
import com.naveen.subscriptions.BoardContentSubscription;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {

    int loopCount;
    public static final String TAG = "MainActivity";
    File content;
    ApolloClient apolloClient;
    private long downloadId;
    Map<Long, File> downloads = new HashMap<>();

    PlayerView playerView;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == id) {
                    Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                    File file = downloads.get(downloadId);
                    downloads.remove(downloadId);
                    SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context);
                    playerView.setPlayer(player);

                    FileDataSource.Factory fileDataSource = new FileDataSourceFactory();
                    MediaSource mediaSource = new ProgressiveMediaSource.Factory(fileDataSource)
                            .createMediaSource(Uri.fromFile(file));
                    LoopingMediaSource loopingMediaSource = new LoopingMediaSource(mediaSource, loopCount);
                    player.prepare(loopingMediaSource);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.player_view);
        try {
            content = new File(this.getExternalFilesDir(null), Utils.FILE_DIR);
            Log.d(TAG, content.getAbsolutePath());
            if (!content.isDirectory()) {
                if (content.mkdirs()) {
                    Log.d(TAG, "Created content directory");
                } else {
                    Toast.makeText(this, "Unable to create content directory", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Unable to create content directory");
                }
            }
            registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        apolloClient = ApolloClient.builder()
                .serverUrl(Utils.BASE_URL)
                .subscriptionTransportFactory(new WebSocketSubscriptionTransport.Factory(Utils.BASE_URL, okHttpClient))
                .okHttpClient(okHttpClient)
                .build();

        BoardContentSubscription subscriptionQuery = BoardContentSubscription
                .builder()
                .apiKey(Utils.API_KEY)
                .mac(Utils.getMacAddr())
                .build();

        ApolloSubscriptionCall<BoardContentSubscription.Data> subscription = apolloClient.subscribe(subscriptionQuery);
        subscription.execute(new ApolloSubscriptionCall.Callback<BoardContentSubscription.Data>() {
            @Override
            public void onResponse(@NotNull Response<BoardContentSubscription.Data> response) {
                try {
                    String url = response.data().boardContent().node().content().url();
                    String id = response.data().boardContent().node().content().id();
                    loopCount = response.data().boardContent().node().loop();

                    String mime = getMimeType(url.toString());
                    File file = new File(content.getAbsoluteFile() + File.separator + id + ".mp4");
                    Log.d(TAG, file.getAbsolutePath());
                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    downloadId = downloadManager.enqueue(initializeRequest(file, mime, Uri.parse(url)));
                    downloads.put(downloadId, file.getAbsoluteFile());

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "Connection completed");
            }

            @Override
            public void onTerminated() {
                Log.d(TAG, "Connection Terminated");
            }

            @Override
            public void onConnected() {
                Log.d(TAG, "Subscribed Successfully");
            }
        });

    }

    public DownloadManager.Request initializeRequest(File file, String mime, Uri uri) {
        try {
            DownloadManager.Request request = new DownloadManager.Request(uri)
                    .setTitle("Digital Signage")
                    .setDescription("Downloading")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setDestinationUri(Uri.fromFile(file))
                    .setRequiresCharging(false)
                    .setMimeType(mime)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);

            return request;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
