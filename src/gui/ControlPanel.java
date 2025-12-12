package gui;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private final JButton btnStart = new JButton("Start");
    private final JButton btnPause = new JButton("Pause");
    private final JButton btnResume = new JButton("Resume");
    private final JButton btnStop = new JButton("Stop");
    private final JButton btnRefuelAll = new JButton("Refuel All");
    private final JButton btnImportCSV = new JButton("Import CSV");
    private final JButton btnAddVehicle = new JButton("Add Vehicle");
    private final JButton btnRemoveVehicle = new JButton("Remove Vehicle");
    private final JButton btnSaveFleet = new JButton("Save Fleet");

    public ControlPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        setBackground(new Color(0x1F1F2E));
        setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

        Style.buttonStyle(btnStart);
        Style.buttonStyle(btnPause);
        Style.buttonStyle(btnResume);
        Style.buttonStyle(btnStop);
        Style.buttonStyle(btnRefuelAll);
        Style.buttonStyle(btnImportCSV);
        Style.buttonStyle(btnAddVehicle);
        Style.buttonStyle(btnRemoveVehicle);
        Style.buttonStyle(btnSaveFleet);

        add(btnStart);
        add(btnPause);
        add(btnResume);
        add(btnStop);
        add(Box.createHorizontalStrut(12));
        add(btnRefuelAll);
        add(btnImportCSV);
        add(btnAddVehicle);
        add(btnRemoveVehicle);
        add(btnSaveFleet);
    }

    public JButton getBtnStart() { return btnStart; }
    public JButton getBtnPause() { return btnPause; }
    public JButton getBtnResume() { return btnResume; }
    public JButton getBtnStop() { return btnStop; }
    public JButton getBtnRefuelAll() { return btnRefuelAll; }
    public JButton getBtnImportCSV() { return btnImportCSV; }
    public JButton getBtnAddVehicle() { return btnAddVehicle; }
    public JButton getBtnRemoveVehicle() { return btnRemoveVehicle; }
    public JButton getBtnSaveFleet() { return btnSaveFleet; }
}
