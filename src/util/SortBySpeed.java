package util;

import vehicles.Vehicle;

import java.util.Comparator;

//sort by speed(high on top) 
public class SortBySpeed implements Comparator<Vehicle> {

    @Override
    public int compare(Vehicle v1, Vehicle v2) {
        if (v1 == null && v2 == null) return 0;
        if (v1 == null) return 1;
        if (v2 == null) return -1;
        return Double.compare(v2.getMaxSpeed(), v1.getMaxSpeed()); // descending
    }
}
