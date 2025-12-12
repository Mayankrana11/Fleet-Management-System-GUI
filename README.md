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
