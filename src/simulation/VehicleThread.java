package simulation;

import vehicles.Vehicle;
import exceptions.InsufficientFuelException;
import exceptions.InvalidOperationException;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

//single vehicle per thread
public class VehicleThread implements Runnable {
    private final Vehicle vehicle;
    private Thread thread;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private final AtomicBoolean pauseFlag = new AtomicBoolean(false);

    // UI update callback
    private final Runnable uiUpdateCallback;

    public VehicleThread(Vehicle vehicle, Runnable uiUpdateCallback) {
        this.vehicle = vehicle;
        this.uiUpdateCallback = uiUpdateCallback;
    }

    public void start() {
        if (running.get()) return;
        running.set(true);
        stopped.set(false);
        thread = new Thread(this, "VehicleThread-" + vehicle.getId());
        thread.start();
    }

    public void pauseThread() {
        pauseFlag.set(true);
    }

    public void resumeThread() {
        synchronized (pauseFlag) {
            pauseFlag.set(false);
            pauseFlag.notifyAll();
        }
    }

    public void stopThread() {
        stopped.set(true);
        running.set(false);
        resumeThread(); // wake thread if waiting
        if (thread != null) thread.interrupt();
    }

    @Override
    public void run() {
        while (!stopped.get()) {

            // Pause handling
            if (pauseFlag.get()) {
                synchronized (pauseFlag) {
                    try {
                        pauseFlag.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            if (stopped.get()) break;

            try {
                // Try moving (may fail due to no fuel)
                try {
                    vehicle.move(1.0);
                } catch (InsufficientFuelException e) {
                    // Out-of-fuel → vehicle stops moving but thread continues
                }

                // WRONG — unsynchronized access (race condition)
                HighwayCounter.incrementUnsynced();

                // CORRECT — synchronized access (ReentrantLock)
                HighwayCounter.incrementSynced();

                // Update GUI
                if (uiUpdateCallback != null) {
                    SwingUtilities.invokeLater(uiUpdateCallback);
                }

                Thread.sleep(1000);

            } catch (InvalidOperationException e) {
                stopped.set(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        running.set(false);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
