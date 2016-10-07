package org.example.parkmotorsim;

/**
 * Same as SpstSwitch but with a special wrapper around Wire2 that toggles the voltage between its
 * real value and FLOAT on an emulated PWM cycle.
 *
 * The maximum semi-reliable pulse frequency in Java _should_be 1000 Hz, which would
 * mean every 1 millisecond would represent a simulated opportunity to toggle the output
 * on or off.  The maximum sampling rate would also be 1000 Hz so the probability of
 * sampling synchronization error would be very high.
 *
 * To mitigate issues with the sampling rate vs. PWM rate, the level of granularity
 * for observed voltage switches is increased by 10x.  For example, a 25% duty cycle
 * could be represented by reporting "on" for 1ms and "off" for 3ms, but will be represented
 * instead by blocks of 10ms on and 30ms off.  Sampling frequencies over 30 Hz should
 * result in reasonably accurate results without being overly jittery.
 *
 * Created by rthomas6 on 10/6/16.
 */
public class SpstSwitchWithPwmEmulation extends SpstSwitch {

    private static final int CYCLE_GRANULARITY = 10;

    private Wire pwmWire2;

    /**
     * Granularity of this is 100 so it is expressed as percentage on.
     * 25 would mean one out of every 4 cycles would report real voltage and
     * the remainder (75%) would report FLOAT (i.e. no voltage).
     */
    private int dutyCycle = 50;

    private boolean invertDutyCycle = false;

    private int dutyCycleDivider = 2;

    public SpstSwitchWithPwmEmulation() {
        // Keep default dutyCycle at 50% and dutyCycleDivider = 4
    }

    /**
     *@param dutyCycle percentage to be on
     *  e.g 50 => 2
     *      25 => 4
     *      33 => 3
     *      12 => 8
     *      75 => 4 inverted
     *      67 => 3 inverted (Note 66 would not work here)
     *      88 => 8 inverted (Note 87 would not work here)
     */
    public SpstSwitchWithPwmEmulation(int dutyCycle) {
        if (dutyCycle > 50) {
            this.dutyCycle = 100 - dutyCycle;
            this.invertDutyCycle = true;
        } else {
            this.dutyCycle = dutyCycle;
            this.invertDutyCycle = false;
        }
        dutyCycleDivider = (int)(100/this.dutyCycle);
    }

    public int getDutyCycle() {
        return dutyCycle;
    }

    public int getDutyCycleDivider() {
        return dutyCycleDivider;
    }

    boolean duringOnCycle() {
        boolean rawResult = (((long)(System.currentTimeMillis()/CYCLE_GRANULARITY)) % dutyCycleDivider == 0);
        return (invertDutyCycle) ? !rawResult : rawResult;
    }

    private class PwmWire extends Wire {

        private Wire realWire;

        PwmWire(Wire realWire) {
            this.realWire = realWire;
        }

        @Override
        public DcVoltage getVoltage() {
            if (isOn() && duringOnCycle()) {
                return realWire.getVoltage();
            } else {
                return DcVoltage.FLOAT;
            }
        }

        @Override
        public void setVoltage(DcVoltage newVoltage) {
            realWire.setVoltage(newVoltage);
        }

    }

    @Override
    public Wire getWire2() {
        if (super.getWire2() == null) {
            return null;
        } else {
            return pwmWire2;
        }
    }

    @Override
    public void setWire2(Wire wire2) {
        // This registers the switch for voltage change notifications
        // However, those notifications on a modulated voltage would be
        // too much overhead, so add it with suppressNotifications == true.
        pwmWire2 = new PwmWire(wire2);
        super.setWire2(pwmWire2, true);
    }
}
