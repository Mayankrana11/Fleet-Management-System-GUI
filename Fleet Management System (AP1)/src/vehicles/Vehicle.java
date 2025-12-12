package vehicles;

import exceptions.InvalidOperationException;

// Abstract base class for all vehicles
public abstract class Vehicle implements Comparable<Vehicle> {

    private String id;
    private String model;
    private double maxSpeed;
    private double currentMileage;

    //New field to track mileage at last maintenance
    private double lastMaintenanceMileage = 0.0;

    public Vehicle(String id, String model, double maxSpeed) {
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = 0.0;
    }

    public abstract void move(double distance) throws InvalidOperationException, Exception;
    public abstract double calculateFuelEfficiency();
    public abstract double estimateJourneyTime(double distance);

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getCurrentMileage() {
        return currentMileage;
    }

    // Update mileage
    protected void addMileage(double distance) {
        if (distance > 0) {
            currentMileage += distance;
        }
    }

    //Maintenance tracking methods
    public double getLastMaintenanceMileage() {
        return lastMaintenanceMileage;
    }

    public void setLastMaintenanceMileage(double mileage) {
        this.lastMaintenanceMileage = mileage;
    }

    // sort by fuel efficiency
    @Override
    public int compareTo(Vehicle other) {
        return Double.compare(this.calculateFuelEfficiency(), other.calculateFuelEfficiency());
    }

    // Display basic info
    public void displayInfo() {
        System.out.println("ID: " + id +
                           ", Model: " + model +
                           ", Max Speed: " + maxSpeed +
                           " km/h, Mileage: " + currentMileage + " km");
    }
}
