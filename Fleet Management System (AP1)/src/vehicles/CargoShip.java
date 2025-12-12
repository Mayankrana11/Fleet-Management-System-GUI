package vehicles;

import interfaces.*;
import exceptions.*;

// cargoship class
public class CargoShip extends WaterVehicle implements FuelConsumable, CargoCarrier, Maintainable {

    private double fuelLevel;
    private double cargoCapacity = 20000;
    private double currentCargo;
    private boolean maintenanceNeeded;
    private boolean hasSail; // true if sail-powered, false if fuel-powered

    public CargoShip(String id, String model, double maxSpeed, boolean hasSail) {
        super(id, model, maxSpeed);
        this.fuelLevel = 0;
        this.currentCargo = 0;
        this.maintenanceNeeded = false;
        this.hasSail = hasSail;
    }

    //Getter for saving/loading
    public boolean isSailPowered() {
        return hasSail;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance < 0) {
            throw new InvalidOperationException("Invalid distance. Must be non-negative.");
        }

        if (hasSail) {
            addMileage(distance);
            System.out.println("Sailing with wind for " + distance + " km.");
        } else {
            double requiredFuel = distance / calculateFuelEfficiency();
            if (fuelLevel < requiredFuel) {
                throw new InsufficientFuelException("Not enough fuel, please refuel.");
            }
            fuelLevel -= requiredFuel;
            addMileage(distance);
            System.out.println("Sailing with engine for " + distance + " km.");
        }
    }

    @Override
    public double calculateFuelEfficiency() {
        return hasSail ? Double.POSITIVE_INFINITY : 2.0; // sail = infinite efficiency
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (hasSail) {
            throw new InvalidOperationException("This ship is sail-powered and cannot be refueled.");
        }
        if (amount <= 0) {
            throw new InvalidOperationException("Refuel amount must be positive.");
        }
        fuelLevel += amount;
    }

    @Override
    public double getFuelLevel() {
        return hasSail ? 0 : fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {
        if (hasSail) {
            return 0; // no fuel consumed
        }
        double required = distance / calculateFuelEfficiency();
        if (fuelLevel < required) {
            throw new InsufficientFuelException("Not enough fuel, please refuel.");
        }
        fuelLevel -= required;
        return required;
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Overload occurred, try reducing the load.");
        }
        currentCargo += weight;
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {
        if (weight > currentCargo) {
            throw new InvalidOperationException("Cannot unload more cargo than currently loaded.");
        }
        currentCargo -= weight;
    }

    @Override
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public double getCurrentCargo() {
        return currentCargo;
    }

    @Override
    public void scheduleMaintenance() {
        maintenanceNeeded = true;
    }

    @Override
    public boolean needsMaintenance() {
        return maintenanceNeeded || 
               (getCurrentMileage() - getLastMaintenanceMileage()) > 20000;
    }

    @Override
    public void performMaintenance() {
        maintenanceNeeded = false;
        setLastMaintenanceMileage(getCurrentMileage());
        System.out.println("CargoShip " + getId() + " has been serviced.");
    }
}
