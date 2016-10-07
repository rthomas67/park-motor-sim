package org.example.parkmotorsim;

import javax.swing.JFrame;
import java.awt.BorderLayout;

/**
 * Created by rthomas6 on 9/29/16.
 */
public class MotorController {

    public static final int SPEED_MAX = 10;
    public static final int SPEED_MIN = -10;
    public static final int SPEED_STOPPED = 0;

    private LogicalMotor logicalMotor = new LogicalMotor();

    private SpstSwitch commonCoilPowerSwitch = new SpstSwitch(WiredComponent.EndId.WIRE_1);
    private Wire commonCoilControllerPin = new Wire();

    private SpstSwitch slowCoilPowerSwitch = new SpstSwitch(WiredComponent.EndId.WIRE_1);
    private SpstSwitch fastCoilPowerSwitch = new SpstSwitch(WiredComponent.EndId.WIRE_1);
    private Wire speedCoilControllerPin = new Wire();

    private int speed = SPEED_STOPPED;

    public MotorController() {
        // finish wiring everything up
        commonCoilControllerPin.setVoltage(DcVoltage.FLOAT);
        commonCoilPowerSwitch.toggleOff();
        commonCoilPowerSwitch.setWire1(commonCoilControllerPin);
        commonCoilPowerSwitch.setWire2(logicalMotor.getWires().get(WireId.RED));

        speedCoilControllerPin.setVoltage(DcVoltage.FLOAT);
        slowCoilPowerSwitch.toggleOff();
        slowCoilPowerSwitch.setWire1(speedCoilControllerPin);
        slowCoilPowerSwitch.setWire2(logicalMotor.getWires().get(WireId.GREEN));
        slowCoilPowerSwitch.toggleOn();

        fastCoilPowerSwitch.toggleOff();
        fastCoilPowerSwitch.setWire1(speedCoilControllerPin);
        fastCoilPowerSwitch.setWire2(logicalMotor.getWires().get(WireId.BLUE));
        // leave this one off.
    }

    public void runProgram() {
        while (true) {
            try {
                runMotorForward();
                Thread.sleep(13000);
                runMotorBackwards();
                Thread.sleep(4600);
                switchCoil();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Note: The motor is modeled with two different coils that run at
     * different maximum speeds.  The pwmSpeed setting is a PWM (effective voltage)
     * that runs the motor forwards or backwards relative to (i.e. less than) the maximum
     * speed of the activated coil.
     */
    private void switchCoil() {
        if (slowCoilPowerSwitch.isOn()) {
            slowCoilPowerSwitch.toggleOff();
            fastCoilPowerSwitch.toggleOn();
        } else {
            fastCoilPowerSwitch.toggleOff();
            slowCoilPowerSwitch.toggleOn();
        }
    }

    private void runMotorForward() {
        commonCoilPowerSwitch.toggleOff();
        commonCoilControllerPin.setVoltage(DcVoltage.POSITIVE);  // red wire
        speedCoilControllerPin.setVoltage(DcVoltage.NEGATIVE);  // blue or green wire
        commonCoilPowerSwitch.toggleOn();
    }

    private void runMotorBackwards() {
        commonCoilPowerSwitch.toggleOff();
        commonCoilControllerPin.setVoltage(DcVoltage.NEGATIVE);  // red wire
        speedCoilControllerPin.setVoltage(DcVoltage.POSITIVE); // blue or green wire
        commonCoilPowerSwitch.toggleOn();
    }

    public void setSpeed(int speed) {
        if (speed > SPEED_MAX || speed < SPEED_MIN) {
            throw new IllegalArgumentException("Speed " + speed +
                    " setting is out of range - MIN: " + SPEED_MIN + " to MAX: " + SPEED_MAX);
        }
        this.speed = speed;
    }

    public LogicalMotor getLogicalMotor() {
        return logicalMotor;
    }

    public static void main(String[] args) {
        MotorController motorController = new MotorController();
        JFrame frame = new JFrame("Parking Motor Simulator");

        ControlButtons controlButtons = new ControlButtons(motorController);
        frame.add(controlButtons, BorderLayout.NORTH);

        LogicalMotorDisplay logicalMotorDisplay = new LogicalMotorDisplay();
        logicalMotorDisplay.setLogicalMotor(motorController.getLogicalMotor());
        frame.add(logicalMotorDisplay, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        try {
            // Wait a few seconds for the UI to appear
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        motorController.runProgram();
    }

}
