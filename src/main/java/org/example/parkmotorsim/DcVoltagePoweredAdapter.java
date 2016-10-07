package org.example.parkmotorsim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class may not be strictly necessary except for semantics.
 * Created by rthomas6 on 9/29/16.
 */
public class DcVoltagePoweredAdapter implements DcVoltagePowered {

    /**
     * Initialize to FLOAT to avoid wire-up-time faults.
     * However, wire-up routines should initialize voltage to POSITIVE or NEGATIVE
     * in most cases.
     */
    private DcVoltage voltage = DcVoltage.FLOAT;

    private List<DcVoltageChangedListener> dcVoltageChangedListeners = new ArrayList<>();

    public DcVoltage getVoltage() {
        return voltage;
    }

    public void setVoltage(DcVoltage newVoltage) {
        DcVoltage previousVoltage = this.voltage;
        this.voltage = newVoltage;
        for (DcVoltageChangedListener listener : dcVoltageChangedListeners) {
            listener.voltageChanged(this, previousVoltage, newVoltage);
        }
    }

    @Override
    public void addVoltageChangedListener(DcVoltageChangedListener voltageChangedListenerToAdd) {
        dcVoltageChangedListeners.add(voltageChangedListenerToAdd);
    }

    @Override
    public void removeVoltageChangedListener(DcVoltageChangedListener voltageChangedListenerToRemove) {
        for (Iterator<DcVoltageChangedListener> i = dcVoltageChangedListeners.iterator(); i.hasNext(); ) {
            DcVoltageChangedListener listener = i.next();
            if (voltageChangedListenerToRemove == listener) {
                i.remove();
            }
        }
    }
}
