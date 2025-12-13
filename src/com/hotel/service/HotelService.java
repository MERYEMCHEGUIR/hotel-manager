package com.hotel.service;

import com.hotel.model.*;
import com.hotel.interfaces.Manageable;
import com.hotel.utils.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.hotel.database.*;

public class HotelService implements Manageable<Object> {

    private List<Customer> customers;
    private List<Room> rooms;
    private List<Reservation> reservations;

    // Constructeur
    public HotelService() {
        this.customers = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    // ========== GESTION DES CLIENTS ==========

    /**
     * Ajouter un client
     */
    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null");
        }

        // Vérifier si CIN existe déjà
        if (findCustomerByCIN(customer.getNationalId()) != null) {
            throw new IllegalArgumentException("Un client avec ce CIN existe déjà");
        }

        customers.add(customer);
        // CORRECTION DE L'ERREUR getName()
        String fullName = customer.getFirstName() + " " + customer.getLastName();
        System.out.println("✓ Client ajouté avec succès : " + fullName);
    }

    /**
     * Trouver un client par ID
     */
    public Customer findCustomerById(String customerId) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Trouver un client par CIN
     */
    public Customer findCustomerByCIN(String cin) {
        return customers.stream()
                .filter(c -> c.getNationalId().equalsIgnoreCase(cin))
                .findFirst()
                .orElse(null);
    }

    /**
     * Afficher tous les clients
     */
    public void displayAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("⚠ Aucun client enregistré");
            return;
        }

        System.out.println("\n========== LISTE DES CLIENTS ==========");
        customers.forEach(System.out::println);
        System.out.println("Total : " + customers.size() + " clients");
    }

    // ========== GESTION DES CHAMBRES ==========

    /**
     * Ajouter une chambre
     */
    public void addRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("La chambre ne peut pas être null");
        }
        rooms.add(room);
        System.out.println("✓ Chambre ajoutée : " + room.getRoomNumber());
    }

    /**
     * Trouver une chambre par numéro
     */
    public Room findRoomByNumber(int roomNumber) {
        return rooms.stream()
                .filter(r -> r.getRoomNumber() == roomNumber)
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtenir toutes les chambres disponibles
     */
    public List<Room> getAvailableRooms() {
        return rooms.stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * Rechercher chambres par type
     */
    public List<Room> searchRoomsByType(Room.RoomType type) {
        return rooms.stream()
                .filter(r -> r.getType() == type && r.isAvailable())
                .collect(Collectors.toList());
    }

    /**
     * Afficher toutes les chambres
     */
    public void displayAllRooms() {
        if (rooms.isEmpty()) {
            System.out.println("⚠ Aucune chambre enregistrée");
            return;
        }

        System.out.println("\n========== LISTE DES CHAMBRES ==========");
        rooms.forEach(System.out::println);
        System.out.println("Total : " + rooms.size() + " chambres");
    }

    /**
     * Afficher les chambres disponibles
     */
    public void displayAvailableRooms() {
        List<Room> available = getAvailableRooms();

        if (available.isEmpty()) {
            System.out.println("⚠ Aucune chambre disponible");
            return;
        }

        System.out.println("\n========== CHAMBRES DISPONIBLES ==========");
        available.forEach(System.out::println);
        System.out.println("Total : " + available.size() + " chambres disponibles");
    }

    // ========== GESTION DES RÉSERVATIONS ==========

    /**
     * Créer une réservation
     */
    public Reservation createReservation(String customerId, int roomNumber,
                                         LocalDate checkIn, LocalDate checkOut) {

        // Vérifier client
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Client introuvable : " + customerId);
        }

        // Vérifier chambre
        Room room = findRoomByNumber(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Chambre introuvable : " + roomNumber);
        }

        if (!room.isAvailable()) {
            throw new IllegalStateException("La chambre " + roomNumber + " n'est pas disponible");
        }

        // Créer réservation temporaire pour vérifier les conflits
        Reservation newReservation = new Reservation(customer, room, checkIn, checkOut);

        // Vérifier conflits de dates
        boolean hasConflict = reservations.stream()
                .anyMatch(r -> r.isOverlapping(newReservation));

        if (hasConflict) {
            throw new IllegalStateException("Conflit de dates pour la chambre " + roomNumber);
        }

        // Réserver la chambre
        room.book();

        // Ajouter points de fidélité (1 point par 100 DH)
        int points = (int) (newReservation.getTotalPrice() / 100);
        customer.addPoints(points);

        // Enregistrer la réservation
        reservations.add(newReservation);

        System.out.println("✓ Réservation créée avec succès !");
        System.out.println("✓ " + points + " points de fidélité ajoutés");

        return newReservation;
    }

    /**
     * Annuler une réservation
     */
    public boolean cancelReservation(String reservationId) {
        Reservation reservation = findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("⚠ Réservation introuvable : " + reservationId);
            return false;
        }

        // Libérer la chambre
        reservation.getRoom().free();

        // Retirer points de fidélité
        int points = (int) (reservation.getTotalPrice() / 100);
        reservation.getCustomer().usePoints(points);

        // Supprimer la réservation
        reservations.remove(reservation);

        System.out.println("✓ Réservation annulée avec succès");
        return true;
    }

    /**
     * Trouver réservation par ID
     */
    public Reservation findReservationById(String reservationId) {
        return reservations.stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Obtenir réservations d'un client
     */
    public List<Reservation> getCustomerReservations(String customerId) {
        return reservations.stream()
                .filter(r -> r.getCustomer().getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    /**
     * Afficher toutes les réservations
     */
    public void displayAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("⚠ Aucune réservation enregistrée");
            return;
        }

        System.out.println("\n========== LISTE DES RÉSERVATIONS ==========");
        reservations.forEach(System.out::println);
        System.out.println("Total : " + reservations.size() + " réservations");
    }

    // ========== STATISTIQUES ==========

    /**
     * Générer un rapport
     */
    public void generateReport() {
        ReportGenerator.generateFullReport(customers, rooms, reservations);
    }

    /**
     * Calculer le taux d'occupation
     */
    public double getOccupancyRate() {
        if (rooms.isEmpty()) return 0;

        long occupiedRooms = rooms.stream()
                .filter(r -> !r.isAvailable())
                .count();

        return (occupiedRooms * 100.0) / rooms.size();
    }

    /**
     * Calculer le chiffre d'affaires total
     */
    public double getTotalRevenue() {
        return reservations.stream()
                .mapToDouble(Reservation::getTotalPrice)
                .sum();
    }

    // ========== DONNÉES DE TEST ==========

    /**
     * Charger des données de test
     */
    public void loadFakeData() {
        System.out.println("\n🔄 Chargement des données de test...");

        try {
            // Ajouter des clients
            addCustomer(new Customer("Ahmed Benali", "ahmed@email.com",
                    "0612345678", "Casablanca", "AB123456"));
            addCustomer(new Customer("Fatima Zahra", "fatima@email.com",
                    "0623456789", "Rabat", "FZ234567"));
            addCustomer(new Customer("Youssef Alami", "youssef@email.com",
                    "0634567890", "Marrakech", "YA345678"));

            // Ajouter des chambres
            addRoom(new Room(Room.RoomType.SINGLE, 1));
            addRoom(new Room(Room.RoomType.SINGLE, 1));
            addRoom(new Room(Room.RoomType.DOUBLE, 2));
            addRoom(new Room(Room.RoomType.DOUBLE, 2));
            addRoom(new Room(Room.RoomType.SUITE, 3));
            addRoom(new Room(Room.RoomType.DELUXE, 4));

            // Créer des réservations
            createReservation(customers.get(0).getCustomerId(),
                    rooms.get(0).getRoomNumber(),
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusDays(4));

            createReservation(customers.get(1).getCustomerId(),
                    rooms.get(2).getRoomNumber(),
                    LocalDate.now().plusDays(2),
                    LocalDate.now().plusDays(7));

            System.out.println("✓ Données de test chargées avec succès !");

        } catch (Exception e) {
            System.out.println("⚠ Erreur lors du chargement : " + e.getMessage());
        }
    }

    // ========== IMPLÉMENTATION DE L'INTERFACE MANAGEABLE ==========

    @Override
    public void add(Object item) {
        if (item instanceof Customer) {
            addCustomer((Customer) item);
        } else if (item instanceof Room) {
            addRoom((Room) item);
        } else {
            throw new IllegalArgumentException("Type d'objet non supporté");
        }
    }

    @Override
    public void update(Object item) {
        System.out.println("⚠ Fonction de mise à jour non implémentée");
    }

    @Override
    public void delete(String id) {
        if (id.startsWith("CUST")) {
            Customer customer = findCustomerById(id);
            if (customer != null) {
                customers.remove(customer);
                System.out.println("✓ Client supprimé");
            }
        } else if (id.startsWith("RES")) {
            cancelReservation(id);
        }
    }

    @Override
    public void displayAll() {
        displayAllCustomers();
        displayAllRooms();
        displayAllReservations();
    }

    // Getters
    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }
}