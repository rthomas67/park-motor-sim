package org.example.parkmotorsim;

/**
 * Created by rthomas6 on 9/29/16.
 */
public enum WireId {
    RED, // common end of both coils (positive voltage to run forward)
    BLUE,  // other end of high speed coil (negative / ground to run forward)
    GREEN,  // other end of low speed coil (negative / ground to run forward)
    YELLOW,  // PARK indicator (connected to RED when motor is in park position range)
    BLACK // PARK "wire 2" (connected to yellow UNLESS motor is in park position range)
}
