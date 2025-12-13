package com.hotel.model;

public class Customer extends Person {

    public Customer(String id, String name, String email, String phone) {
        super(id, name, email, phone);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
