package gui;

import fleet.FleetManager;
import simulation.HighwayCounter;
import simulation.SimulationController;
import vehicles.Vehicle;
import interfaces.FuelConsumable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

//maingui
public class MainWindow extends JFrame {
    private final FleetManager fleetManager;
    private final SimulationController controller = new SimulationController();

    private final ControlPanel controlPanel = new ControlPanel();
    private final HighwayPanel highwayPanel = new HighwayPanel();
    private final JPanel vehiclesContainer = new JPanel();
    private final JScrollPane vehiclesScroll;
    private final JTextArea logArea = new JTextArea(8, 80);

    // Race-condition demo labels
    private final JLabel lblUnsynced = new JLabel("Unsynced Distance: 0");
    private final JLabel lblSynced = new JLabel("Synced Distance: 0");

    public MainWindow(FleetManager fm) {
        super("Fleet Highway Simulator");
        this.fleetManager = fm;

        this.fleetManager.loadFromFile("fleet_data.csv");

        vehiclesContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
        vehiclesContainer.setBackground(new Color(0x0B0F13));

        vehiclesScroll = new JScrollPane(
                vehiclesContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        vehiclesScroll.getVerticalScrollBar().setUnitIncrement(12);
        vehiclesScroll.setPreferredSize(new Dimension(0, 180));

        initLayout();
        wireActions();
        loadFleet();

        setSize(1280, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) {
                onExit();
            }
        });
        setVisible(true);
    }

    private void initLayout() {
        getContentPane().setBackground(new Color(0x0F1724));
        setLayout(new BorderLayout(10,10));

        // -----------------------------------------------------
        // TOP BAR: control panel + race-condition counters
        // -----------------------------------------------------
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(controlPanel, BorderLayout.WEST);

        Style.labelStyle(lblUnsynced, 14);
        Style.labelStyle(lblSynced, 14);

        lblUnsynced.setForeground(Color.WHITE);
        lblSynced.setForeground(new Color(0x88FFAA)); // light green for contrast

        JPanel counterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        counterPanel.setOpaque(false);

        counterPanel.add(lblUnsynced);
        counterPanel.add(lblSynced);

        top.add(counterPanel, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // -----------------------------------------------------
        // HIGHWAY + VEHICLE LIST (SplitPane)
        // -----------------------------------------------------
        JPanel highwayWrapper = new JPanel(new BorderLayout());
        highwayWrapper.setOpaque(false);
        highwayWrapper.add(highwayPanel, BorderLayout.CENTER);

        JScrollPane fullHighwayScroll = new JScrollPane(
                highwayWrapper,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        fullHighwayScroll.getVerticalScrollBar().setUnitIncrement(12);
        fullHighwayScroll.getHorizontalScrollBar().setUnitIncrement(12);
        fullHighwayScroll.setBorder(null);

        vehiclesScroll.setPreferredSize(new Dimension(getWidth(), 220));

        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                fullHighwayScroll,
                vehiclesScroll
        );

        split.setResizeWeight(0.70);
        split.setDividerSize(6);
        split.setOpaque(false);
        add(split, BorderLayout.CENTER);

        // LOG PANEL
        logArea.setEditable(false);
        logArea.setBackground(new Color(0x071017));
        logArea.setForeground(Color.LIGHT_GRAY);

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(getWidth(), 140));
        add(logScroll, BorderLayout.SOUTH);
    }

    private void wireActions() {

        controlPanel.getBtnStart().addActionListener(e -> {
            controller.startAll();
            log("Simulation started.");
        });

        controlPanel.getBtnPause().addActionListener(e -> {
            controller.pauseAll();
            log("Simulation paused.");
        });

        controlPanel.getBtnResume().addActionListener(e -> {
            controller.resumeAll();
            log("Simulation resumed.");
        });

        controlPanel.getBtnStop().addActionListener(e -> {
            controller.stopAll();
            log("Simulation stopped.");
        });

        controlPanel.getBtnRefuelAll().addActionListener(e -> {
            String s = JOptionPane.showInputDialog(
                    this,
                    "Enter amount to refuel all fuel-based vehicles (liters):",
                    "Refuel All",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (s == null) return;

            try {
                double amt = Double.parseDouble(s.trim());
                if (amt <= 0) throw new NumberFormatException();

                int refueled = 0;
                for (Vehicle v : fleetManager.getFleet()) {
                    if (v instanceof FuelConsumable) {
                        try {
                            ((FuelConsumable) v).refuel(amt);
                            refueled++;
                        } catch (Exception ex) {}
                    }
                }
                controller.resumeAll();
                log("Refueled " + refueled + " vehicles.");
                refreshAll();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        controlPanel.getBtnImportCSV().addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(new File("."));
            int res = chooser.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                fleetManager.addFromExternalFile(f.getAbsolutePath());
                log("Imported vehicles from " + f.getName());
                loadFleet();
            }
        });

        controlPanel.getBtnAddVehicle().addActionListener(e -> {
            AddVehicleDialog dlg = new AddVehicleDialog(this);
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);

            if (dlg.isCreated()) {
                Vehicle v = dlg.getCreatedVehicle();
                try {
                    fleetManager.addVehicle(v);
                    fleetManager.saveToFile("fleet_data.csv");
                    log("Added vehicle: " + v.getId());
                    loadFleet();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Failed to add vehicle: " + ex.getMessage(),
                            "Add Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        controlPanel.getBtnRemoveVehicle().addActionListener(e -> {
            RemoveVehicleDialog dlg = new RemoveVehicleDialog(this, fleetManager);
            dlg.setLocationRelativeTo(this);
            dlg.setVisible(true);

            if (dlg.isRemoved()) {
                String id = dlg.getRemovedId();
                try {
                    fleetManager.removeVehicle(id);
                    fleetManager.saveToFile("fleet_data.csv");
                    log("Removed vehicle: " + id);
                    loadFleet();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error removing vehicle: " + ex.getMessage(),
                            "Remove Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        controlPanel.getBtnSaveFleet().addActionListener(e -> {
            fleetManager.saveToFile("fleet_data.csv");
            log("Fleet saved to disk.");
        });

        // UPDATE BOTH COUNTERS (race-condition demonstration)
        Timer uiTimer = new Timer(400, e -> {
            lblUnsynced.setText("Unsynced Distance: " + HighwayCounter.getUnsyncedDistance());
            lblSynced.setText("Synced Distance: " + HighwayCounter.getSyncedDistance());
            refreshAll();
        });
        uiTimer.start();
    }

    private void loadFleet() {
        List<Vehicle> list = fleetManager.getFleet();
        highwayPanel.setVehicles(list);

        SwingUtilities.invokeLater(() -> {
            highwayPanel.revalidate();
            highwayPanel.repaint();
        });

        vehiclesContainer.removeAll();
        for (Vehicle v : list) {
            VehiclePanel vp = new VehiclePanel(v);
            vehiclesContainer.add(vp);
        }

        vehiclesContainer.revalidate();
        vehiclesContainer.repaint();

        controller.initialize(list, this::refreshAll);
        log("Fleet loaded with " + list.size() + " vehicles.");
    }

    private void refreshAll() {
        SwingUtilities.invokeLater(() -> {
            for (Component c : vehiclesContainer.getComponents()) {
                if (c instanceof VehiclePanel) ((VehiclePanel)c).updateFields();
            }
            vehiclesContainer.revalidate();
            vehiclesContainer.repaint();
            highwayPanel.repaint();
        });
    }

    private void log(String s) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(s + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void onExit() {
        int ans = JOptionPane.showConfirmDialog(
                this,
                "Save fleet and exit?",
                "Exit",
                JOptionPane.YES_NO_CANCEL_OPTION
        );
        if (ans == JOptionPane.CANCEL_OPTION || ans == JOptionPane.CLOSED_OPTION) return;
        if (ans == JOptionPane.YES_OPTION) {
            fleetManager.saveOnExit();
            log("Fleet saved to disk.");
        }
        controller.stopAll();
        highwayPanel.stop();
        dispose();
        System.exit(0);
    }
}
