package com.hotel.model;

import com.hotel.utils.IdGenerator;
import com.hotel.utils.Validator;

public class Customer extends Person {

    private String customerId;
    private String nationalId; // CIN
    private int loyaltyPoints;

    // Constructeur
    public Customer(String name, String email, String phone, String address, String nationalId) {
        super(name, email, phone, address);
        this.customerId = IdGenerator.generateCustomerId();
        setNationalId(nationalId);
        this.loyaltyPoints = 0;
    }

    // Getters et Setters
    public String getCustomerId() {
        return customerId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        if (!Validator.isValidCIN(nationalId)) {
            throw new IllegalArgumentException("CIN invalide: " + nationalId);
        }
        this.nationalId = nationalId.toUpperCase().trim();
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    /**
     * Ajouter des points de fidélité
     */
    public void addPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Les points doivent être positifs");
        }
        this.loyaltyPoints += points;
    }

    /**
     * Utiliser des points de fidélité
     */
    public boolean usePoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Les points doivent être positifs");
        }
        if (this.loyaltyPoints >= points) {
            this.loyaltyPoints -= points;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | %s | CIN: %s | Points: %d",
                customerId, super.toString(), nationalId, loyaltyPoints);
    }
}