package fleet;

import vehicles.*;
import interfaces.*;
import exceptions.*;

import java.util.*;
import java.io.*;

// FleetManager class for handling the fleet
public class FleetManager {

    private List<Vehicle> fleet = new ArrayList<>();

    public void addVehicle(Vehicle v) throws InvalidOperationException {
        for (Vehicle existing : fleet) {
            if (existing.getId().equals(v.getId())) {
                throw new InvalidOperationException("Vehicle with this ID already exists.");
            }
        }
        fleet.add(v);
    }

    public void removeVehicle(String id) throws InvalidOperationException {
        Vehicle toRemove = null;
        for (Vehicle v : fleet) {
            if (v.getId().equals(id)) {
                toRemove = v;
                break;
            }
        }
        if (toRemove == null) {
            throw new InvalidOperationException("No vehicle with ID " + id);
        }
        fleet.remove(toRemove);
    }

    public void startAllJourneys(double distance) {
        for (Vehicle v : fleet) {
            try {
                v.move(distance);
                v.displayInfo();
            } catch (Exception e) {
                System.out.println("Journey failed for " + v.getId() + ": " + e.getMessage());
            }
        }
    }

    public void maintainAll() {
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable) {
                ((Maintainable) v).performMaintenance();
            }
        }
    }

    public List<Vehicle> getVehiclesNeedingMaintenance() {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable) {
                if (((Maintainable) v).needsMaintenance()) {
                    result.add(v);
                }
            }
        }
        return result;
    }

    public List<Vehicle> searchByType(Class<?> clazz) {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : fleet) {
            if (clazz.isInstance(v)) {
                result.add(v);
            }
        }
        return result;
    }

    // create a report of the current fleet
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fleet Report:\n");
        sb.append("Total vehicles: ").append(fleet.size()).append("\n");

        if (fleet.isEmpty()) {
            sb.append("No vehicles in fleet.\n");
            return sb.toString();
        }

        double totalMileage = 0;
        boolean hasFuelBased = false;

        for (Vehicle v : fleet) {
            sb.append(" - ").append(v.getId())
              .append(" (").append(v.getModel()).append(") ")
              .append(" | Speed: ").append(v.getMaxSpeed())
              .append(" | Mileage: ").append(v.getCurrentMileage()).append("\n");

            totalMileage += v.getCurrentMileage();
            if (v instanceof FuelConsumable) {
                hasFuelBased = true;
            }
        }

        if (!hasFuelBased) {
            sb.append("No fuel-based vehicles in fleet.\n");
        }

        sb.append("Total mileage: ").append(totalMileage).append(" km\n");
        return sb.toString();
    }

    // save current fleet
    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("#Type,ID,Model,MaxSpeed,Other fields");
            for (Vehicle v : fleet) {
                if (v instanceof Car) {
                    Car c = (Car) v;
                    pw.println("Car," + c.getId() + "," + c.getModel() + "," + c.getMaxSpeed()
                               + "," + c.getNumWheels()
                               + "," + c.getFuelLevel()
                               + "," + c.getCurrentPassengers()
                               + "," + (c.needsMaintenance() ? 1 : 0));
                } else if (v instanceof Truck) {
                    Truck t = (Truck) v;
                    pw.println("Truck," + t.getId() + "," + t.getModel() + "," + t.getMaxSpeed()
                               + "," + t.getNumWheels()
                               + "," + t.getFuelLevel()
                               + "," + t.getCurrentCargo()
                               + "," + (t.needsMaintenance() ? 1 : 0));
                } else if (v instanceof Bus) {
                    Bus b = (Bus) v;
                    pw.println("Bus," + b.getId() + "," + b.getModel() + "," + b.getMaxSpeed()
                               + "," + b.getNumWheels()
                               + "," + b.getFuelLevel()
                               + "," + b.getCurrentPassengers()
                               + "," + b.getCurrentCargo()
                               + "," + (b.needsMaintenance() ? 1 : 0));
                } else if (v instanceof Airplane) {
                    Airplane a = (Airplane) v;
                    pw.println("Airplane," + a.getId() + "," + a.getModel() + "," + a.getMaxSpeed()
                               + "," + a.getMaxAltitude()
                               + "," + a.getFuelLevel()
                               + "," + a.getCurrentPassengers()
                               + "," + a.getCurrentCargo()
                               + "," + (a.needsMaintenance() ? 1 : 0));
                } else if (v instanceof CargoShip) {
                    CargoShip s = (CargoShip) v;
                    // ✅ save hasSail correctly
                    pw.println("CargoShip," + s.getId() + "," + s.getModel() + "," + s.getMaxSpeed()
                               + "," + s.isSailPowered()
                               + "," + s.getFuelLevel()
                               + "," + s.getCurrentCargo()
                               + "," + (s.needsMaintenance() ? 1 : 0));
                }
            }
            System.out.println("Fleet saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    // load some fleet in .csv
    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                String type = parts[0];
                String id = parts[1];
                String model = parts[2];
                double speed = Double.parseDouble(parts[3]);

                Vehicle v = null;
                switch (type.toLowerCase()) {
                    case "car":
                        v = new Car(id, model, speed, Integer.parseInt(parts[4]));
                        break;
                    case "truck":
                        v = new Truck(id, model, speed, Integer.parseInt(parts[4]));
                        break;
                    case "bus":
                        v = new Bus(id, model, speed, Integer.parseInt(parts[4]));
                        break;
                    case "airplane":
                        v = new Airplane(id, model, speed, Double.parseDouble(parts[4]));
                        break;
                    case "cargoship":
                        boolean hasSail = Boolean.parseBoolean(parts[4]); //loads
                        v = new CargoShip(id, model, speed, hasSail);
                        break;
                }
                if (v != null) {
                    fleet.add(v);
                }
            }
            System.out.println("Fleet loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public void refuelAll(double amount) {
        for (Vehicle v : fleet) {
            if (v instanceof FuelConsumable) {
                try {
                    ((FuelConsumable) v).refuel(amount);
                } catch (Exception e) {
                    System.out.println("Refuel failed for " + v.getId() + ": " + e.getMessage());
                }
            }
        }
    }
}
