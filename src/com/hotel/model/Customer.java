package com.hotel.model;

import com.hotel.utils.IdGenerator;
import com.hotel.utils.Validator;

public class Customer extends Person {

    private String customerId;
    private String nationalId; // CIN
    private int loyaltyPoints;

    // NOUVEL ATTRIBUT : Ajout du champ 'address' car il n'est pas dans Person
    private String address;

    // Constructeur CORRIGÉ
    // On garde l'ancien design (un seul 'name'), mais on gère la décomposition pour 'Person'
    public Customer(String name, String email, String phone, String address, String nationalId) {

        // --- 1. PRE-TRAITEMENT POUR RESPECTER LA CLASSE PERSON ---

        // Décomposition simple du nom :
        // Note: C'est une simplification. Dans un projet réel, il faudrait demander firstName et lastName séparément.
        String[] nameParts = name.split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        // Génération de l'ID, requis par le constructeur de Person
        String id = IdGenerator.generateCustomerId();

        // --- 2. APPEL DU CONSTRUCTEUR PARENT ---

        // APPEL CORRIGÉ : super(id, firstName, lastName, email, phone) -> 5 arguments
        super(id, firstName, lastName, email, phone);

        // --- 3. INITIALISATION DES CHAMPS SPÉCIFIQUES ---

        this.customerId = id; // Utilise l'ID généré
        this.address = address; // Stocke l'adresse (spécifique à Customer)
        setNationalId(nationalId);
        this.loyaltyPoints = 0;
    }

    // NOUVEAUX Getters et Setters pour 'address'
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getters et Setters (inchangés sauf l'ajout de address)
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

    // Méthode pour obtenir le nom complet, pour la classe Reservation
    public String getFullName() {
        // getFirstName() et getLastName() sont hérités de Person
        return getFirstName() + " " + getLastName();
    }

    @Override
    public String toString() {
        // Correction de super.toString() pour inclure l'adresse et utiliser le nom complet
        // Note: super.toString() dans Person affiche les champs {id, firstName, lastName, email, phone}.
        // On préfère ici un format propre.
        return String.format("ID: %s | Nom: %s | Email: %s | Tél: %s | Adresse: %s | CIN: %s | Points: %d",
                customerId, getFullName(), getEmail(), getPhone(), address, nationalId, loyaltyPoints);
    }
}