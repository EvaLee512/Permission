package com.example.evalee.runtimepermission;

import android.os.Bundle;

public class MainActivity extends PermissionActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
