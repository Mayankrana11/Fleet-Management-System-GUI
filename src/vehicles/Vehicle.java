package vehicles;

import exceptions.InvalidOperationException;
import exceptions.InsufficientFuelException;

//base abstract vehicle class
public abstract class Vehicle implements Comparable<Vehicle> {

    private String id;
    private String model;
    private double maxSpeed;
    private double currentMileage;

    // Track mileage at last maintenance
    private double lastMaintenanceMileage = 0.0;

    public Vehicle(String id, String model, double maxSpeed) {
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = 0.0;
    }

    public abstract void move(double distance)
            throws InvalidOperationException, InsufficientFuelException;

    public abstract double calculateFuelEfficiency();
    public abstract double estimateJourneyTime(double distance);


    // Getters and Setters
    public String getId() { return id; }
    public String getModel() { return model; }
    public double getMaxSpeed() { return maxSpeed; }
    public double getCurrentMileage() { return currentMileage; }

    public void setCurrentMileage(double mileage) {
        this.currentMileage = Math.max(0, mileage);
    }

    public void addMileage(double distance) {
        if (distance > 0) currentMileage += distance;
    }

    public double getLastMaintenanceMileage() { return lastMaintenanceMileage; }
    public void setLastMaintenanceMileage(double mileage) { this.lastMaintenanceMileage = mileage; }

    // Comparison
    @Override
    public int compareTo(Vehicle other) {
        return Double.compare(other.calculateFuelEfficiency(), this.calculateFuelEfficiency());
    }

    public void displayInfo() {
        System.out.println("ID: " + id +
                ", Model: " + model +
                ", Max Speed: " + maxSpeed +
                " km/h, Mileage: " + currentMileage + " km");
    }
}
