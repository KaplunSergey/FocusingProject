package com.example.focusingproject.camera;

public interface CameraStatusCallback {

    void startRecordingVideo();

    void stopRecordingVideo();

    void showMessage(String message);

}
