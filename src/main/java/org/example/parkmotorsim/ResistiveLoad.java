package org.example.parkmotorsim;

/**
 * For now, this represents a simple electrical device that consumes enough current
 * to prevent a direct short and does not consume more current than the circuit can supply.
 *
 * ENHANCEMENT: Check amps/volts on supply and compare with max/min load requirements.
 *
 * Created by rthomas6 on 9/29/16.
 */
public class ResistiveLoad extends WiredComponent {

    boolean isLoaded()
    {
        return getWire1().getVoltage() != DcVoltage.FLOAT
            && getWire2().getVoltage() != DcVoltage.FLOAT
            && getWire1().getVoltage() != getWire2().getVoltage();
    }

    @Override
    public boolean isShorted() {
        return false;  // loads can't really short since they consume the power.
    }

    @Override
    public void voltageChanged(DcVoltagePowered dcVoltagePoweredObject, DcVoltage previousVoltage, DcVoltage newVoltage) {
        if (dcVoltagePoweredObject == getWire1()) {
            // ENHANCE: If there is anything more to do on a wire1 voltage change, do it here.
        } else if (dcVoltagePoweredObject == getWire2()) {
            // ENHANCE: If there is anything more to do on a wire2 voltage change, do it here.
        } else {
            System.err.println("WARNING: ResistiveLoad getting notified of voltage changes on a " +
                    "DcVoltagePowered item that is not connected to it.  Verify whether unregistration is happening.");
        }
    }
}
