package com.example.focusingproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.focusingproject.camera.AutoFitTextureView;
import com.example.focusingproject.camera.CameraProvider;
import com.example.focusingproject.camera.CameraStatusCallback;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_PERMISSIONS = 1;
    private static final String VIDEO_PERMISSIONS = Manifest.permission.CAMERA;

    private Button buttonVideo;
    private boolean isRecordingVideo;
    private SeekBar seekBar;
    private CameraProvider cameraProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        final AutoFitTextureView textureView = findViewById(R.id.texture);
        buttonVideo = findViewById(R.id.video);
        seekBar = findViewById(R.id.seekBar);

        cameraProvider = new CameraProvider(this, textureView);

        if (ActivityCompat.checkSelfPermission(this, VIDEO_PERMISSIONS) != PackageManager.PERMISSION_GRANTED) {
            requestVideoPermissions();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraProvider.setStatusCallback(statusCallback);
        seekBar.setOnSeekBarChangeListener(seekBarListener);
        buttonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecordingVideo) {
                    cameraProvider.stopRecordingVideo();
                } else {
                    cameraProvider.startRecordingVideo();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraProvider.startCameraPreview();
    }

    @Override
    public void onPause() {
        cameraProvider.stopCameraPreview();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraProvider.setStatusCallback(null);
        seekBar.setOnSeekBarChangeListener(null);
        buttonVideo.setOnClickListener(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
            showToast("Permission denied");
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestVideoPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, VIDEO_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, new String[]{VIDEO_PERMISSIONS}, REQUEST_VIDEO_PERMISSIONS);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            cameraProvider.changeFocusDistance(i);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private CameraStatusCallback statusCallback = new CameraStatusCallback() {
        @Override
        public void startRecordingVideo() {
            buttonVideo.setText(R.string.stop);
            isRecordingVideo = true;
        }

        @Override
        public void stopRecordingVideo() {
            isRecordingVideo = false;
            buttonVideo.setText(R.string.record);
        }

        @Override
        public void showMessage(String message) {
            showToast(message);
        }
    };
}
