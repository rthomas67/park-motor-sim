package org.example.parkmotorsim;

/**
 * This is (apparently) the type of switch that exists inside the motor
 * to control the "park" wires.
 * input = YELLOW, normallyClosed = BLACK, and normallyOpen = RED
 * Output switched from BLACK to RED when the motor is in park position.
 *
 * Created by rthomas6 on 9/29/16.
 */
public class DpdtSwitchCenterOff {

    private WireConnection position1WireConnection;

    private WireConnection position2WireConnection;

    private WireConnection activeWireConnection;

    public DpdtSwitchCenterOff(Wire input, Wire position1Output, Wire position2Output) {

        position1WireConnection = new WireConnection();
        position1WireConnection.setWire1(input);
        position1WireConnection.setWire2(position1Output);

        position2WireConnection = new WireConnection();
        position2WireConnection.setWire1(input);
        position2WireConnection.setWire2(position2Output);

        toggleOff();

    }

    public boolean isPosition1On() {
        return (activeWireConnection == position1WireConnection);
    }

    public boolean isPosition2On() {
        return (activeWireConnection == position2WireConnection);
    }

    public boolean isPositionOff() {
        return (activeWireConnection == null);
    }

    public void toggleOff() {
        activeWireConnection = null;

    }

    public void toggleToPosition1() {
        activeWireConnection = position1WireConnection;

    }

    public void toggleToPosition2() {
        activeWireConnection = position2WireConnection;

    }

    public boolean isShorted() {
        return activeWireConnection != null &&
                activeWireConnection.getWire1() != null && activeWireConnection.getWire2() != null &&
                activeWireConnection.getWire1().getVoltage() != activeWireConnection.getWire2().getVoltage();

    }

}
