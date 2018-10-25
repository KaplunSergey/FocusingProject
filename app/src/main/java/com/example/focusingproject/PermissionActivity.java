package com.example.focusingproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class PermissionActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_PERMISSIONS = 1;
    private static final String VIDEO_PERMISSIONS = Manifest.permission.CAMERA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, VIDEO_PERMISSIONS) != PackageManager.PERMISSION_GRANTED) {
            requestVideoPermissions();
        } else {
            launchCameraActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                launchCameraActivity();
            }
        }
    }


    private void requestVideoPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{VIDEO_PERMISSIONS}, REQUEST_VIDEO_PERMISSIONS);
    }

    private void launchCameraActivity() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
