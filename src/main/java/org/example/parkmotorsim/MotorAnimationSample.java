package org.example.parkmotorsim;

/**
 * Created by rthomas6 on 10/6/16.
 */
public class MotorAnimationSample {

    private long time;
    private int speed;

    MotorAnimationSample(long time, int speed) {
        this.time = time;
        this.speed = speed;
    }

    public long getTime() {
        return time;
    }

    public int getSpeed() {
        return speed;
    }
}
