README - Fleet Management System


1. Demonstration of OOP Concepts

a) Inheritance:
  The system uses a clear hierarchy. Vehicle is the abstract base class, extended by
  LandVehicle, AirVehicle, and WaterVehicle. Concrete classes like Car, Truck, Bus,
  Airplane, and CargoShip inherit common attributes and methods.

b) Polymorphism:
  FleetManager operates on the base type Vehicle. At runtime, the specific subclass
  implementation of move(), refuel(), and maintenance methods is invoked depending
  on the actual object (e.g., Car vs Airplane).

c) Abstract Classes:
  Vehicle, LandVehicle, AirVehicle, and WaterVehicle are abstract classes that define
  core attributes (ID, model, speed, mileage) and enforce implementation of move().

d) Interfaces:
  The system defines interfaces FuelConsumable, PassengerCarrier, CargoCarrier, and
  Maintainable. These are implemented by specific vehicles to model behavior like
  refueling, passenger handling, cargo handling, and maintenance scheduling.


2. Compilation and Execution Instructions

For Simplicity:
	Double click run.bat (for windows only)

Otherwise:

Compilation (from project root folder):

Open Terminal in Root Folder:
    javac -d out src/fleet/*.java src/vehicles/*.java src/interfaces/*.java src/exceptions/*.java

Run (from project root):
    java -cp out fleet.Main


3. Testing Persistence with CSV

- A sample file fleet.csv is there to check test cases.
- Use Option 8 (Load Fleet) to load vehicles from CSV into memory.
- Use Option 7 (Save Fleet) to save the current fleet state to a CSV file (one is already saved for testing purpose as fleetest.csv).
- Data includes ID, model, speed, mileage, cargo/passenger info, etc.


4. CLI Usage

After running the program, the following menu is displayed:

1. Add Vehicle                                         - Enter type, ID, model, and speed to add a new vehicle.
2. Remove Vehicle                                      - Remove vehicle by ID.
3. Start Journey                                       - Enter distance; each vehicle attempts the journey. Fuel is consumed and mileage updated.
4. Refuel All                                          - Add fuel to all fuel-based vehicles. Sail-powered ships reject refueling.
5. Perform Maintenance                                 - Run maintenance on all vehicles needing it.
6. Generate Report                                     - Print details of all vehicles, including mileage and fuel.
7. Save Fleet                                          - Save current fleet to a CSV file.
8. Load Fleet                                          - Load fleet from an existing CSV file.
9. Search by Type                                      - Search vehicles by type (e.g., Car, Bus).
10. List Vehicles Needing Maintenance                  - Shows vehicles requiring service.
11. Exit                                               - Quit program.


5. Demo Walkthrough

Example Demo:
- Start program: java -cp out fleet.Main
- Option 8: Load fleet from sample_fleet.csv. Vehicles appear in report.
- Option 3: Start journey with distance 100. Mileage and fuel update.
- Option 4: Refuel with 50 liters. Fuel levels increase accordingly.
- Option 6: Generate report to confirm updated state.
- Option 10: Run multiple journeys until maintenance thresholds exceeded. Vehicles appear in maintenance list.

Car → needsMaintenance() if mileage > 10000
Truck → mileage > 15000
Bus → mileage > 10000
Airplane → mileage > 10000
CargoShip → mileage > 20000

- Option 5: Perform maintenance to reset flags.

Expected Output:
- Vehicles print journey messages.
- Fuel warnings occur when insufficient fuel is available.
- Overload/invalid operation exceptions trigger error messages.
- Reports show vehicle count, mileage, fuel levels, and maintenance needs.
