package simulation;

import vehicles.Vehicle;
import interfaces.FuelConsumable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

//simulation cycle
public class SimulationController {
    private final List<VehicleThread> vehicleThreads = new ArrayList<>();
    private boolean started = false;
    private Runnable uiUpdateCallback = null;

    public SimulationController() { }

//created threads
    public synchronized void initialize(List<Vehicle> vehicles, Runnable uiUpdateCallback) {
        stopAll(); // ensure clean state
        vehicleThreads.clear();

        this.uiUpdateCallback = uiUpdateCallback;

        if (vehicles != null) {
            for (Vehicle v : vehicles) {
                VehicleThread vt = new VehicleThread(v, uiUpdateCallback);
                vehicleThreads.add(vt);
            }
        }

        HighwayCounter.reset();
        started = false;
    }

    public synchronized void startAll() {
        if (started) return;
        for (VehicleThread vt : vehicleThreads) {
            vt.start();
        }
        started = true;
    }

    public synchronized void pauseAll() {
        for (VehicleThread vt : vehicleThreads) vt.pauseThread();
    }

    public synchronized void resumeAll() {
        for (VehicleThread vt : vehicleThreads) vt.resumeThread();
    }

    public synchronized void stopAll() {
        for (VehicleThread vt : vehicleThreads) vt.stopThread();
        // lightweight wait for threads to respond to interruption (non-blocking)
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {}
        started = false;
    }

    public synchronized List<VehicleThread> getVehicleThreads() {
        return new ArrayList<>(vehicleThreads);
    }

    public synchronized boolean isStarted() {
        return started;
    }

//refuel by id
    public synchronized void refuelVehicle(String vehicleId, double amount) throws Exception {
        for (VehicleThread vt : vehicleThreads) {
            Vehicle v = vt.getVehicle();
            if (v.getId().equals(vehicleId)) {
                if (v instanceof FuelConsumable) {
                    try {
                        ((FuelConsumable) v).refuel(amount);
                    } catch (Exception ex) {
                        // rethrow to caller if needed
                        throw ex;
                    }
                    // resume the vehicle thread in case it was waiting due to pause or similar
                    vt.resumeThread();

                    // ensure UI reflects new fuel level
                    if (uiUpdateCallback != null) SwingUtilities.invokeLater(uiUpdateCallback);
                }
                break;
            }
        }
    }
}
