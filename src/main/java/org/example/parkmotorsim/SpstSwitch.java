package org.example.parkmotorsim;

/**
 * This type of switch simply shows the wires on either end of it
 * as connected or not.  It simulates this using two wire connections
 * that:
 * If connected, must all be set to the same DcVoltage value (otherwise a short will occur)
 * If not connected, can be set to different DcVoltage values with no "short detected".
 *
 * Procedure to change the input voltage.
 * Note: Input will be "sensed" as the first wire to have voltage "applied"
 *
 *
 * Created by rthomas6 on 9/29/16.
 */
public class SpstSwitch extends WiredComponent {

    private boolean on = false;

    private Wire lastFloatingWire;

    /**
     * If master is null (i.e. never set), the "lastFloatingWire" logic prevails.
     * Otherwise, if master does have a value, it dictates which end of the connection
     * (i.e. wire) "sends" the voltage.
     */
    private EndId master = null;

    public SpstSwitch() {

    }

    public SpstSwitch(EndId master) {
        this.master = master;
    }

    public EndId getMaster() {
        return master;
    }

    public void setMaster(EndId master) {
        this.master = master;
    }

    public boolean isOn() {
        return on;
    }

    public void toggleOn() {
        if (isPotentiallyShorted()) {
            CircuitFaultWatcher.setFaultMessage("Ouch. Closing a SPST switch when there are opposite voltages applied can light you up.");
        } else if (EndId.WIRE_1 == master) {
            getWire2().setVoltage(getWire1().getVoltage());
            // No update to lastFloating when master is set.
        } else if (EndId.WIRE_2 == master) {
            getWire1().setVoltage(getWire2().getVoltage());
            // No update to lastFloating when master is set.
        } else if (getWire1().getVoltage() == DcVoltage.FLOAT && getWire2().getVoltage() != DcVoltage.FLOAT) {
            // wire1 was unconnected, so it gets set to the wire2 value (if that's not also FLOAT)
            getWire1().setVoltage(getWire2().getVoltage());
            lastFloatingWire = getWire1();
        } else if (getWire2().getVoltage() == DcVoltage.FLOAT && getWire1().getVoltage() != DcVoltage.FLOAT) {
            // wire2 was unconnected, so it gets set to the wire1 value (if that's not also FLOAT)
            getWire2().setVoltage(getWire1().getVoltage());
            lastFloatingWire = getWire2();
        }
        on = true;
    }

    public void toggleOff() {
        if (EndId.WIRE_1 == master && getWire2() != null) {
            getWire2().setVoltage(DcVoltage.FLOAT);
            // No update to lastFloating when master is set.
        } else if (EndId.WIRE_2 == master && getWire1() != null) {
            getWire1().setVoltage(DcVoltage.FLOAT);
            // No update to lastFloating when master is set.
        } else if (lastFloatingWire != null && lastFloatingWire == getWire1()) {
            getWire1().setVoltage(DcVoltage.FLOAT);
        } else if (lastFloatingWire != null && lastFloatingWire == getWire2()) {
            getWire2().setVoltage(DcVoltage.FLOAT);
        }
        on = false;

    }

    @Override
    public boolean isShorted() {
        return isOn() && isPotentiallyShorted();
    }

    private boolean isPotentiallyShorted() {
        return getWire1() != null && getWire2() != null &&
                getWire1().getVoltage() != DcVoltage.FLOAT
                && getWire2().getVoltage() != DcVoltage.FLOAT
                && getWire1().getVoltage() != getWire2().getVoltage();
    }

    @Override
    public void voltageChanged(DcVoltagePowered dcVoltagePoweredObject, DcVoltage previousVoltage, DcVoltage newVoltage) {
        boolean bothWiresConnected = (getWire1() != null) && (getWire2() != null);
        if (dcVoltagePoweredObject == getWire1()) {
            if (bothWiresConnected && isOn() &&
                    ((master == null && lastFloatingWire == getWire2()) || master == EndId.WIRE_1)) {
                getWire2().setVoltage(getWire1().getVoltage());
            }
            // ENHANCE: If there is anything more to do on a wire1 voltage change, do it here.
        } else if (dcVoltagePoweredObject == getWire2()) {
            if (bothWiresConnected && isOn() &&
                    ((master == null && lastFloatingWire == getWire1()) || master == EndId.WIRE_2)) {
                getWire1().setVoltage(getWire2().getVoltage());
            }
            // ENHANCE: If there is anything more to do on a wire2 voltage change, do it here.
        } else {
            System.err.println("WARNING: WiredConnection getting notified of voltage changes on a " +
                    "DcVoltagePowered item that is not connected to it.  Verify whether unregistration is happening.");
        }
        if (isShorted()) {
            CircuitFaultWatcher.setFaultMessage("Bummer. Somebody connected positive and negative to a closed spst switch.  Sizzle.  Bye bye switch and/or other stuff.");
        }
    }
}
