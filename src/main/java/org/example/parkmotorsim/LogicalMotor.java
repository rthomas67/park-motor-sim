package org.example.parkmotorsim;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by rthomas6 on 9/29/16.
 */
public class LogicalMotor {

    private static final int PARK_POSITION_LOW = 175;

    private static final int PARK_POSITION_HIGH = 185;

    private static final int PARK_DEAD_ZONE_LOW = 160;

    private static final int PARK_DEAD_ZONE_HIGH = 200;

    private static final int SLOW_CYCLE_FREQUENCY = 3;

    private Map<WireId, Wire> wires = new HashMap<>();

    private ResistiveLoad lowSpeedCoil = new ResistiveLoad();

    private ResistiveLoad highSpeedCoil = new ResistiveLoad();

    private DpdtSwitchCenterOff parkSwitch;

    private Thread internalUpdateThread;

    private int cycle = 0;

    public LogicalMotor() {
        wires.put(WireId.RED, new Wire());
        wires.put(WireId.GREEN, new Wire());
        wires.put(WireId.BLUE, new Wire());
        wires.put(WireId.YELLOW, new Wire());
        wires.put(WireId.BLACK, new Wire());

        lowSpeedCoil.setWire1(wires.get(WireId.RED));
        lowSpeedCoil.setWire2(wires.get(WireId.GREEN));
        highSpeedCoil.setWire1(wires.get(WireId.RED));
        highSpeedCoil.setWire2(wires.get(WireId.BLUE));

        parkSwitch = new DpdtSwitchCenterOff(wires.get(WireId.YELLOW),
                wires.get(WireId.BLACK),
                wires.get(WireId.RED));

        internalUpdateThread = new Thread(updateLogic);
        internalUpdateThread.setDaemon(true);
        internalUpdateThread.start();
    }

    public DpdtSwitchCenterOff getParkSwitch() {
        return parkSwitch;
    }

    private Runnable updateLogic = new Runnable() {
        public void run() {
            while (true) {
                cycle++;
                try {
                    if (highSpeedCoil.isLoaded()) {
                        if (wires.get(WireId.RED).getVoltage() == DcVoltage.POSITIVE) {
                            advancePosition();
                        } else {
                            retreatPosition();
                        }
                    } else if (lowSpeedCoil.isLoaded() && (cycle % SLOW_CYCLE_FREQUENCY) == 0) {
                        if (wires.get(WireId.RED).getVoltage() == DcVoltage.POSITIVE) {
                            advancePosition();
                        } else {
                            retreatPosition();
                        }
                    }
                    if (parkSwitch.isShorted()) {
                        CircuitFaultWatcher.setFaultMessage("Bummer. Direct short across park switch.  It's dead Jim.");
                    }
                    //System.out.print(position + ".");
                    //System.out.flush();
                    Thread.sleep(5);  // Don't hog the CPU 100%, but update VERY frequently.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                updateParkSwitchStatus();
            }
        }
    };

    /**
     * The park "switch" is connects yellow to either black or
     * red, but not at the same time.  The following diagram shows
     * when during a rotation the wires are connected.
     *
     * Position:  0         90        180         270         360
     *      RED:                     *****
     *   YELLOW:  ***********************************************
     *    BLACK:  ****************           ********************
     *
     * Note: RED is also the common end of both motor coils, so it would normally have either
     * POSITIVE or NEGATIVE voltage.
     */
    private void updateParkSwitchStatus() {
        if (position < PARK_POSITION_HIGH && position > PARK_POSITION_LOW) {
            parkSwitch.toggleToPosition2(); // yellow to red
        } else if (position < PARK_DEAD_ZONE_HIGH && position > PARK_DEAD_ZONE_LOW) {
            parkSwitch.toggleOff();
        } else {
            parkSwitch.toggleToPosition1(); // yellow to black
        }
    }

    public Map<WireId, Wire> getWires() {
        return wires;
    }

    /**
     * values from 0 to 359 (360 should reset to 0) to represent degrees in a circle.
     */
    private int position = 0;

    public int getPosition() {
        return position;
    }

    private void advancePosition() {
        if (position == 359) {
            position = 0;
        } else {
            position++;
        }
    }

    private void retreatPosition() {
        if (position == 0) {
            position = 359;
        } else {
            position--;
        }
    }

    public DcVoltage getWireVoltage(WireId wire) {
        switch (wire) {
            case RED:
                break;
            case BLUE:
                break;
            case GREEN:
                break;
            case YELLOW:
                break;
            case BLACK:
                break;
            default:
                return DcVoltage.NEGATIVE;
        }
        return null;
    }


}
