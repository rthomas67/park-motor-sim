package org.example.parkmotorsim;

/**
 * Created by rthomas6 on 9/30/16.
 */
public class MotorControllerEventLogicPark implements MotorControllerEventLogic {

    private MotorController motorController;

    private int parkPositionDeadzoneUpdateCount = 0;

    public MotorControllerEventLogicPark(MotorController motorController) {
        this.motorController = motorController;
    }

    @Override
    public void update(LogicalMotor logicalMotor) {
        if (logicalMotor.getParkSwitch().isPosition2On()) {

        }

    }
}
