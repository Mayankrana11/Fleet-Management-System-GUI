package gui;

import vehicles.Vehicle;
import interfaces.FuelConsumable;
import vehicles.CargoShip;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;


public class VehiclePanel extends JPanel {
    private static final double FUEL_EPS = 0.01;

    private final Vehicle vehicle;
    private final JLabel lblId = new JLabel();
    private final JLabel lblModel = new JLabel();
    private final JLabel lblMileage = new JLabel();
    private final JLabel lblFuel = new JLabel();
    private final JLabel lblStatus = new JLabel();
    private final JButton btnRefuel = new JButton("Refuel");

    private final DecimalFormat df = new DecimalFormat("#0.0");

    public VehiclePanel(Vehicle vehicle) {
        this.vehicle = vehicle;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(260, 120));
        setMaximumSize(new Dimension(420, 120));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x2B3038), 1),
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));
        setBackground(new Color(0x0D1114));

        initContent();
        updateFields();
    }

    private void initContent() {
        JPanel left = new JPanel(new GridLayout(4,1,2,2));
        left.setOpaque(false);
        lblId.setForeground(Color.WHITE);
        lblModel.setForeground(Color.LIGHT_GRAY);
        lblMileage.setForeground(Color.WHITE);
        lblFuel.setForeground(Color.WHITE);

        lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblModel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMileage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFuel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        left.add(lblId);
        left.add(lblModel);
        left.add(lblMileage);
        left.add(lblFuel);

        JPanel right = new JPanel(new BorderLayout(6,6));
        right.setOpaque(false);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setBorder(BorderFactory.createEmptyBorder(6,10,6,10));
        lblStatus.setOpaque(true);
        lblStatus.setForeground(Color.WHITE);

        btnRefuel.setFocusable(false);
        Style.buttonStyle(btnRefuel);

        right.add(lblStatus, BorderLayout.CENTER);
        right.add(btnRefuel, BorderLayout.SOUTH);

        add(left, BorderLayout.CENTER);
        add(right, BorderLayout.EAST);

        if (!(vehicle instanceof FuelConsumable)) {
            btnRefuel.setEnabled(false);
            btnRefuel.setToolTipText("Not fuel-based");
        } else {
            btnRefuel.addActionListener(e -> {
                String s = JOptionPane.showInputDialog(this, "Enter amount (liters):", "Refuel "+vehicle.getId(), JOptionPane.PLAIN_MESSAGE);
                if (s == null) return;
                try {
                    double amt = Double.parseDouble(s.trim());
                    if (amt <= 0) throw new NumberFormatException();
                    ((FuelConsumable) vehicle).refuel(amt);
                    updateFields();
                    JOptionPane.showMessageDialog(this, "Refueled "+vehicle.getId(), "OK", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    public void updateFields() {
        lblId.setText(vehicle.getId());
        lblModel.setText(vehicle.getModel());
        lblMileage.setText("Mileage: " + df.format(vehicle.getCurrentMileage()) + " km");

        if (vehicle instanceof CargoShip && ((CargoShip) vehicle).isSailPowered()) {
            lblFuel.setText("Fuel: ∞ (Wind)");
        } else if (vehicle instanceof FuelConsumable) {
            try {
                lblFuel.setText("Fuel: " + df.format(((FuelConsumable) vehicle).getFuelLevel()) + " L");
            } catch (Exception e) {
                lblFuel.setText("Fuel: N/A");
            }
        } else {
            lblFuel.setText("Fuel: N/A");
        }

        // Status badge
        String status = "Running";
        Color bg = new Color(0x2ECC71);
        try {
            if (vehicle instanceof CargoShip && ((CargoShip) vehicle).isSailPowered()) {
                status = "Wind-Powered";
                bg = new Color(0x36C57F);
            } else if (vehicle instanceof FuelConsumable) {
                double f = ((FuelConsumable) vehicle).getFuelLevel();
                if (f <= FUEL_EPS) {           // <--- threshold fix
                    status = "Out-of-Fuel";
                    bg = Style.DANGER;
                }
            }
        } catch (Exception e) {
            status = "Unknown";
            bg = Style.WARN;
        }

        lblStatus.setText(status);
        lblStatus.setBackground(bg);
    }

    public Vehicle getVehicle() { return vehicle; }
}
