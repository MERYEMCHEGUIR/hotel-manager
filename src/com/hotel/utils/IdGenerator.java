package com.hotel.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

    private static AtomicInteger customerCounter = new AtomicInteger(1000);
    private static AtomicInteger reservationCounter = new AtomicInteger(5000);
    private static AtomicInteger roomCounter = new AtomicInteger(100);

    /**
     * Génère ID client unique (ex: CUST1001)
     */
    public static String generateCustomerId() {
        return "CUST" + customerCounter.incrementAndGet();
    }

    /**
     * Génère ID réservation unique (ex: RES5001)
     */
    public static String generateReservationId() {
        return "RES" + reservationCounter.incrementAndGet();
    }

    /**
     * Génère numéro de chambre unique (ex: 101)
     */
    public static int generateRoomNumber() {
        return roomCounter.incrementAndGet();
    }

    /**
     * Reset compteurs (pour tests)
     */
    public static void reset() {
        customerCounter.set(1000);
        reservationCounter.set(5000);
        roomCounter.set(100);
    }
}
