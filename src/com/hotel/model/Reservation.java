package com.hotel.model;

import com.hotel.utils.IdGenerator;
import com.hotel.utils.DateUtils;
import java.time.LocalDate;

public class Reservation {

    private String reservationId;
    private Customer customer;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;

    // Constructeur (inchangé)
    public Reservation(Customer customer, Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationId = IdGenerator.generateReservationId();
        this.customer = customer;
        this.room = room;
        setCheckInDate(checkInDate);
        setCheckOutDate(checkOutDate);
        this.totalPrice = calculateTotalPrice();
    }

    // Getters et Setters (inchangés)
    public String getReservationId() {
        return reservationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        if (checkInDate == null) {
            throw new IllegalArgumentException("La date d'arrivée ne peut pas être nulle");
        }
        if (DateUtils.isPast(checkInDate)) {
            throw new IllegalArgumentException("La date d'arrivée ne peut pas être dans le passé");
        }
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        if (checkOutDate == null) {
            throw new IllegalArgumentException("La date de départ ne peut pas être nulle");
        }
        if (!DateUtils.isBefore(checkInDate, checkOutDate)) {
            throw new IllegalArgumentException("La date de départ doit être après la date d'arrivée");
        }
        this.checkOutDate = checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Calculer le prix total du séjour
     */
    public double calculateTotalPrice() {
        long duration = getDuration();
        return room.getPrice() * duration;
    }

    /**
     * Obtenir la durée du séjour en jours
     */
    public long getDuration() {
        return DateUtils.daysBetween(checkInDate, checkOutDate);
    }

    /**
     * Vérifier si cette réservation chevauche une autre
     */
    public boolean isOverlapping(Reservation other) {
        if (other == null || !this.room.equals(other.room)) {
            return false;
        }

        return !(this.checkOutDate.isBefore(other.checkInDate) ||
                this.checkInDate.isAfter(other.checkOutDate));
    }

    @Override
    public String toString() {
        // CORRECTION DE L'ERREUR getName()
        String customerFullName = customer.getFirstName() + " " + customer.getLastName();

        return String.format("Réservation %s | Client: %s | Chambre: %d | " +
                        "Du %s au %s | Durée: %d jours | Total: %.2f DH",
                reservationId,
                customerFullName, // Utilisation du nom complet corrigé
                room.getRoomNumber(),
                DateUtils.formatDate(checkInDate), DateUtils.formatDate(checkOutDate),
                getDuration(), totalPrice);
    }
}