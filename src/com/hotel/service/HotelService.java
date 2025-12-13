package com.hotel.service;

import com.hotel.model.*;
import com.hotel.java.Manageable;
import com.hotel.utils.DateUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
// Importez Room.RoomType si nécessaire
import com.hotel.model.Room.RoomType;


public class HotelService implements Manageable<Object> {

    // Simule les DAOs (Data Access Objects)
    private List<Customer> customers = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public HotelService() {
        // Optionnel : charger des données initiales ou de la DB
    }

    // ========== GESTION DES CLIENTS (CORRIGÉ pour compilation) ==========

    // addCustomer est appelée en interne par add(Object item)
    public void addCustomer(Customer customer) {
        if (findCustomerByCIN(customer.getNationalId()) != null) {
            throw new IllegalArgumentException("Un client avec ce CIN existe déjà.");
        }
        customers.add(customer);
        System.out.println("✓ Client ajouté avec succès : " + customer.getFullName());
    }

    public Customer findCustomerById(String customerId) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst().orElse(null);
    }

    public Customer findCustomerByCIN(String cin) {
        return customers.stream()
                .filter(c -> c.getNationalId().equalsIgnoreCase(cin))
                .findFirst().orElse(null);
    }

    public void displayAllCustomers() {
        // ... (Logique d'affichage)
        customers.forEach(System.out::println);
    }

    // ========== GESTION DES CHAMBRES (CORRIGÉ pour compilation) ==========

    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("✓ Chambre ajoutée : " + room.getRoomNumber());
    }

    public Room findRoomByNumber(int roomNumber) {
        return rooms.stream().filter(r -> r.getRoomNumber() == roomNumber).findFirst().orElse(null);
    }

    public void displayAllRooms() {
        rooms.forEach(System.out::println);
    }

    public void displayAvailableRooms() {
        getAvailableRooms().forEach(System.out::println);
    }

    public List<Room> getAvailableRooms() {
        return rooms.stream().filter(Room::isAvailable).collect(Collectors.toList());
    }

    // MÉTHODE MANQUANTE REQUISE PAR MAIN.java
    public List<Room> searchRoomsByType(RoomType type) {
        return getAvailableRooms().stream()
                .filter(r -> r.getRoomType() == type)
                .collect(Collectors.toList());
    }

    // ========== GESTION DES RÉSERVATIONS (CORRIGÉ pour compilation) ==========

    public Reservation createReservation(String customerId, int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        // ... (Logique de création de réservation)
        Customer customer = findCustomerById(customerId);
        Room room = findRoomByNumber(roomNumber);

        if (customer == null || room == null) { throw new IllegalArgumentException("Client ou Chambre introuvable."); }
        if (!room.isAvailable()) { throw new IllegalStateException("La chambre n'est pas disponible"); }

        Reservation newReservation = new Reservation(customer, room, checkIn, checkOut);

        // Simuler la réservation
        room.book(); // Assurez-vous que la méthode book() existe dans Room
        reservations.add(newReservation);

        return newReservation;
    }

    public void displayAllReservations() {
        reservations.forEach(System.out::println);
    }

    // MÉTHODE MANQUANTE REQUISE PAR MAIN.java
    public boolean cancelReservation(String id) {
        Reservation reservation = reservations.stream()
                .filter(r -> r.getReservationId().equals(id))
                .findFirst().orElse(null);

        if (reservation != null) {
            // Logique d'annulation (libérer la chambre)
            reservation.getRoom().unbook(); // Assurez-vous que unbook() existe dans Room
            reservations.remove(reservation);
            return true;
        }
        return false;
    }

    // ========== IMPLÉMENTATION MANAGEABLE (CORRIGÉ pour compilation) ==========

    @Override
    public void add(Object item) {
        if (item instanceof Customer) { addCustomer((Customer) item); }
        else if (item instanceof Room) { addRoom((Room) item); }
        else { throw new IllegalArgumentException("Type d'objet non supporté."); }
    }

    @Override
    public void update(Object item) { System.out.println("⚠ Mise à jour non implémentée."); }
    @Override
    public void delete(String id) { System.out.println("⚠ Suppression non implémentée."); }
    @Override
    public void displayAll() { displayAllCustomers(); displayAllReservations(); }

    // MÉTHODE MANQUANTE REQUISE PAR MAIN.java
    public void generateReport() {
        System.out.println("\n📊 --- RAPPORT D'ACTIVITÉ ---");
        System.out.println("- Clients : " + customers.size());
        System.out.println("- Réservations : " + reservations.size());
        System.out.println("------------------------------");
    }

    public void loadFakeData() {
        // ... (Logique pour charger des données de test)
    }
}