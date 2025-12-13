package com.hotel.model;

import java.time.LocalDate;

public class Reservation {
    private String id;
    private Customer customer;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;

    public Reservation(String id, Customer customer, Room room,
                       LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", customer=" + customer.getName() +
                ", room=" + room.getRoomNumber() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
