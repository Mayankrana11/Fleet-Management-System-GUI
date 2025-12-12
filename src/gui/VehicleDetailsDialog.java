package gui;

import vehicles.Vehicle;
import interfaces.CargoCarrier;
import interfaces.PassengerCarrier;
import interfaces.FuelConsumable;
import vehicles.CargoShip;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

//vehicle details on click 
public class VehicleDetailsDialog extends JDialog {
    private final Vehicle v;
    private final DecimalFormat df = new DecimalFormat("#0.0");

    public VehicleDetailsDialog(Window owner, Vehicle v) {
        super(owner, "Details - " + v.getId(), ModalityType.APPLICATION_MODAL);
        this.v = v;
        initUI();
        setSize(460, 380);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(12,12));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        root.setBackground(new Color(0x0E1216));

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel(v.getId() + " — " + v.getModel());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(title, BorderLayout.WEST);

        // status chip
        JLabel chip = new JLabel();
        chip.setOpaque(true);
        chip.setForeground(Color.WHITE);
        chip.setBorder(BorderFactory.createEmptyBorder(6,10,6,10));
        chip.setHorizontalAlignment(SwingConstants.CENTER);
        String status = "Running";
        Color bg = new Color(0x2ECC71);
        try {
            if (v instanceof CargoShip && ((CargoShip) v).isSailPowered()) {
                status = "Wind-Powered";
                bg = new Color(0x36C57F);
            } else if (v instanceof FuelConsumable) {
                double f = ((FuelConsumable) v).getFuelLevel();
                if (f <= 0) { status = "Out-of-Fuel"; bg = Style.DANGER; }
            }
        } catch (Exception ignored) {}
        chip.setText(status);
        chip.setBackground(bg);
        header.add(chip, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        // center info area
        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setOpaque(false);
        info.setForeground(Color.WHITE);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(v.getId()).append("\n");
        sb.append("Model: ").append(v.getModel()).append("\n");
        sb.append("Max Speed: ").append(v.getMaxSpeed()).append(" km/h\n");
        sb.append("Current Mileage: ").append(df.format(v.getCurrentMileage())).append(" km\n");
        try {
            double eff = v.calculateFuelEfficiency();
            sb.append("Efficiency: ").append(df.format(eff)).append(" km/l\n");
        } catch (Exception e) {
            sb.append("Efficiency: N/A\n");
        }

        if (v instanceof CargoShip) {
            sb.append("Type: CargoShip\n");
            sb.append("Sail-powered: ").append(((CargoShip) v).isSailPowered()).append("\n");
        } else if (v instanceof PassengerCarrier) {
            sb.append("Type: PassengerCarrier\n");
            try {
                sb.append("Passengers: ").append(((PassengerCarrier) v).getCurrentPassengers()).append("/").append(((PassengerCarrier) v).getPassengerCapacity()).append("\n");
            } catch (Exception ignored) {}
        } else if (v instanceof CargoCarrier) {
            sb.append("Type: CargoCarrier\n");
            try {
                sb.append("Cargo: ").append(((CargoCarrier) v).getCurrentCargo()).append("/").append(((CargoCarrier) v).getCargoCapacity()).append(" kg\n");
            } catch (Exception ignored) {}
        } else {
            sb.append("Type: Generic Vehicle\n");
        }

        if (v instanceof CargoShip && ((CargoShip) v).isSailPowered()) {
            sb.append("Fuel: ∞ (Wind-powered)\n");
        } else if (v instanceof FuelConsumable) {
            try {
                sb.append("Fuel: ").append(df.format(((FuelConsumable) v).getFuelLevel())).append(" L\n");
            } catch (Exception ignored) { sb.append("Fuel: N/A\n"); }
        } else {
            sb.append("Fuel: N/A\n");
        }

        info.setText(sb.toString());

        JScrollPane sp = new JScrollPane(info);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);

        root.add(sp, BorderLayout.CENTER);

        // bottom actions
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        JButton btnClose = new JButton("Close");
        Style.buttonStyle(btnClose);
        btnClose.addActionListener(e -> dispose());
        bottom.add(btnClose);

        root.add(bottom, BorderLayout.SOUTH);

        setContentPane(root);
    }
}
