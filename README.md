#  Fleet Management & Highway Simulator  
**Assignments 1, 2, and 3 (AP-M2025)**

This repository contains the complete and integrated implementation of **Assignment 1**, **Assignment 2**, and **Assignment 3**, developed as a unified **Fleet Management and Highway Simulation System** using Java, Swing, and multithreading.

The project demonstrates **object-oriented design**, **GUI development**, **multithreaded execution**, and a **race condition with its synchronization fix**.

---

##  Table of Contents
- [Overview](#overview)
- [Features](#features)
  - [Multithreaded Simulation](#multithreaded-simulation)
  - [Shared Counters & Race Condition Demo](#shared-counters--race-condition-demo)
  - [GUI (Swing)](#gui-swing)
  - [Integration of Assignments 1 & 2](#integration-of-assignments-1--2)
- [How to Compile & Run](#how-to-compile--run)
- [GUI Layout Overview](#gui-layout-overview)
- [Thread Safety & EDT](#thread-safety--edt)
- [Race Condition Evidence](#race-condition-evidence)
- [Persistence & CSV Integration](#persistence--csv-integration)
- [Project Structure](#project-structure)
- [Summary](#summary)

---

##  Overview

This project evolves progressively across three assignments:

- **Assignment 1** – Vehicle classes, inheritance hierarchy, and interfaces  
- **Assignment 2** – Fleet management operations such as add/remove vehicles, CSV persistence, and sorting utilities  
- **Assignment 3** – Swing GUI with a multithreaded highway simulator, including race-condition demonstration and fix  

Each vehicle runs independently in its own thread while updating shared highway counters.

---

##  Features

### Multithreaded Simulation
- Each vehicle runs in a dedicated `VehicleThread`
- Every second, a vehicle:
  - Moves 1 km (if fuel is available)
  - Consumes fuel based on vehicle type
  - Updates shared highway counters
  - Updates its status on the GUI
- Simulation controls: **Start**, **Pause**, **Resume**, **Stop**, **Refuel All**

---

### Shared Counters & Race Condition Demo

Two highway distance counters are implemented:

**Unsynchronized Counter**
- Uses `unsyncedDistance++`
- Produces incorrect totals due to race conditions (Step 1)

**Synchronized Counter**
- Uses `ReentrantLock`
- Ensures accurate updates across threads (Step 4)

| Step | Description |
|-----|-------------|
| Step 1 | Unsynchronized counter increments incorrectly |
| Step 2 | Screenshots captured (wrong totals + refueling issues) |
| Step 3 | Synchronization applied using `ReentrantLock` |
| Step 4 | Correct totals shown via synchronized counter |

---

### GUI (Swing)

- Start / Pause / Resume / Stop controls  
- Add / Remove vehicle dialogs  
- Refuel-All button  
- CSV import and Save Fleet  
- Real-time vehicle visualization  

Each vehicle panel displays ID, model, mileage, fuel level, and status badge.

---

### Integration of Assignments 1 & 2

- Vehicle hierarchies: **Land**, **Air**, **Water**
- Vehicles: Car, Bus, Truck, Airplane, CargoShip
- Interfaces: `FuelConsumable`, `Maintainable`, `PassengerCarrier`, `CargoCarrier`
- Sorting utilities and `FleetManager` from Assignment 2 fully integrated

---

##  How to Compile & Run

### Requirements
- Java 17 or higher

### Method 1 — `run.bat` (Windows)
Double-click `run.bat`.

### Method 2 — Manual (PowerShell)

```powershell
$f = Get-ChildItem -Recurse -Filter *.java | % { $_.FullName }
javac -d out $f
java -cp out Main
```

---

## 🖼 GUI Layout Overview

- **Top Panel**: Controls + counters  
- **Middle (JSplitPane)**: Highway view + vehicle list  
- **Bottom Panel**: Event log  

All UI updates use `SwingUtilities.invokeLater(...)`.

---

##  Thread Safety & EDT

Simulation logic runs on worker threads.  
All GUI updates are dispatched on the Event Dispatch Thread (EDT).

---

##  Race Condition Evidence

Screenshots in `docs/`:

- `unsync_race_cond.png`
- `Unsync_fueled(2).png`
- `Synced_race_counter.png`
- `Synced_fueled.png`

---

##  Persistence & CSV Integration

- Fleet data stored in `fleet_data.csv`
- Supports load, save, and CSV import
- Implemented using `FleetManager.loadFromFile()` and `saveToFile()`

---

##  Project Structure

```
project-root/
│   fleet_data.csv
│   sample.csv
│   run.bat
│
├── Fleet Management System (AP1)
├── Fleet Management System (AP2)
│
├── docs
├── out
└── src
```

---

##  Summary

A complete academic project demonstrating **OOP**, **GUI design**, **multithreading**, and **synchronization**, covering all requirements of Assignments 1, 2, and 3.
