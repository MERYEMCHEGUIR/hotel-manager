package com.hotel.model;

import com.hotel.utils.Validator;

public abstract class Person {

    protected String name;
    protected String email;
    protected String phone;
    protected String address;

    // Constructeur
    public Person(String name, String email, String phone, String address) {
        setName(name);
        setEmail(email);
        setPhone(phone);
        setAddress(address);
    }

    // Getters et Setters avec validation
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!Validator.isNotEmpty(name)) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!Validator.isValidEmail(email)) {
            throw new IllegalArgumentException("Email invalide: " + email);
        }
        this.email = email.trim().toLowerCase();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (!Validator.isValidPhone(phone)) {
            throw new IllegalArgumentException("Numéro de téléphone invalide: " + phone);
        }
        this.phone = phone.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (!Validator.isNotEmpty(address)) {
            throw new IllegalArgumentException("L'adresse ne peut pas être vide");
        }
        this.address = address.trim();
    }

    @Override
    public String toString() {
        return String.format("Nom: %s | Email: %s | Tél: %s | Adresse: %s",
                name, email, phone, address);
    }
}
