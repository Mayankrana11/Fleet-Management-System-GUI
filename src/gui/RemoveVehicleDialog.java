package gui;

import fleet.FleetManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import vehicles.Vehicle;

public class RemoveVehicleDialog extends JDialog {

    private boolean removed = false;
    private String removedId = null;

    public RemoveVehicleDialog(Window parent, FleetManager fm) {
        super(parent, "Remove Vehicle", ModalityType.APPLICATION_MODAL);

        List<Vehicle> list = fm.getFleet();
        String[] ids = list.stream().map(Vehicle::getId).toArray(String[]::new);

        JComboBox<String> cmb = new JComboBox<>(ids);
        JButton btnRemove = new JButton("Remove");
        JButton btnCancel = new JButton("Cancel");

        Style.buttonStyle(btnRemove);
        Style.buttonStyle(btnCancel);

        JPanel panel = new JPanel(new GridLayout(3,1,8,8));
        panel.setBackground(new Color(0x0D1114));
        panel.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));

        JLabel lbl = new JLabel("Select vehicle to remove:");
        Style.labelStyle(lbl, 14);

        panel.add(lbl);
        panel.add(cmb);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setOpaque(false);
        buttons.add(btnRemove);
        buttons.add(btnCancel);

        panel.add(buttons);
        setContentPane(panel);

        btnRemove.addActionListener(e -> {
            removedId = (String)cmb.getSelectedItem();
            removed = true;
            dispose();
        });

        btnCancel.addActionListener(e -> dispose());

        setSize(320, 200);
        setLocationRelativeTo(parent);
    }

    public boolean isRemoved() { return removed; }
    public String getRemovedId() { return removedId; }
}
