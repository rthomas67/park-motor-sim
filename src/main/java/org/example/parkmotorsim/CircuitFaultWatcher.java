package org.example.parkmotorsim;

/**
 * Singleton that centralizes the reporting of circuit faults so that a main thread can choose how to deal with
 * faults (exiting the program... stopping a simulation loop, painting a smoke cloud on a display, etc.)
 * Created by rthomas6 on 10/3/16.
 */
public class CircuitFaultWatcher {

    private static String faultMessage = null;

    private static boolean instantKill = false;

    public static void setFaultMessage(String faultMessage) {
        CircuitFaultWatcher.faultMessage = faultMessage;
        if (instantKill) {
            System.err.println(faultMessage);
            System.exit(86);
        }
    }

    public static String getFaultMessage() {
        return faultMessage;
    }

    public static boolean isFaulted() {
        return faultMessage != null;
    }

    public static boolean isInstantKill() {
        return instantKill;
    }

    public static void setInstantKill(boolean instantKill) {
        CircuitFaultWatcher.instantKill = instantKill;
    }

    public static void reset() {
        faultMessage = null;
    }

}
