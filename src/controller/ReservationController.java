package com.hotel.controller;

import com.hotel.model.Reservation;
import java.util.ArrayList;
import java.util.List;

public class ReservationController {

    private List<Reservation> reservations;

    public ReservationController() {
        this.reservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.getRoom().book();
        System.out.println("✔ Réservation ajoutée !");
    }

    public void displayAllReservations() {
        if(reservations.isEmpty()) System.out.println("Liste des réservations vide !");
        else reservations.forEach(System.out::println);
    }

    public Reservation findById(String id) {
        return reservations.stream()
                .filter(r -> r.getReservationId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void removeReservation(Reservation reservation) {
        if(reservations.remove(reservation)) {
            reservation.getRoom().free();
            System.out.println("✔ Réservation supprimée !");
        } else
            System.out.println("⚠ Réservation non trouvée !");
    }

}

