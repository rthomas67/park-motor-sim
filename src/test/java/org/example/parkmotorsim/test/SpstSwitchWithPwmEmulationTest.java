package org.example.parkmotorsim.test;

import org.example.parkmotorsim.DcVoltage;
import org.example.parkmotorsim.SpstSwitchWithPwmEmulation;
import org.example.parkmotorsim.Wire;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;


/**
 * Created by rthomas6 on 10/7/16.
 */
public class SpstSwitchWithPwmEmulationTest {

    @Test
    public void testPwm25() throws Exception {
        SpstSwitchWithPwmEmulation spstSwitch = new SpstSwitchWithPwmEmulation(25);
        assertThat("Duty cycle divider calculation error.", spstSwitch.getDutyCycleDivider(), equalTo(4));
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire1.setVoltage(DcVoltage.POSITIVE);
        wire2.setVoltage(DcVoltage.FLOAT);
        spstSwitch.toggleOn();
        int sampleCount = 0;
        int observedOnCount = 0;
        int observedOffCount = 0;
        Random random = new Random();
        Wire actualWire2 = spstSwitch.getWire2();  // This is really the PwmWire from the Pwm switch
        while (sampleCount++ < 10000) {
            if (actualWire2.getVoltage() == DcVoltage.POSITIVE) {
                observedOnCount++;
            }
            Thread.sleep(1);  // sample rate about 1000 Hz
        }
        assertThat("observedOnCount should have been higher", observedOnCount, greaterThan(2450));
        assertThat("observedOnCount should have been lower", observedOnCount, lessThan(2550));
        double percentOn = (double)observedOnCount / (double)sampleCount;
        assertTrue("Percent observed on should have been around 25%, but was " + percentOn, (percentOn > 0.24D && percentOn < 0.26D));
    }

    /**
     * Tests to verify that the internal PWM duty-cycle inversion works.
     * @throws Exception
     */
    @Test
    public void testPwm75() throws Exception {
        SpstSwitchWithPwmEmulation spstSwitch = new SpstSwitchWithPwmEmulation(75);
        assertThat("Duty cycle divider calculation error.", spstSwitch.getDutyCycleDivider(), equalTo(4));
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire1.setVoltage(DcVoltage.POSITIVE);
        wire2.setVoltage(DcVoltage.FLOAT);
        spstSwitch.toggleOn();
        int sampleCount = 0;
        int observedOnCount = 0;
        int observedOffCount = 0;
        Random random = new Random();
        Wire actualWire2 = spstSwitch.getWire2();  // This is really the PwmWire from the Pwm switch
        while (sampleCount++ < 10000) {
            if (actualWire2.getVoltage() == DcVoltage.POSITIVE) {
                observedOnCount++;
            }
            Thread.sleep(1);  // sample rate about 1000 Hz
        }
        assertThat("observedOnCount should have been higher", observedOnCount, greaterThan(7450));
        assertThat("observedOnCount should have been lower", observedOnCount, lessThan(7550));
        double percentOn = (double)observedOnCount / (double)sampleCount;
        assertTrue("Percent observed on should have been around 75%, but was " + percentOn, (percentOn > 0.74D && percentOn < 0.76D));
    }

}
