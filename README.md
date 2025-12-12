<<<<<<< HEAD
# 🚗 Fleet Management & Highway Simulator

**Assignments 1, 2, and 3 (AP-M2025)**

> This repository contains the integrated work for **Assignment 1**,
> **Assignment 2**, and **Assignment 3**: a Fleet Management system and
> a multithreaded Highway Simulator with a Swing GUI, race-condition
> demonstration, and synchronization fix.

------------------------------------------------------------------------

## Table of Contents

-   [Overview](#overview)\
-   [Features](#features)
    -   [Multithreaded Simulation](#multithreaded-simulation)\
    -   [Shared Counters & Race Condition
        Demo](#shared-counters--race-condition-demo)\
    -   [GUI (Swing)](#gui-swing)\
    -   [Integration of Assignments 1 &
        2](#integration-of-assignments-1--2)\
-   [How to Compile & Run](#how-to-compile--run)\
-   [GUI Layout Overview](#gui-layout-overview)\
-   [Thread Safety & EDT](#thread-safety--edt)\
-   [Race Condition Evidence
    (Screenshots)](#race-condition-evidence-screenshots)\
-   [Persistence & CSV Integration](#persistence--csv-integration)\
-   [Project Structure](#project-structure)\
-   [Summary](#summary)\
-   [Possible Enhancements](#possible-enhancements)

------------------------------------------------------------------------

## Overview

This project evolves the codebase across three assignments:

-   **Assignment 1:** Vehicle classes, inheritance, interfaces.\
-   **Assignment 2:** Fleet management utilities (add/remove, save/load,
    sorting).\
-   **Assignment 3:** GUI + multithreaded highway simulator, showing a
    race condition and its fix.

The final application is a **Java Swing** GUI that runs each vehicle in
its own thread, updates shared highway counters, and visualizes both the
faulty (unsynchronized) and correct (synchronized) counters.

------------------------------------------------------------------------

## Features

### Multithreaded Simulation

-   Each vehicle runs in a dedicated `VehicleThread`.
-   Every second, a vehicle:
    -   Moves by 1 km (if it has fuel).
    -   Consumes fuel according to its type.
    -   Updates shared highway counters.
    -   Updates its GUI representation.
-   Simulation controls: **Start**, **Pause**, **Resume**, **Stop**,
    **Refuel All**.

------------------------------------------------------------------------

### Shared Counters & Race Condition Demo

Two highway counters are provided:

1.  **Unsynchronized Counter**
    -   Incremented with `unsyncedDistance++`.
    -   Demonstrates inconsistent totals due to race conditions (Step
        1).
2.  **Synchronized Counter**
    -   Incremented via a `ReentrantLock`
        (e.g. `HighwayCounter.incrementSynced()`).
    -   Prevents race conditions and shows correct totals (Step 4).

The GUI displays both counters so you can **compare** unsynced vs synced
behavior.

**Race Condition Demo Steps** - **Step 1:** All threads increment
unsynchronized counter → incorrect totals. - **Step 2:** Screenshots
captured showing incorrect increments and inconsistent behavior after
refueling. - **Step 3:** Applied `ReentrantLock` to the counter
method. - **Step 4:** Captured screenshots of correct synchronized
totals.

------------------------------------------------------------------------

### GUI (Swing)

-   ControlPanel with Start / Pause / Resume / Stop / Refuel-All.
-   Add Vehicle dialog & Remove Vehicle dialog.
-   Import CSV and Save Fleet functionality.
-   Two scrollable regions:
    -   **HighwayPanel** (visualizes vehicle positions).
    -   **Vehicle list panel** (horizontal scrollable vehicle cards).
-   Each vehicle panel shows:
    -   ID, Model, Mileage, Fuel Level, Status Badge (Running /
        Out-of-Fuel / Wind-Powered).
-   Dynamic color/status updates in real time.

------------------------------------------------------------------------

### Integration of Assignments 1 & 2

-   All vehicle hierarchies and classes from earlier assignments are
    included and integrated:
    -   **Land / Air / Water** base classes.
    -   **Car**, **Bus**, **Truck**, **Airplane**, **CargoShip**.
-   Interfaces: `FuelConsumable`, `Maintainable`, `PassengerCarrier`,
    `CargoCarrier`.
-   Assignment 2 features included:
    -   Sorting utilities: `SortByModel`, `SortBySpeed`,
        `SortByEfficiency`.
    -   `FleetManager` for CSV load/save, searching, sorting.

------------------------------------------------------------------------

## How to Compile & Run

### Requirements

-   Java 17 or above.

### Method 1 --- run.bat (recommended on Windows)

Double-click `run.bat` in the project root.\
This script: 1. Recursively compiles all `.java` files into `/out`. 2.
Runs the `Main` class from `/out`.

### Method 2 --- Manual (PowerShell)

``` powershell
$f = Get-ChildItem -Recurse -Filter *.java | % { $_.FullName }; javac -d out $f; java -cp out Main
```

------------------------------------------------------------------------

## GUI Layout Overview

-   **Top Panel**
    -   Controls (Start / Pause / Resume / Stop)
    -   Unsynchronized counter (race demo)
    -   Synchronized counter (fixed behavior)
-   **Middle Section (JSplitPane)**
    -   HighwayPanel (top): vehicle visualization
    -   Vehicle list panel (bottom): horizontally scrolling vehicle
        cards
-   **Bottom Panel**
    -   Event log (added/removed vehicles, refuels, simulation state
        changes)

All UI updates are dispatched with `SwingUtilities.invokeLater(...)`.

------------------------------------------------------------------------

## Thread Safety & EDT

-   Worker threads (vehicle threads) perform simulation logic.
-   All GUI modifications are performed on the Event Dispatch Thread
    (EDT) using `SwingUtilities.invokeLater(...)` to avoid thread-safety
    issues and UI freezes.

------------------------------------------------------------------------

## Race Condition Evidence (Screenshots)

All screenshots are in the `docs/` folder:

-   `unsync_race_cond.png` --- incorrect totals during unsynchronized
    execution.\
-   `Unsync_fueled(2).png` --- unsynchronized behavior after refueling.\
-   `Synced_race_counter.png` --- correct behavior after
    synchronization.\
-   `Synced_fueled.png` --- synchronized behavior after refueling.

These images demonstrate Steps 2 and 4 of the assignment rubric.

------------------------------------------------------------------------

## Persistence & CSV Integration

-   Fleet data is persisted in `fleet_data.csv`.
-   Features:
    -   Loading `fleet_data.csv` at startup.
    -   Adding/removing vehicles and saving changes.
    -   Importing an external CSV (merge).
-   Persistence methods:
    -   `FleetManager.saveToFile(...)`
    -   `FleetManager.loadFromFile(...)`

------------------------------------------------------------------------

## Project Structure

    project-root/
    │   fleet_data.csv
    │   sample.csv
    │   run.bat
    │
    ├───Fleet Management System (AP1)     ← Assignment 1 (Vehicle Models, Basics)
    ├───Fleet Management System (AP2)     ← Assignment 2 (Persistent Fleet Manager)
    │
    ├───docs
    │       README.txt
    │       Synced_fueled.png
    │       Synced_race_counter.png
    │       Unsync_fueled(2).png
    │       unsync_race_cond.png
    │
    ├───out         (compiled classes)
    └───src         (source code for Assignment 3)
        ├───exceptions
        ├───fleet
        ├───gui
        ├───interfaces
        ├───simulation
        ├───util
        └───vehicles

------------------------------------------------------------------------

## Summary

This repository contains a complete and integrated solution for
Assignments **1**, **2**, and **3**: - Full OOP design and polymorphism
for vehicle classes. - Fleet management (CSV persistence, sorting,
search). - Swing-based GUI with real-time updates. - Multithreaded
simulation with a demonstrable race condition and a proper
synchronization fix.

------------------------------------------------------------------------
=======
🚗 Fleet Management & Highway Simulator
Assignments 1, 2, and 3 (AP-M2025)

This repository contains the complete work for Assignment 1, Assignment 2, and Assignment 3, integrated into a unified Fleet Management and Highway Simulation System with GUI, multithreading, and race-condition demonstrations.

📌 1. Overview

This project extends the functionality of Assignments 1 → 2 → 3, evolving from:

Assignment 1: Basic vehicle classes, inheritance, interfaces

Assignment 2: Fleet operations (add, remove, save, load), sorting utilities

Assignment 3: Fully interactive GUI + multithreaded highway simulator with race conditions and synchronization

The final system is a complete Fleet Highway Simulator built using Java Swing and multithreading.
Each vehicle behaves independently in its own thread while updating shared highway counters.

🚦 2. Features Implemented
2.1 Multithreaded Highway Simulation

Each vehicle runs inside its own thread (VehicleThread) and performs actions every second:

Moves 1 km at a time

Consumes fuel based on vehicle type

Updates shared highway distance counters

Refreshes its status on the GUI

Simulation supports:

✔ Start
✔ Pause
✔ Resume
✔ Stop
✔ Refuel All

2.2 Shared Highway Counters & Race Condition Demo

Two counters are implemented:

Unsynchronized Counter

Uses simple increment (unsyncedDistance++)

Produces incorrect results due to race conditions

Used for Step 1 + screenshots

Synchronized Counter

Uses ReentrantLock

Ensures accurate counts across threads

Demonstrates correct behavior

GUI displays:

Unsynced counter (Step 1 demonstration)

Synced counter (Step 4 corrected behavior)

2.3 Race Condition Demonstration Steps
Step	Description
Step 1	All vehicle threads increment the unsynced counter → incorrect totals
Step 2	Screenshots captured for wrong values + refuel behavior
Step 3	ReentrantLock added to fix the race condition
Step 4	Synced counter screenshots showing correct totals

Screenshots included in /docs.

2.4 GUI (Swing-based)

The application includes:

Start / Pause / Resume / Stop simulation controls

Add Vehicle dialog

Remove Vehicle dialog

Refuel-All button

CSV import

Save Fleet (CSV persistence)

Real-time vehicle panels

Real-time highway view

Each vehicle card displays:

ID

Model

Mileage

Current Fuel

Status Badge

Live Color Indicators

2.5 Integration of Assignment 1 & 2 Vehicle Models

The system contains all class structures required from earlier assignments:

✔ Vehicle hierarchy

LandVehicle

AirVehicle

WaterVehicle

✔ Concrete Vehicles

Car

Bus

Truck

Airplane

CargoShip

✔ Interfaces

FuelConsumable

Maintainable

PassengerCarrier / CargoCarrier

✔ Assignment 2 features included

Sorting utilities (SortByModel, SortBySpeed, SortByEfficiency)

FleetManager (load/save CSV, sorting, searching)

All earlier work is fully integrated and extended for the simulator.

🛠 3. How to Compile & Run
Method 1: Using run.bat (recommended)

Just double-click:

run.bat


This performs:

Recursively compiles all Java files into /out

Runs the program (Main)

Method 2: Manual Terminal Compilation
$f = Get-ChildItem -Recurse -Filter *.java | % { $_.FullName }; javac -d out $f; java -cp out Main

🖼 4. GUI Layout Overview
Top Panel

Start / Pause / Resume / Stop

Unsynced counter

Synced counter

Middle (JSplitPane)

HighwayPanel (vehicle movement visualization)

Vehicle list panel (scrollable)

Bottom Panel

Simulation events log

All GUI updates use:

SwingUtilities.invokeLater(...)


to ensure thread-safe rendering.

📷 5. Race Condition Evidence (Screenshots)

Located in /docs:

File	Description
unsync_race_cond.png	Incorrect totals due to race condition
Unsync_fueled(2).png	Inconsistent updates after refueling
Synced_race_counter.png	Correct synchronized totals
Synced_fueled.png	Correct behavior after refueling

These satisfy Step 2 and Step 4.

📁 6. Persistence & CSV Integration

The simulator supports:

Loading fleet_data.csv at startup

Adding new vehicles and saving to CSV

Removing vehicles and immediate persistence

Importing an external CSV

Using:

FleetManager.saveToFile(...)
FleetManager.loadFromFile(...)

📂 7. Project Structure
project-root/
│   fleet_data.csv
│   sample.csv
│   run.bat
│
├───Fleet Management System (AP1)     ← Assignment 1
├───Fleet Management System (AP2)     ← Assignment 2
│
├───docs
│       README.txt
│       Synced_fueled.png
│       Synced_race_counter.png
│       Unsync_fueled(2).png
│       unsync_race_cond.png
│
├───out         (compiled classes)
└───src         (source code for Assignment 3)


Assignments 1 and 2 are fully included in separate folders for reference and completeness.

✅ 8. Summary

This repository delivers:

Complete implementations for Assignments 1, 2, and 3

Full OOP hierarchy, interfaces, and polymorphism

Fleet management system with CSV persistence

Full Swing GUI

Multithreaded highway simulation

Demonstrated race conditions and synchronization fixes

Real-time visualization of vehicle movement
>>>>>>> 2c9a4cb9fb7cbc9bbaa0bc6870fbe2489f9557ab
