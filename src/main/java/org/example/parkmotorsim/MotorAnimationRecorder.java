package org.example.parkmotorsim;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rthomas6 on 10/6/16.
 */
public class MotorAnimationRecorder implements Runnable {

    private long animationSampleRateInterval = 50;  // millis

    private long standbyInterval = 300;  // millis

    private long lastRecordingStartTime;

    private List<MotorAnimationSample> samples = new ArrayList<>();

    private Thread recordingThread;

    private MotorAnimationRecordingController motorAnimationRecordingController;

    public MotorAnimationRecorder(MotorAnimationRecordingController motorAnimationController) {
        this.motorAnimationRecordingController = motorAnimationRecordingController;
        recordingThread = new Thread(this);
    }

    @Override
    public void run() {

        while (true) {
            try {
                if (motorAnimationRecordingController.getRecordingStartTime() > lastRecordingStartTime) {
                    // A change in the recordingStartTime triggers resetting the recording data.
                    lastRecordingStartTime = motorAnimationRecordingController.getRecordingStartTime();
                    samples.clear();
                }
                if (motorAnimationRecordingController.isRecordingActive()) {
                    samples.add(new MotorAnimationSample(System.currentTimeMillis(), motorAnimationRecordingController.getSpeed()));
                    Thread.sleep(animationSampleRateInterval);
                } else {
                    Thread.sleep(standbyInterval);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
