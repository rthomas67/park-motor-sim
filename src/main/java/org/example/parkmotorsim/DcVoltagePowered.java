package org.example.parkmotorsim;

/**
 * Created by rthomas6 on 9/30/16.
 */
public interface DcVoltagePowered {

    DcVoltage getVoltage();

    void setVoltage(DcVoltage dcVoltage);

    void addVoltageChangedListener(DcVoltageChangedListener voltageChangedListener);

    void removeVoltageChangedListener(DcVoltageChangedListener voltageChangedListener);

}
