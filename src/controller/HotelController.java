package com.hotel.controller;

import com.hotel.model.Room;
import com.hotel.model.Customer;
import com.hotel.model.Reservation;
import java.util.ArrayList;
import java.util.List;

public class HotelController {

    private List<Customer> customers;
    private List<Room> rooms;
    private List<Reservation> reservations;

    public HotelController() {
        this.customers = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    // --------- Customers ---------
    public void addCustomer(Customer customer) {
        customers.add(customer);
        System.out.println("✔ Client ajouté !");
    }

    public void displayCustomers() {
        if(customers.isEmpty()) System.out.println("Liste des clients vide !");
        else customers.forEach(System.out::println);
    }

    public Customer findCustomerByEmail(String email) {
        return customers.stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    // --------- Rooms ---------
    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("✔ Chambre ajoutée !");
    }

    public void displayRooms() {
        if(rooms.isEmpty()) System.out.println("Liste des chambres vide !");
        else rooms.forEach(System.out::println);
    }

    public Room findRoomByNumber(int number) {
        return rooms.stream()
                .filter(r -> r.getRoomNumber() == number)
                .findFirst()
                .orElse(null);
    }

    // --------- Reservations ---------
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.getRoom().book();
        System.out.println("✔ Réservation ajoutée !");
    }

    public void displayReservations() {
        if(reservations.isEmpty()) System.out.println("Liste des réservations vide !");
        else reservations.forEach(System.out::println);
    }

    public Reservation findReservationById(String id) {
        return reservations.stream()
                .filter(r -> r.getReservationId().equals(id))
                .findFirst()
                .orElse(null);
    }

}

