package org.example.parkmotorsim.test;

import org.example.parkmotorsim.CircuitFaultWatcher;
import org.example.parkmotorsim.DcVoltage;
import org.example.parkmotorsim.Wire;
import org.example.parkmotorsim.WireConnection;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Created by rthomas6 on 10/3/16.
 */
public class WireConnectionTest {

    @Test
    public void testConnectionWithWiresShorted() {
        CircuitFaultWatcher.reset();
        Wire wire1 = new Wire();
        wire1.setVoltage(DcVoltage.POSITIVE);
        Wire wire2 = new Wire();
        wire2.setVoltage(DcVoltage.NEGATIVE);
        WireConnection wireConnection = new WireConnection();
        wireConnection.setWire1(wire1);
        wireConnection.setWire2(wire2);
        assertTrue("Short should have reported a circuit fault.", CircuitFaultWatcher.isFaulted());
    }

    @Test
    public void testConnectionWithNewWireCreatingAShort() {
        CircuitFaultWatcher.reset();
        Wire wire1 = new Wire();
        wire1.setVoltage(DcVoltage.POSITIVE);
        Wire wire2 = new Wire();
        wire2.setVoltage(DcVoltage.FLOAT);
        WireConnection wireConnection = new WireConnection();
        wireConnection.setWire1(wire1);
        wireConnection.setWire2(wire2);
        Wire wire3 = new Wire();
        wire3.setVoltage(DcVoltage.NEGATIVE);
        wireConnection.setWire2(wire3);
        assertTrue("Short should have reported a circuit fault.", CircuitFaultWatcher.isFaulted());
    }

    @Test
    public void testConnectionWithVoltageChangeOnWireCausingAShort() {
        CircuitFaultWatcher.reset();
        Wire wire1 = new Wire();
        wire1.setVoltage(DcVoltage.POSITIVE);
        Wire wire2 = new Wire();
        wire2.setVoltage(DcVoltage.FLOAT);
        WireConnection wireConnection = new WireConnection();
        wireConnection.setWire1(wire1);
        wireConnection.setWire2(wire2);
        assertTrue("Test should not have reported a circuit fault yet.", !CircuitFaultWatcher.isFaulted());
        wire2.setVoltage(DcVoltage.NEGATIVE);
        assertTrue("Short should have reported a circuit fault.", CircuitFaultWatcher.isFaulted());
    }

    @Test
    public void testConnectionWithWiresAtFloatVoltage() {
        CircuitFaultWatcher.reset();
        Wire wire1 = new Wire();
        wire1.setVoltage(DcVoltage.FLOAT);
        Wire wire2 = new Wire();
        wire2.setVoltage(DcVoltage.FLOAT);
        WireConnection wireConnection = new WireConnection();
        wireConnection.setWire1(wire1);
        wireConnection.setWire2(wire2);
        assertTrue("Test should not have reported a circuit fault.", !CircuitFaultWatcher.isFaulted());
    }

    @Test
    public void testConnectionWithWiresAtPositiveVoltage() {
        CircuitFaultWatcher.reset();
        Wire wire1 = new Wire();
        wire1.setVoltage(DcVoltage.POSITIVE);
        Wire wire2 = new Wire();
        wire2.setVoltage(DcVoltage.POSITIVE);
        WireConnection wireConnection = new WireConnection();
        wireConnection.setWire1(wire1);
        wireConnection.setWire2(wire2);
        assertTrue("Test should not have reported a circuit fault.", !CircuitFaultWatcher.isFaulted());
    }

    @Test
    public void testConnectionWithWiresAtNegativeVoltage() {
        CircuitFaultWatcher.reset();
        Wire wire1 = new Wire();
        wire1.setVoltage(DcVoltage.NEGATIVE);
        Wire wire2 = new Wire();
        wire2.setVoltage(DcVoltage.NEGATIVE);
        WireConnection wireConnection = new WireConnection();
        wireConnection.setWire1(wire1);
        wireConnection.setWire2(wire2);
        assertTrue("Test should not have reported a circuit fault.", !CircuitFaultWatcher.isFaulted());
    }



}
