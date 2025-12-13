package com.hotel.service;

import com.hotel.model.Reservation;

public class ReportGenerator {

    public static void printReservation(Reservation reservation) {
        System.out.println("=== Reservation Report ===");
        System.out.println(reservation);
    }
}
