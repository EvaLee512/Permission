package com.thinking.video.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EvaLee on 2016/9/6.
 * 20160907 added by Eva_Lee for android M permission issue
 */
public class PermissionManagerActivity {
    private static final String TAG = "PerManActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    //20161115
    Boolean hasAllPermissions = false;

    Context mContext;

    public void setContext(Context _con) { //_con->BaseActivity
        mContext = _con;
    }

    //一次请求多个权限
    @TargetApi(23)
    public void checkPermissionGranted() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("Use MICROPHONE");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((Activity) mContext).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            ((Activity) mContext).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }

    @TargetApi(23)
    private boolean addPermission(List<String> permissionsList, String permission) {

        try {
            if (mContext.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!((Activity) mContext).shouldShowRequestPermissionRationale(permission))
                    return false;
            }
            return true;
        } catch (Exception e) {
        }
        return false;

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                       ) {//20161115
                    Log.i("PerManagerActivity","Eva macate All Permissions Granted");
                    hasAllPermissions = true;//20161115
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    hasAllPermissions = false;//20161115
                    Toast.makeText(mContext, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                    destory();
                }
            }
            break;
        }
    }

    public void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    //20161115
    public boolean hasAllPermissions(){
        return hasAllPermissions;
    }

    public void destory() {
        mContext = null;
        System.exit(1);
    }
}
