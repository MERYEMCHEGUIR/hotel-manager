package com.hotel.model;

/**
 * Représente une chambre de l'hôtel.
 */
public class Room {
    public enum RoomType {
        SINGLE, DOUBLE, SUITE, DELUXE
    }

    private int roomNumber;
    private RoomType roomType;
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int roomNumber, RoomType roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    // Getters
    public int getRoomNumber() { return roomNumber; }
    public RoomType getRoomType() { return roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return isAvailable; }

    // Méthodes d'état (CRUCIALES pour HotelService)
    public void book() { this.isAvailable = false; }
    public void unbook() { this.isAvailable = true; }

    @Override
    public String toString() {
        String status = isAvailable ? "DISPONIBLE" : "OCCUPÉE";
        return String.format("| Num: %-4d | Type: %-8s | Prix: %-6.2f DH | Statut: %s |",
                roomNumber, roomType, pricePerNight, status);
    }
}