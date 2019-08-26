package com.androidwave.exoplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class Permissions extends Activity {

    private static final int REQUEST_ID_WRITE_PERMISSION = 200;
    private Context activity;

    public Boolean askPermissionAndWriteFile(Context context) {
        this.activity = context;
        Boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return canWrite;
    }

    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 24) {

            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(activity, permissionName);

         //   ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA}, PERMISSION_REQUEST_CODE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                ActivityCompat.requestPermissions((Activity) activity,
                        new String[]{
                                permissionName
                        },
                        requestId
                );
                return false;
            }
        } else {
            return true;
        }
        return true;
    }




}
