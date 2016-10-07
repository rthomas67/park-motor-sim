package org.example.parkmotorsim;

/**
 * Models a permanent connection between two Wire objects.
 *
 * Note: The WiredComponent setWire1, and setWire2 methods both register this object
 * with the input argument Wire object as a DcVoltageChangedListener.
 * Note: The WiredComponent setWire1, and setWire2 methods both unregister this object
 * as a DcVoltageChangedListener from the previously set Wire object (if any).
 *
 * Any time either of the connected wire
 *
 * Created by rthomas6 on 9/29/16.
 */
public class WireConnection extends WiredComponent {

    @Override
    public boolean isShorted() {

        return
                (getWire1() != null && getWire2() != null) &&
                getWire1().getVoltage() != DcVoltage.FLOAT
                        && getWire2().getVoltage() != DcVoltage.FLOAT
                        && getWire1().getVoltage() != getWire2().getVoltage();
    }

    @Override
    public void voltageChanged(DcVoltagePowered dcVoltagePoweredObject, DcVoltage previousVoltage, DcVoltage newVoltage) {
        if (isShorted()) {
            CircuitFaultWatcher.setFaultMessage("Bummer. Somebody connected positive and negative directly across a WireConnection.  Crackle!  Poof!  Smolder....");
        } else if (dcVoltagePoweredObject == getWire1()) {
            // ENHANCE: If there is anything more to do on a wire1 voltage change, do it here.
        } else if (dcVoltagePoweredObject == getWire2()) {
            // ENHANCE: If there is anything more to do on a wire2 voltage change, do it here.
        } else {
            System.err.println("WARNING: WiredConnection getting notified of voltage changes on a " +
                    "DcVoltagePowered item that is not connected to it.  Verify whether unregistration is happening.");
        }

    }

}
