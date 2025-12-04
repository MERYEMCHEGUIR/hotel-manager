package com.hotel.model;

import com.hotel.utils.IdGenerator;

public class Room {

    // Types de chambres
    public enum RoomType {
        SINGLE("Simple", 300),
        DOUBLE("Double", 500),
        SUITE("Suite", 900),
        DELUXE("Deluxe", 1500);

        private final String displayName;
        private final double basePrice;

        RoomType(String displayName, double basePrice) {
            this.displayName = displayName;
            this.basePrice = basePrice;
        }

        public String getDisplayName() {
            return displayName;
        }

        public double getBasePrice() {
            return basePrice;
        }
    }

    private int roomNumber;
    private RoomType type;
    private double price;
    private int floor;
    private boolean isAvailable;

    // Constructeur
    public Room(RoomType type, int floor) {
        this.roomNumber = IdGenerator.generateRoomNumber();
        this.type = type;
        this.price = type.getBasePrice();
        this.floor = floor;
        this.isAvailable = true;
    }

    // Getters et Setters
    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Le prix doit être positif");
        }
        this.price = price;
    }

    public int getFloor() {
        return floor;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Réserver la chambre
     */
    public void book() {
        if (!isAvailable) {
            throw new IllegalStateException("La chambre est déjà réservée");
        }
        this.isAvailable = false;
    }

    /**
     * Libérer la chambre
     */
    public void free() {
        this.isAvailable = true;
    }

    /**
     * Appliquer une réduction
     */
    public void applyDiscount(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("La réduction doit être entre 0 et 100%");
        }
        this.price = type.getBasePrice() * (1 - percentage / 100);
    }

    @Override
    public String toString() {
        String status = isAvailable ? "✓ Disponible" : "✗ Occupée";
        return String.format("Chambre %d | Type: %s | Prix: %.2f DH | Étage: %d | %s",
                roomNumber, type.getDisplayName(), price, floor, status);
    }
}
