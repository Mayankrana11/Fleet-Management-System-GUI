package gui;

import java.util.List;
import vehicles.Vehicle;
import interfaces.FuelConsumable;
import vehicles.CargoShip;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.*;

//highway panel with speed based movement (speed/200)
public class HighwayPanel extends JPanel {

    private final List<Vehicle> vehicles =
            Collections.synchronizedList(new ArrayList<>());

    private final Map<String, Float> smoothX =
            Collections.synchronizedMap(new HashMap<>());

    private final Map<String, Rectangle> lastBounds =
            Collections.synchronizedMap(new HashMap<>());

    // per-vehicle mileage baseline
    private final Map<String, Double> baseline =
            Collections.synchronizedMap(new HashMap<>());

    private final Timer repaintTimer;

    // visuals
    private int lanes = 1;
    private final int laneHeight = 60;
    private final int margin = 28;
    private int trackLengthPx = 1800;

    private final Color laneColor = new Color(0x0B1320);
    private final Color laneEdge = new Color(0x142029);

    // rolling window
    private final double visualWindowKm = 200.0;


    public HighwayPanel() {
        setOpaque(true);
        setBackground(new Color(0x091018));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        repaintTimer = new Timer(35, e -> {
            updateSmoothPositions();
            repaint();
        });
        repaintTimer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Vehicle clicked = findVehicleAt(e.getPoint());
                if (clicked != null) {
                    Window w = SwingUtilities.getWindowAncestor(HighwayPanel.this);
                    VehicleDetailsDialog dlg = new VehicleDetailsDialog(w, clicked);
                    dlg.setLocationRelativeTo(HighwayPanel.this);
                    dlg.setVisible(true);
                }
            }
        });
    }


    public void setVehicles(List<Vehicle> list) {
        vehicles.clear();

        if (list != null)
            vehicles.addAll(list);

        synchronized (vehicles) {
            lanes = Math.max(1, vehicles.size());

            for (Vehicle v : vehicles) {
                smoothX.putIfAbsent(v.getId(), (float) margin);
                baseline.put(v.getId(), v.getCurrentMileage());
            }
        }

        updatePreferredSize();
        revalidate();
        repaint();
    }


    private void updatePreferredSize() {
        int height = lanes * (laneHeight + 14) + margin * 2;
        int width = Math.max(1000, trackLengthPx + margin * 2);
        setPreferredSize(new Dimension(width, height));
    }


    /**
     * REALISTIC SPEED-BASED MOVEMENT
     */
    private void updateSmoothPositions() {
        synchronized (vehicles) {
            if (vehicles.isEmpty()) return;

            for (Vehicle v : vehicles) {

                baseline.putIfAbsent(v.getId(), v.getCurrentMileage());
                double startMileage = baseline.get(v.getId());

                // relative mileage since start
                double rel = v.getCurrentMileage() - startMileage;

                // visual speed factor
                double speedFactor = v.getMaxSpeed() / 200.0;  // tuned for smooth visual scaling

                // scaled visual movement
                double scaledRel = rel * speedFactor;

                double frac = (scaledRel % visualWindowKm) / visualWindowKm;

                float target = (float)(margin + frac * trackLengthPx);

                Float old = smoothX.get(v.getId());
                float current = (old != null ? old : target);

                // smooth interpolation
                float next = lerp(current, target, 0.2f);
                smoothX.put(v.getId(), next);
            }
        }
    }


    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }


    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);

        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(getBackground());
        g.fillRect(0,0,getWidth(),getHeight());

        int startY = margin;

        // lanes
        for (int i = 0; i < lanes; i++) {
            int y = startY + i * (laneHeight + 14);

            RoundRectangle2D lane = new RoundRectangle2D.Double(
                    margin, y,
                    getWidth() - margin * 2,
                    laneHeight,
                    10, 10
            );

            g.setColor(laneColor);
            g.fill(lane);

            int mid = y + laneHeight/2;

            g.setColor(new Color(0x1F2A34));
            g.setStroke(new BasicStroke(
                    1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                    0, new float[]{8f,12f}, 0f
            ));
            g.drawLine(margin + 12, mid, getWidth() - margin - 12, mid);

            g.setColor(laneEdge);
            g.setStroke(new BasicStroke(1f));
            g.drawRoundRect(margin, y,
                    getWidth() - margin*2,
                    laneHeight, 10, 10);
        }

        lastBounds.clear();

        synchronized (vehicles) {
            for (int i = 0; i < vehicles.size(); i++) {

                Vehicle v = vehicles.get(i);
                int y = startY + i * (laneHeight + 14);

                Float sx = smoothX.get(v.getId());
                float x = (sx != null ? sx : margin);

                Rectangle r = paintVehicle(g, v, (int)x, y, laneHeight);
                lastBounds.put(v.getId(), r);
            }
        }

        g.dispose();
    }


    private Rectangle paintVehicle(Graphics2D g, Vehicle v, int x, int y, int height) {

        int w = 120;
        int h = height - 12;
        int py = y + 6;

        Color base;

        try {
            if (v instanceof CargoShip ship && ship.isSailPowered()) {
                base = new Color(0x36C57F);
            } else if (v instanceof FuelConsumable fc) {

                double f = fc.getFuelLevel();
                if (f <= 0.01)
                    base = new Color(0xE74C3C);
                else
                    base = new Color(0x2ECC71);

            } else {
                base = new Color(0x4DA6FF);
            }
        } catch (Exception ex) {
            base = Color.YELLOW;
        }

        g.setColor(new Color(0,0,0,60));
        g.fillRoundRect(x+6, py + h - 6, w, 10, 10, 10);

        g.setColor(base);
        g.fillRoundRect(x, py, w, h, 12, 12);

        g.setColor(base.darker().darker());
        g.setStroke(new BasicStroke(2f));
        g.drawRoundRect(x, py, w, h, 12, 12);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g.drawString(v.getId(), x + 10, py + 18);

        g.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        String model = v.getModel();
        String display = model.length() > 18 ? model.substring(0, 15) + "..." : model;
        g.drawString(display, x + 10, py + 36);

        return new Rectangle(x, py, w, h);
    }


    private Vehicle findVehicleAt(Point p) {
        synchronized (vehicles) {
            for (Vehicle v : vehicles) {
                Rectangle r = lastBounds.get(v.getId());
                if (r != null && r.contains(p))
                    return v;
            }
        }
        return null;
    }


    public void stop() {
        if (repaintTimer != null)
            repaintTimer.stop();
    }
}
