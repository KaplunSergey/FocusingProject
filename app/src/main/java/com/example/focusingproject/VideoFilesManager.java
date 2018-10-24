package com.example.focusingproject;

import java.io.*;
import java.sql.Timestamp;

public class VideoFilesManager {

    private static final String MP4 = ".mp4";
    private static final String TXT = ".txt";

    private String directory;
    private File videoFile;
    private File logFile;

    public VideoFilesManager(String directory) {
        this.directory = directory;
    }

    public boolean createVideoSessionFiles() {
        videoFile = null;
        logFile = null;

        String fileName = directory + "/" + System.currentTimeMillis();
        videoFile = new File(fileName + MP4);
        logFile = new File(fileName + TXT);
        try {
            return videoFile.createNewFile() && logFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getVideoPath() {
        return videoFile.getAbsolutePath();
    }

    public void writeFocusDistance(float focusDistance) {
        if (logFile == null || !logFile.exists()) {
            return;
        }

        try (BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true))){
            buf.append(new StringBuilder(new Timestamp(System.currentTimeMillis()).getTime() + " - Focus distance: " + focusDistance));
            buf.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
