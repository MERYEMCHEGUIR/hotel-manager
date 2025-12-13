package com.hotel.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    // =====================================
    // ATTRIBUTS DE CLASSE (VARIABLES)
    // =====================================
    private static int NEXT_ID = 5000;

    private String reservationId; // <-- Manquait dans votre extrait
    private Customer customer;   // <-- Manquait dans votre extrait
    private Room room;           // <-- Manquait dans votre extrait
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;   // <-- Manquait dans votre extrait

    // =====================================
    // CONSTRUCTEUR
    // =====================================
    public Reservation(Customer customer, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationId = "RES" + (NEXT_ID++);
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = calculateTotalPrice();
    }

    // =====================================
    // LOGIQUE INTERNE
    // =====================================
    private double calculateTotalPrice() {
        // Calcule le nombre de jours entre les deux dates
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        // Sécurité pour éviter un prix négatif si les dates sont inversées
        if (days <= 0) {
            // Considérer au moins une nuit si check-in et check-out sont le même jour
            days = 1;
        }

        return days * room.getPricePerNight();
    }

    // =====================================
    // GETTERS
    // =====================================
    public String getReservationId() { return reservationId; }
    public Customer getCustomer() { return customer; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; } // Le getter correct
    public double getTotalPrice() { return totalPrice; }

    @Override
    public String toString() {
        return String.format("| RES ID: %s | Client: %s | Chambre: %d | Arrivée: %s | Départ: %s | Prix: %.2f DH |",
                reservationId,
                customer.getFullName(),
                room.getRoomNumber(),
                checkInDate,
                checkOutDate,
                totalPrice);
    }
}