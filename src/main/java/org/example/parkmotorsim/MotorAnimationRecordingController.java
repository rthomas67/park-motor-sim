package org.example.parkmotorsim;

/**
 * Created by rthomas6 on 10/6/16.
 */
public interface MotorAnimationRecordingController {

    boolean isRecordingActive();

    long getRecordingStartTime();

    int getSpeed();

}
