package com.thinking.video.permission;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by EvaLee on 2016/9/6.
 * 20160907 added by Eva_Lee for android M permission issue
 * SetupIntro.java->BaseActivity.java->Activity.java
 */
public class BaseActivity extends Activity {

    PermissionManagerActivity mPermissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPermissionManager = new PermissionManagerActivity();
        mPermissionManager.setContext(this);
        //checkSDKVersion only need to check the Permission at Android M
        Log.i("BaseActivity","Eva macate onCreate()->checkSDKVersion() = "+checkSDKVersion());
        if (checkSDKVersion())
            mPermissionManager.checkPermissionGranted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mPermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkSDKVersion() {
        if (Build.VERSION.SDK_INT >= 23) {
            return true;
        } else {
            return false;
        }
    }

    //20161115
    public boolean hasAllPermissions(){
       return mPermissionManager.hasAllPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPermissionManager.destory();
    }
}
