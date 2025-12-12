package gui;

import vehicles.*;
import javax.swing.*;
import java.awt.*;

//vehicle add screen
public class AddVehicleDialog extends JDialog {
    private final JTextField tfId = new JTextField();
    private final JTextField tfModel = new JTextField();
    private final JTextField tfMaxSpeed = new JTextField();
    private final JComboBox<String> cbType = new JComboBox<>(new String[]{"Car","Truck","Bus","Airplane","CargoShip"});
    private final JCheckBox chkSail = new JCheckBox("Sail-powered (CargoShip)");
    private final JLabel lblSail = new JLabel(""); // placeholder label cell for layout symmetry

    private boolean created = false;
    private Vehicle createdVehicle = null;

    public AddVehicleDialog(Window owner) {
        super(owner, "Add Vehicle", ModalityType.APPLICATION_MODAL);
        initUI();
        setSize(440, 260);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        Style.panelStyle(root);

        // Using GridBagLayout so we can hide/show the sail row cleanly.
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0; g.weightx = 0.25;
        form.add(new JLabel("Type:"), g);
        g.gridx = 1; g.gridy = 0; g.weightx = 0.75;
        form.add(cbType, g);

        g.gridx = 0; g.gridy = 1; g.weightx = 0.25;
        form.add(new JLabel("ID:"), g);
        g.gridx = 1; g.gridy = 1; g.weightx = 0.75;
        form.add(tfId, g);

        g.gridx = 0; g.gridy = 2; g.weightx = 0.25;
        form.add(new JLabel("Model:"), g);
        g.gridx = 1; g.gridy = 2; g.weightx = 0.75;
        form.add(tfModel, g);

        g.gridx = 0; g.gridy = 3; g.weightx = 0.25;
        form.add(new JLabel("Max Speed (km/h):"), g);
        g.gridx = 1; g.gridy = 3; g.weightx = 0.75;
        form.add(tfMaxSpeed, g);

        // Sail row (label + checkbox). Start hidden (only appears for CargoShip).
        g.gridx = 0; g.gridy = 4; g.weightx = 0.25;
        form.add(lblSail, g); // empty label to keep grid consistent
        g.gridx = 1; g.gridy = 4; g.weightx = 0.75;
        form.add(chkSail, g);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        JButton ok = new JButton("Add");
        JButton cancel = new JButton("Cancel");
        Style.buttonStyle(ok);
        Style.buttonStyle(cancel);
        buttons.add(ok);
        buttons.add(cancel);

        ok.addActionListener(e -> onAdd());
        cancel.addActionListener(e -> dispose());

        root.add(form, BorderLayout.CENTER);
        root.add(buttons, BorderLayout.SOUTH);
        setContentPane(root);

        // initial visibility: checkbox hidden until CargoShip selected
        setSailRowVisible(false);

        cbType.addActionListener(e -> {
            String sel = (String) cbType.getSelectedItem();
            setSailRowVisible("cargoship".equalsIgnoreCase(sel));
        });
    }

    private void setSailRowVisible(boolean visible) {
        chkSail.setVisible(visible);
        lblSail.setVisible(visible); // keep layout cell consistent
        // Revalidate parent so layout updates immediately
        SwingUtilities.invokeLater(() -> {
            chkSail.getParent().revalidate();
            chkSail.getParent().repaint();
        });
    }

    private void onAdd() {
        String type = ((String) cbType.getSelectedItem()).trim();
        String id = tfId.getText().trim();
        String model = tfModel.getText().trim();
        String ms = tfMaxSpeed.getText().trim();

        if (id.isEmpty() || model.isEmpty() || ms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double maxSpeed;
        try {
            maxSpeed = Double.parseDouble(ms);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid max speed", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Vehicle v;
            switch (type.toLowerCase()) {
                case "car":
                    v = new Car(id, model, maxSpeed, 4);
                    break;
                case "truck":
                    v = new Truck(id, model, maxSpeed, 6);
                    break;
                case "bus":
                    v = new Bus(id, model, maxSpeed, 6);
                    break;
                case "airplane":
                    v = new Airplane(id, model, maxSpeed, 10000.0);
                    break;
                case "cargoship":
                case "cargo ship":
                case "ship":
                    boolean sail = chkSail.isSelected();
                    v = new CargoShip(id, model, maxSpeed, sail);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Unknown type", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            // success: return vehicle to caller
            this.createdVehicle = v;
            this.created = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to create vehicle: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isCreated() { return created; }
    public Vehicle getCreatedVehicle() { return createdVehicle; }
    public String getCreatedId() { return createdVehicle != null ? createdVehicle.getId() : null; }
}
