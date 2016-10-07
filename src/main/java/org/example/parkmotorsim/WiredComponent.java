package org.example.parkmotorsim;

/**
 * Created by rthomas6 on 9/29/16.
 */
public abstract class WiredComponent implements DcVoltageChangedListener {

    private Wire wire1;

    private Wire wire2;

    public Wire getWire1() {
        return wire1;
    }

    public Wire getWire2() {
        return wire2;
    }

    public void setWire1(Wire wire1) {
        setWire1(wire1, false);
    }

    protected void setWire1(Wire wire1, boolean suppressNotifications) {
        DcVoltage previousVoltage = DcVoltage.FLOAT;
        if (this.wire1 != null) {
            previousVoltage = this.wire1.getVoltage();
            this.wire1.removeVoltageChangedListener(this);
        }
        this.wire1 = wire1;
        if (!suppressNotifications) {
            this.wire1.addVoltageChangedListener(this);
            // Report the initial change to the listener
            voltageChanged(wire1, previousVoltage, wire1.getVoltage());
        }
    }

    public void setWire2(Wire wire2) {
        setWire2(wire2, false);
    }

    protected void setWire2(Wire wire2, boolean suppressNotifications) {
        DcVoltage previousVoltage = DcVoltage.FLOAT;
        if (this.wire2 != null) {
            previousVoltage = this.wire2.getVoltage();
            this.wire2.removeVoltageChangedListener(this);
        }
        this.wire2 = wire2;
        if (!suppressNotifications) {
            this.wire2.addVoltageChangedListener(this);
            // Report the initial change to the listener
            voltageChanged(wire2, previousVoltage, wire2.getVoltage());
        }
    }

    public abstract boolean isShorted();

    public enum EndId {
        WIRE_1,
        WIRE_2
    }

}
