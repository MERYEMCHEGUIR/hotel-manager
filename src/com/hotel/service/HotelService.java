package com.hotel.service;

import com.hotel.java.Manageable;
import com.hotel.model.*;

import java.util.ArrayList;
import java.util.List;

public class HotelService implements Manageable<Object> {

    private List<Customer> customers = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    @Override
    public void add(Object item) {
        if (item instanceof Customer) customers.add((Customer) item);
        else if (item instanceof Room) rooms.add((Room) item);
        else if (item instanceof Reservation) reservations.add((Reservation) item);
    }

    @Override
    public void remove(String id) {
        customers.removeIf(c -> c.getId().equals(id));
        reservations.removeIf(r -> r.getId().equals(id));
    }

    @Override
    public Object findById(String id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Object> getAll() {
        List<Object> all = new ArrayList<>();
        all.addAll(customers);
        all.addAll(rooms);
        all.addAll(reservations);
        return all;
    }

    public void reserveRoom(Reservation reservation) {
        reservation.getRoom().setAvailable(false);
        reservations.add(reservation);
    }
}
