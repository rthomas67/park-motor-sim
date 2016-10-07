package org.example.parkmotorsim;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

/**
 * Created by rthomas6 on 9/29/16.
 */
public class LogicalMotorDisplay extends JPanel implements ActionListener {

    private LogicalMotor logicalMotor;

    Timer timer;
    Rectangle.Float motorArm = new Rectangle.Float(180, 20, 40, 150);
    Color motorArmColor = Color.GRAY;

    Rectangle.Float blackWire = new Rectangle.Float(10, 10, 30, 10);
    Rectangle.Float yellowWire = new Rectangle.Float(10, 30, 30, 10);
    Rectangle.Float redWire = new Rectangle.Float(10, 50, 30, 10);

    public LogicalMotorDisplay() {
        timer = new Timer(10, this);
        timer.start();
    }

    public void setLogicalMotor(LogicalMotor logicalMotor) {
        this.logicalMotor = logicalMotor;
    }

    public void paint(Graphics g) {
        int h = getHeight();
        int w = getWidth();

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        AffineTransform originalXForm = g2d.getTransform();

        int degrees = logicalMotor.getPosition();
        g2d.drawString("Position: " + degrees, 30, 30);

        g2d.translate(300D, 30D);
        drawParkSwitch(g2d);
        g2d.setTransform(originalXForm);

        /*
         * This draws the motor arm before the graphics context is rotated translated or
         * scaled.
         */
        Color originalColor = g2d.getColor();
        g2d.setColor(motorArmColor);
        double pivotX = ((double)motorArm.x) + (motorArm.width / 2);
        double pivotY = ((double)motorArm.y) + ((double)motorArm.height) - (motorArm.width / 2);
        g2d.rotate(Math.toRadians(degrees), pivotX, pivotY);
        g2d.fill(motorArm);
        g2d.setColor(originalColor);

        g2d.setTransform(originalXForm);

    }

    private void drawParkSwitch(Graphics2D g2d) {
        Color originalColor = g2d.getColor();
        g2d.setColor(Color.YELLOW.darker());
        g2d.fill(yellowWire);
        g2d.setColor(Color.BLACK);
        g2d.fill(blackWire);
        g2d.setColor(Color.RED);
        g2d.fill(redWire);
        g2d.setColor(originalColor);
        Stroke originalStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(Color.DARK_GRAY);
        if (logicalMotor.getParkSwitch().isPosition1On()) {
            // This is most of the time... Yellow connected to black
            double yHalf = Math.abs(yellowWire.getY() - blackWire.getY()) / 2;
            double yBetween = Math.min(yellowWire.getY(), blackWire.getY()) + yHalf;
            double xArc = yellowWire.getX() + yellowWire.getWidth() + 10D;
            Path2D.Double connector = new Path2D.Double();
            connector.moveTo(yellowWire.getX() + yellowWire.getWidth(), yellowWire.getY() + yellowWire.getHeight()/2);
            connector.curveTo(yellowWire.getX() + yellowWire.getWidth(), yellowWire.getY() + yellowWire.getHeight()/2,
                    xArc, yBetween,
                    blackWire.getX() + blackWire.getWidth(), blackWire.getY() + blackWire.getHeight()/2);
            g2d.draw(connector);
        } else if (logicalMotor.getParkSwitch().isPosition2On()) {
            double yHalf = Math.abs(yellowWire.getY() - redWire.getY()) / 2;
            double yBetween = Math.min(yellowWire.getY(), redWire.getY()) + yHalf;
            double xArc = yellowWire.getX() + yellowWire.getWidth() + 10D;
            Path2D.Double connector = new Path2D.Double();
            connector.moveTo(yellowWire.getX() + yellowWire.getWidth(), yellowWire.getY() + yellowWire.getHeight()/2);
            connector.curveTo(yellowWire.getX() + yellowWire.getWidth(), yellowWire.getY() + yellowWire.getHeight()/2,
                    xArc, yBetween,
                    redWire.getX() + redWire.getWidth(), redWire.getY() + redWire.getHeight()/2);
            g2d.draw(connector);
        }
        // else don't draw a connector between yellow and anything.
        g2d.setColor(originalColor);
        g2d.setStroke(originalStroke);
    }

    public void actionPerformed(ActionEvent e) {
        // Note: events are sent to this by the Timer declared and started above

        repaint();
    }
}
