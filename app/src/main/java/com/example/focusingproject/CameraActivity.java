package com.example.focusingproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private Button buttonVideo;
    private boolean isRecordingVideo;
    private SeekBar seekBar;
    private CameraProvider cameraProvider;
    private TextView mode;

    private String manualMode;
    private String autoMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        final AutoFitTextureView textureView = findViewById(R.id.texture);
        buttonVideo = findViewById(R.id.video);
        seekBar = findViewById(R.id.seekBar);
        mode = findViewById(R.id.mode);

        manualMode = getResources().getString(R.string.manual);
        autoMode = getResources().getString(R.string.auto);

        cameraProvider = new CameraProvider(this, textureView);
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
        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeMode();
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

    private void changeMode() {
        final String modeText;
        if (mode.getText().equals(manualMode)) {
            seekBar.setVisibility(View.GONE);
            cameraProvider.changeFocusDistanceAuto(true);
            modeText = autoMode;
        } else {
            seekBar.setVisibility(View.VISIBLE);
            cameraProvider.changeFocusDistanceAuto(false);
            modeText = manualMode;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mode.setText(modeText);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateButtonText(final int text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buttonVideo.setText(text);
            }
        });
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
            updateButtonText(R.string.stop);
            isRecordingVideo = true;
        }

        @Override
        public void stopRecordingVideo() {
            updateButtonText(R.string.record);
            isRecordingVideo = false;
        }

        @Override
        public void showMessage(String message) {
            showToast(message);
        }
    };

}
