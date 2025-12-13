package com.hotel.model;

/**
 * Représente un client de l'hôtel.
 */
public class Customer {
    private static int NEXT_ID = 1000;
    private String customerId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String nationalId; // CIN ou Passeport

    public Customer(String fullName, String email, String phone, String address, String nationalId) {
        this.customerId = "CUST" + (NEXT_ID++);
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.nationalId = nationalId;
    }

    // Getters
    public String getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getNationalId() { return nationalId; }

    // Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return String.format("| ID: %-8s | Nom: %-20s | Email: %-25s | Tél: %s |",
                customerId, fullName, email, phone);
    }
}