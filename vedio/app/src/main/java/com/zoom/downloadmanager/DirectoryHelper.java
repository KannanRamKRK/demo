package com.zoom.downloadmanager;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;

import java.io.File;

public class DirectoryHelper extends ContextWrapper {

    public static final String ROOT_DIRECTORY_NAME = "DownloadManager";

    public DirectoryHelper(Context context) {
        super(context);
        createFolderDirectories();
    }

    public static void createDirectory(Context context){
        new DirectoryHelper(context);
    }
    private boolean isExternalStorageAvailable(){
        String exStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(exStorageState);
    }

    private void createFolderDirectories() {
        if (isExternalStorageAvailable())
            createDirectory(ROOT_DIRECTORY_NAME);
          }

          private void createDirectory(String directoryName){
        if(!isDirectoryExists(directoryName)){
            File file=new File(Environment.getExternalStorageDirectory(),directoryName);
            file.mkdir();
        }
          }
          private  boolean isDirectoryExists(String directoryName){
        File file = new File(Environment.getExternalStorageDirectory()+"/"+directoryName);
        return file.isDirectory()&& file.exists();
          }
}
