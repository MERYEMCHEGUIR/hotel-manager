package com.hotel.controller;

import com.hotel.model.Customer;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    private List<Customer> customers;

    public CustomerController() {
        this.customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        System.out.println("✔ Client ajouté !");
    }

    public void displayAllCustomers() {
        if(customers.isEmpty()) System.out.println("Liste des clients vide !");
        else customers.forEach(System.out::println);
    }

    public Customer findByEmail(String email) {
        return customers.stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public void removeCustomer(Customer customer) {
        if(customers.remove(customer))
            System.out.println("✔ Client supprimé !");
        else
            System.out.println("⚠ Client non trouvé !");
    }

}

