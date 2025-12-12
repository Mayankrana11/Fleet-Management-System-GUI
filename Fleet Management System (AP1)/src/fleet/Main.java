package fleet;

import vehicles.*;
import exceptions.*;
import interfaces.*;

import java.util.Scanner;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FleetManager manager = new FleetManager();

        while (true) { // infinite loop until exit option chosen
            System.out.println("\nFleet Management Menu");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Start Journey");
            System.out.println("4. Refuel All");
            System.out.println("5. Perform Maintenance");
            System.out.println("6. Generate Report");
            System.out.println("7. Save Fleet");
            System.out.println("8. Load Fleet");
            System.out.println("9. Search by Type");
            System.out.println("10. List Vehicles Needing Maintenance");
            System.out.println("11. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter vehicle type (Car/Truck/Bus/Airplane/CargoShip): ");
                        String type = sc.nextLine();
                        System.out.print("Enter ID: ");
                        String id = sc.nextLine();
                        System.out.print("Enter model: ");
                        String model = sc.nextLine();
                        System.out.print("Enter max speed: ");
                        double speed = sc.nextDouble();

                        Vehicle v = null;
                        if (type.equalsIgnoreCase("Car")) {
                            v = new Car(id, model, speed, 4);
                        } else if (type.equalsIgnoreCase("Truck")) {
                            v = new Truck(id, model, speed, 6);
                        } else if (type.equalsIgnoreCase("Bus")) {
                            v = new Bus(id, model, speed, 6);
                        } else if (type.equalsIgnoreCase("Airplane")) {
                            v = new Airplane(id, model, speed, 10000);
                        } else if (type.equalsIgnoreCase("CargoShip")) {
                            System.out.print("Is this ship sail-powered? (yes/no): ");//cli implementation for cargo ship HasSail = true/false
                            String sailInput = sc.next();
                            boolean hasSail = sailInput.equalsIgnoreCase("yes");
                            v = new CargoShip(id, model, speed, hasSail);
                        } else {
                            System.out.println("Invalid vehicle type! Please enter Car, Truck, Bus, Airplane, or CargoShip.");
                             break;
                        }

                        if (v != null) {
                            manager.addVehicle(v);
                            System.out.println("Vehicle added successfully.");
                        }
                        break;

                    case 2:
                        System.out.print("Enter ID to remove: ");
                        String removeId = sc.nextLine();
                        manager.removeVehicle(removeId);
                        System.out.println("Vehicle removed.");
                        break;

                    case 3:
                        System.out.print("Enter journey distance (km): ");
                        double dist = sc.nextDouble();
                        manager.startAllJourneys(dist);
                        break;

                    case 4:
                        System.out.print("Enter fuel amount to add to all (liters): ");
                        double amount = sc.nextDouble();
                        for (Vehicle vf : manager.searchByType(Vehicle.class)) {
                            if (vf instanceof FuelConsumable) {
                                try {
                                    ((FuelConsumable) vf).refuel(amount);
                                } catch (InvalidOperationException e) {
                                    System.out.println("Refuel failed for " + vf.getId());
                                }
                            }
                        }
                        break;

                    case 5:
                        manager.maintainAll();
                        System.out.println("Maintenance done where needed.");
                        break;

                    case 6:
                        System.out.println(manager.generateReport());
                        break;

                    case 7:
                        System.out.print("Enter filename to save: ");
                        String saveFile = sc.nextLine();
                        manager.saveToFile(saveFile);
                        System.out.println("Fleet saved.");
                        break;

                    case 8:
                        System.out.print("Enter filename to load: ");
                        String loadFile = sc.nextLine();
                        manager.loadFromFile(loadFile);
                        System.out.println("Fleet loaded.");
                        break;

                    case 9:
                        System.out.print("Enter type to search (Car/Truck/Bus/Airplane/CargoShip): ");
                        String searchType = sc.nextLine();
                        Class<?> clazz = Vehicle.class;
                        if (searchType.equalsIgnoreCase("Car")) clazz = Car.class;
                        else if (searchType.equalsIgnoreCase("Truck")) clazz = Truck.class;
                        else if (searchType.equalsIgnoreCase("Bus")) clazz = Bus.class;
                        else if (searchType.equalsIgnoreCase("Airplane")) clazz = Airplane.class;
                        else if (searchType.equalsIgnoreCase("CargoShip")) clazz = CargoShip.class;

                        List<Vehicle> found = manager.searchByType(clazz);
                        System.out.println("Found " + found.size() + " vehicle(s):");
                        for (Vehicle res : found) {
                            res.displayInfo();
                        }
                        break;

                    case 10:
                        List<Vehicle> needing = manager.getVehiclesNeedingMaintenance();
                        if (needing.isEmpty()) {
                            System.out.println("No vehicles require maintenance at this time.");
                        } else {
                            System.out.println("Vehicles needing maintenance:");
                            for (Vehicle vehc : needing) {
                                vehc.displayInfo();
                            }
                        }
                    break;


                    case 11:
                        System.out.println("Exiting program.");
                        sc.close();
                        return; // break out of while loop

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
