package org.example.parkmotorsim.test;

import org.example.parkmotorsim.CircuitFaultWatcher;
import org.example.parkmotorsim.DcVoltage;
import org.example.parkmotorsim.SpstSwitch;
import org.example.parkmotorsim.Wire;
import org.example.parkmotorsim.WiredComponent;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Created by rthomas6 on 10/3/16.
 */
public class SpstSwitchTest {

    @Test
    public void testToggleOnWithLastFloat() {

        SpstSwitch spstSwitch = new SpstSwitch();
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire1.setVoltage(DcVoltage.POSITIVE);
        assertTrue("Wire1 should have been at DcVoltage.POSITIVE", spstSwitch.getWire1().getVoltage() == DcVoltage.POSITIVE);
        assertTrue("Wire2 should have been at DcVoltage.FLOAT", spstSwitch.getWire2().getVoltage() == DcVoltage.FLOAT);
        spstSwitch.toggleOn();
        assertTrue("Wire2 should have changed to DcVoltage.POSITIVE", spstSwitch.getWire2().getVoltage() == DcVoltage.POSITIVE);
    }

    @Test
    public void testToggleOffWithLastFloat() {

        SpstSwitch spstSwitch = new SpstSwitch();
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire1.setVoltage(DcVoltage.POSITIVE);
        assertTrue("Wire1 should have been at DcVoltage.POSITIVE", spstSwitch.getWire1().getVoltage() == DcVoltage.POSITIVE);
        assertTrue("Wire2 should have been at DcVoltage.FLOAT", spstSwitch.getWire2().getVoltage() == DcVoltage.FLOAT);
        spstSwitch.toggleOn();
        spstSwitch.toggleOff();
        assertTrue("Wire2 should have changed back to DcVoltage.FLOAT", spstSwitch.getWire2().getVoltage() == DcVoltage.FLOAT);
    }

    @Test
    public void testNoMasterToggleOnWithShort() {
        CircuitFaultWatcher.reset();
        SpstSwitch spstSwitch = new SpstSwitch();
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire1.setVoltage(DcVoltage.POSITIVE);
        wire2.setVoltage(DcVoltage.NEGATIVE);
        assertTrue("Setting opposite voltages on an SPST that is off should not have registered a fault.", !CircuitFaultWatcher.isFaulted());
        spstSwitch.toggleOn();
        assertTrue("Switching on an SPST with opposite voltages connected should have registered a fault.", CircuitFaultWatcher.isFaulted());
    }

    @Test
    public void testMasterWire1ToggleOnWithShort() {
        CircuitFaultWatcher.reset();
        SpstSwitch spstSwitch = new SpstSwitch(WiredComponent.EndId.WIRE_1);
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire1.setVoltage(DcVoltage.POSITIVE);
        wire2.setVoltage(DcVoltage.NEGATIVE);
        assertTrue("Setting opposite voltages on an SPST that is off should not have registered a fault. Fault message is: '" +
                CircuitFaultWatcher.getFaultMessage() + "'"
                , !CircuitFaultWatcher.isFaulted());
        spstSwitch.toggleOn();
        assertTrue("Switching on an SPST with opposite voltages connected should have registered a fault.", CircuitFaultWatcher.isFaulted());
    }

    @Test
    public void testMasterWire2ToggleOnWithShort() {
        CircuitFaultWatcher.reset();
        SpstSwitch spstSwitch = new SpstSwitch(WiredComponent.EndId.WIRE_2);
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire1.setVoltage(DcVoltage.POSITIVE);
        wire2.setVoltage(DcVoltage.NEGATIVE);
        assertTrue("Setting opposite voltages on an SPST that is off should not have registered a fault.", !CircuitFaultWatcher.isFaulted());
        spstSwitch.toggleOn();
        assertTrue("Switching on an SPST with opposite voltages connected should have registered a fault.", CircuitFaultWatcher.isFaulted());
    }

    @Test
    public void testToggleOnWithMasterWire1() {
        CircuitFaultWatcher.reset();
        SpstSwitch spstSwitch = new SpstSwitch(WiredComponent.EndId.WIRE_1);
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire1.setVoltage(DcVoltage.NEGATIVE);
        assertTrue("Wire1 should have been at DcVoltage.NEGATIVE", spstSwitch.getWire1().getVoltage() == DcVoltage.NEGATIVE);
        assertTrue("Wire2 should have been at DcVoltage.FLOAT", spstSwitch.getWire2().getVoltage() == DcVoltage.FLOAT);
        spstSwitch.toggleOn();
        assertTrue("Wire2 should have changed to DcVoltage.NEGATIVE", spstSwitch.getWire2().getVoltage() == DcVoltage.NEGATIVE);
    }

    @Test
    public void testToggleOnWithMasterWire2() {
        CircuitFaultWatcher.reset();
        SpstSwitch spstSwitch = new SpstSwitch(WiredComponent.EndId.WIRE_2);
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire2.setVoltage(DcVoltage.NEGATIVE);
        assertTrue("Wire1 should have been at DcVoltage.FLOAT", spstSwitch.getWire1().getVoltage() == DcVoltage.FLOAT);
        assertTrue("Wire2 should have been at DcVoltage.NEGATIVE", spstSwitch.getWire2().getVoltage() == DcVoltage.NEGATIVE);
        spstSwitch.toggleOn();
        assertTrue("Wire1 should have changed to DcVoltage.NEGATIVE", spstSwitch.getWire1().getVoltage() == DcVoltage.NEGATIVE);
    }

    @Test
    public void testVoltageChangeOnNonMasterToShortWhileOn() {
        CircuitFaultWatcher.reset();
        SpstSwitch spstSwitch = new SpstSwitch(WiredComponent.EndId.WIRE_1);
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire1.setVoltage(DcVoltage.POSITIVE);
        spstSwitch.toggleOn();
        assertTrue("Wire1 should have been at DcVoltage.POSITIVE", spstSwitch.getWire1().getVoltage() == DcVoltage.POSITIVE);
        assertTrue("Wire2 should have been at DcVoltage.POSITIVE", spstSwitch.getWire2().getVoltage() == DcVoltage.POSITIVE);
        assertTrue("Toggling on a switch with POSITIVE on master and FLOAT on the other wire should not have registered a fault.",
                !CircuitFaultWatcher.isFaulted());
        wire2.setVoltage(DcVoltage.NEGATIVE);  // create a short
        assertTrue("Changing the voltage on the non-master wire of a closed SPST switch should have registered a fault.",
                CircuitFaultWatcher.isFaulted());
    }

    @Test
    public void testVoltageChangeOnNonMasterToShortWhileOff() {
        CircuitFaultWatcher.reset();
        SpstSwitch spstSwitch = new SpstSwitch(WiredComponent.EndId.WIRE_1);
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire1.setVoltage(DcVoltage.POSITIVE);
        spstSwitch.toggleOn();
        assertTrue("Wire1 should have been at DcVoltage.POSITIVE", spstSwitch.getWire1().getVoltage() == DcVoltage.POSITIVE);
        assertTrue("Wire2 should have been at DcVoltage.POSITIVE", spstSwitch.getWire2().getVoltage() == DcVoltage.POSITIVE);
        assertTrue("Toggling on a switch with POSITIVE on master and FLOAT on the other wire should not have registered a fault.",
                !CircuitFaultWatcher.isFaulted());
        spstSwitch.toggleOff();
        wire2.setVoltage(DcVoltage.NEGATIVE);
        assertTrue("Changing the voltage on the non-master wire of an open SPST switch should not have registered a fault.",
                !CircuitFaultWatcher.isFaulted());
    }

    @Test
    public void testVoltageChangeOnMasterWhileOn() {
        CircuitFaultWatcher.reset();
        SpstSwitch spstSwitch = new SpstSwitch(WiredComponent.EndId.WIRE_1);
        Wire wire1 = new Wire();
        Wire wire2 = new Wire();
        spstSwitch.setWire1(wire1);
        spstSwitch.setWire2(wire2);
        wire1.setVoltage(DcVoltage.POSITIVE);
        spstSwitch.toggleOn();
        assertTrue("Wire1 should have been at DcVoltage.POSITIVE", spstSwitch.getWire1().getVoltage() == DcVoltage.POSITIVE);
        assertTrue("Wire2 should have been at DcVoltage.POSITIVE", spstSwitch.getWire2().getVoltage() == DcVoltage.POSITIVE);
        assertTrue("Toggling on a switch with POSITIVE on master and FLOAT on the other wire should not have registered a fault.",
                !CircuitFaultWatcher.isFaulted());
        wire1.setVoltage(DcVoltage.NEGATIVE);
        assertTrue("Changing the voltage on the master wire of a closed SPST switch should not have registered a fault.",
                !CircuitFaultWatcher.isFaulted());
        assertTrue("Wire2 should have been at DcVoltage.NEGATIVE", spstSwitch.getWire2().getVoltage() == DcVoltage.NEGATIVE);
    }

}
