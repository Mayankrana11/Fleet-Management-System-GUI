package simulation;

import java.util.concurrent.locks.ReentrantLock;

//shared highway counter
public class HighwayCounter {
    // intentionally unsynchronized counter (to show race condition)
    public static int unsyncedDistance = 0;

    // synchronized counter + lock (correct)
    private static int syncedDistance = 0;
    private static final ReentrantLock lock = new ReentrantLock();

    // Unsynchronized increment (race condition)
    public static void incrementUnsynced() {
        unsyncedDistance++;
    }

    // Synchronized increment using ReentrantLock
    public static void incrementSynced() {
        lock.lock();
        try {
            syncedDistance++;
        } finally {
            lock.unlock();
        }
    }

    public static int getUnsyncedDistance() {
        return unsyncedDistance;
    }

    public static int getSyncedDistance() {
        lock.lock();
        try {
            return syncedDistance;
        } finally {
            lock.unlock();
        }
    }

    // Use this to reset counters between runs
    public static void reset() {
        unsyncedDistance = 0;
        lock.lock();
        try {
            syncedDistance = 0;
        } finally {
            lock.unlock();
        }
    }
}
