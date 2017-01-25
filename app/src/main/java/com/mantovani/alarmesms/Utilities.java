package com.mantovani.alarmesms;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Mantovani on 25-Jan-17.
 */

public class Utilities {
    public static boolean askForPermissionIfNeeded (Activity activity, String permission) {
        // requestCode is only used in implementation of onRequestPermissionResult, which is not
        // used here. Therefore, a simple constant is used
        final int requestCode = 1;

        if (ContextCompat.checkSelfPermission(activity, permission) !=
                PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        }

        return ContextCompat.checkSelfPermission(activity, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }
}
