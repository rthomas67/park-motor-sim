package org.example.parkmotorsim;

/**
 * Created by rthomas6 on 9/30/16.
 */
public interface DcVoltageChangedListener {

    void voltageChanged(DcVoltagePowered dcVoltagePoweredObject, DcVoltage previousVoltage, DcVoltage newVoltage);

}
