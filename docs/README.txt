Fleet Highway Simulator (Assignment 3)

1. Introduction

This project implements the Assignment 3 requirements for AP-M2025.
It extends the earlier work from Assignments 1 and 2 into a unified, GUI-based and multithreaded Fleet Highway Simulator. The system models various types of vehicles travelling on a simulated highway, each running concurrently in its own thread. The program also demonstrates a clear race condition and then resolves it through proper synchronization.

The entire system is fully controlled via a Swing-based GUI with real-time updates of vehicle states and the shared highway counter.

2. Features Implemented :

2.1 Multithreaded Simulation

Each vehicle runs inside its own dedicated thread (VehicleThread).
Every second, a vehicle:

Moves by 1 km (if it has fuel).

Consumes fuel based on its type.

Updates the shared highway distance counters.

Updates its status on the GUI.

The application supports:

Start, Pause, Resume, Stop etc

2.2 Shared Highway Counters (Race Condition)

Two counters were implemented:

Unsynchronized Counter

Updated using a plain increment (unsyncedDistance++).

Demonstrates incorrect, inconsistent totals due to race conditions.

Synchronized Counter (Correct Version)

Updated through a ReentrantLock.

Prevents race conditions and ensures an accurate total.

The GUI shows:

The unsynchronized counter for Step 1 (race condition demonstration).

The synchronized counter for Step 4 (correct behaviour).

2.3 Race Condition Demonstration and Fix

Step 1:
All vehicle threads increment the unsynchronized counter, producing incorrect totals.

Step 2:
Screenshots were captured showing:

Unsynchronized counter increasing incorrectly.

Unsynchronized behaviour after refueling (threads resume at inconsistent rates).

Step 3:
Synchronization applied using a ReentrantLock in HighwayCounter.incrementSynced().

Step 4:
Synchronized counter screenshot shows correct totals with no inconsistencies.

2.4 Graphical User Interface (Swing)

The GUI provides:

Control buttons (Start, Pause, Resume, Stop).

Refuel-All functionality.

Import CSV.

Add Vehicle dialog.

Remove Vehicle dialog.

Save Fleet button for persistence.

Two scrollable regions:

Highway panel (top)

Vehicle list panel (bottom)

Each vehicle is displayed with:

ID, Model, Mileage, Fuel Level and Status Badge (Running, Out-of-Fuel, Wind-Powered)

The GUI dynamically updates statuses and colours in real time.

2.5 Integration of Previous Assignments (Vehicle Model)

All vehicle classes from Assignments 1 and 2 are integrated:

Land, Air, and Water hierarchies.

FuelConsumable and Maintainable interfaces.

Car, Bus, Truck, Airplane, CargoShip models.

Full inheritance and polymorphism preserved.

Vehicle movement, consumption logic, and mileage tracking are extended to fit the simulator.

3. How to Compile and Run
Requirements

Java 17 or above.

Windows (for batch file execution) or manual compilation.

Method 1: Using run.bat

Simply double-click:

run.bat


This performs:

Recursive compilation of all .java files into /out

Launching of Main with the compiled classes

Method 2: Manual Commands (if needed)

$f=Get-ChildItem -Recurse -Filter *.java | % { $_.FullName }; javac -d out $f; java -cp out Main

4. GUI Layout Overview

The screen is divided into:

Top Panel

ControlPanel (buttons)

Unsynchronized counter (race condition display)

Middle Section (JSplitPane)

Scrollable HighwayPanel showing real-time vehicle positions

Scrollable horizontal vehicle list (VehiclePanel for each vehicle)

Bottom Panel

Log output showing events such as:

Added/removed vehicles

Refueling actions

Simulation state changes

5. Thread Control and Event Dispatch Thread

All GUI updates occur using:

SwingUtilities.invokeLater()


This ensures:

No UI freezes

No unsafe cross-thread operations

Proper separation between worker threads and the GUI thread

6. Race Condition Evidence (Screenshots Included)

All required screenshots are located in docs/:

unsync_race_cond.png
Incorrect counter during unsynchronized execution.

Unsync_fueled(2).png
Unsynchronized behaviour after refueling.

Synced_race_counter.png
Correct behaviour after synchronization.

Synced_fueled.png
Synchronized behaviour with refueling included.

These fully satisfy Step 2 and Step 4 requirements.

7. Persistence and CSV Integration

The simulator supports:

Loading fleet_data.csv at startup

Adding vehicles through GUI and saving immediately

Removing vehicles and writing changes back to CSV

Importing an external CSV to merge fleets

All persistence uses:
FleetManager.saveToFile(...)
and
FleetManager.loadFromFile(...)

8. Project Folder Structure (tree /F)
project-root/
│   fleet_data.csv
│   run.bat
│   sample.csv
│
├───docs
│       README.txt
│       Synced_fueled.png
│       Synced_race_counter.png
│       Unsync_fueled(2).png
│       unsync_race_cond.png
│
├───out  (compiled classes)
│
└───src  (source code)


The structure follows standard Java project layout.
All class categories are properly separated: exceptions, fleet, gui, interfaces, simulation, util, vehicles.

