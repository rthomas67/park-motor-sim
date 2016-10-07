package org.example.parkmotorsim;

import java.util.Iterator;
import java.util.List;

/**
 * Created by rthomas6 on 10/6/16.
 */
public class MotorAnimator implements Runnable {

    private MotorController motorController;

    private List<MotorAnimationSample> samples;

    public MotorAnimator(MotorController motorController, List<MotorAnimationSample> samples) {
        this.motorController = motorController;
        this.samples = samples;

    }

    @Override
    public void run() {
        long animationStartTime = System.currentTimeMillis();
        long sampleSetBaseTime = samples.get(0).getTime();
        Iterator<MotorAnimationSample> samplesIterator = samples.iterator();
        MotorAnimationSample currentSample = samplesIterator.next();  // first
        long currentSampleTimeOffset = 0;  // would be sample[0].time - sample[0].time, so just set to zero
        while (samplesIterator.hasNext()) {
            long elapsed = System.currentTimeMillis() - animationStartTime;
            if (elapsed > currentSampleTimeOffset) {
                // Due for applying the sample to the motor controller
                motorController.setSpeed(currentSample.getSpeed());
                currentSample = samplesIterator.next();
                currentSampleTimeOffset = currentSample.getTime() - sampleSetBaseTime;
            }
        }

    }


}
