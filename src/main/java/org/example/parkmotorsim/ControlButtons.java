package org.example.parkmotorsim;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by rthomas6 on 10/5/16.
 */
public class ControlButtons extends JPanel implements ItemListener, ChangeListener, MouseListener, MotorAnimationRecordingController {

    private MotorController motorController;

    private JCheckBox parkModeEnabled;

    private JSlider manualSpeed;

    /**
     * Checking this box should display instructions to set the start
     * speed on the slider, release the mouse button, and recording should
     * start upon the next mouse-down on the slider, ending at 60s or
     * when the mouse is released (whichever comes first).
     * While recording, the checkbox should be disabled for input.
     * At the end of recording, the checkbox should be re-enabled and de-selected.
     */
    private JCheckBox recordModeEnabled;

    private int recordingStartSpeed = 0;

    private long recordingStartTime = 0;

    private boolean recordingActive = false;

    public ControlButtons(MotorController motorController) {
        this.motorController = motorController;
        setLayout(new GridLayout(1, 3));
        parkModeEnabled = new JCheckBox("Park Enabled");
        add(parkModeEnabled);
        recordModeEnabled = new JCheckBox("Record");
        add(recordModeEnabled);
        manualSpeed = new JSlider(JSlider.HORIZONTAL,-10,10,0);
        manualSpeed.addMouseListener(this);
        add(manualSpeed);
    }

    public boolean isParkModeEnabled() {
        return parkModeEnabled.isSelected();
    }

    @Override
    public boolean isRecordingActive() {
        return recordingActive;
    }

    @Override
    public long getRecordingStartTime() {
        return recordingStartTime;
    }

    @Override
    public int getSpeed() {
        return manualSpeed.getValue();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }

    public static void main(String[] args) {
        MotorController motorController = new MotorController();
        JFrame frame = new JFrame("Test View ControlButtons");
        ControlButtons controlButtons = new ControlButtons(null);
        frame.add(controlButtons, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 100);
        frame.setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            int speed = (int)source.getValue();
            motorController.setSpeed(speed);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (recordModeEnabled.isSelected() && !recordModeEnabled.isEnabled()) {
            // Conditions indicate recording capture should start here.
            recordingActive = true;
            recordingStartTime = System.currentTimeMillis();
            // Until mouseReleased or maximum record time passes.
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (recordModeEnabled.isSelected() && recordModeEnabled.isEnabled()) {
            // Conditions indicate that the start speed is being set here...
            recordingStartSpeed = manualSpeed.getValue();
            recordModeEnabled.setEnabled(false);
        } else if (recordModeEnabled.isSelected()) {
            recordModeEnabled.setEnabled(true);
            recordModeEnabled.setSelected(false);
        }
        recordingActive = false;

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
