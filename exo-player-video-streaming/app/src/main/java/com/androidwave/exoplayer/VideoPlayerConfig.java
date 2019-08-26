package com.androidwave.exoplayer;

import android.os.Environment;

import java.io.File;

public class VideoPlayerConfig {
    //Minimum Video you want to buffer while Playing
    public static final int MIN_BUFFER_DURATION = 6000;
    //Max Video you want to buffer during PlayBack
    public static final int MAX_BUFFER_DURATION = 15000;
    //Min Video you want to buffer before start Playing it
    public static final int MIN_PLAYBACK_START_BUFFER = 2500;
    //Min video You want to buffer when user resumes video
    public static final int MIN_PLAYBACK_RESUME_BUFFER = 5000;

    public static final String DEFAULT_VIDEO_URL =  Environment.getExternalStorageDirectory()+File.separator+"video_sample_3.webm";//"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    public static final String STREAMING_URL = "https://dw-digital-signage.s3.us-east-2.amazonaws.com/eduar.mp4";
}


//    //Minimum Video you want to buffer while Playing
//    public static final int MIN_BUFFER_DURATION = 3000;
//    //Max Video you want to buffer during PlayBack
//    public static final int MAX_BUFFER_DURATION = 5000;
//    //Min Video you want to buffer before start Playing it
//    public static final int MIN_PLAYBACK_START_BUFFER = 1500;
//    //Min video You want to buffer when user resumes video
//    public static final int MIN_PLAYBACK_RESUME_BUFFER = 5000;
