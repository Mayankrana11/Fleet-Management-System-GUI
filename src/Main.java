import fleet.FleetManager;
import gui.MainWindow;

import javax.swing.*;

//entry point main
public class Main {
    public static void main(String[] args) {

        // Create FleetManager (this also ensures fleet_data.csv exists)
        FleetManager fm = new FleetManager();

        // Load fleet from CSV (required for vehicles to appear in GUI)
        try {
            fm.loadFromFile("fleet_data.csv");
            System.out.println("Loaded fleet_data.csv successfully.");
        } catch (Exception e) {
            System.err.println("Error loading fleet_data.csv: " + e.getMessage());
        }

        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainWindow(fm);
        });
    }
}
