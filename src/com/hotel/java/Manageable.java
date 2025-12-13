package com.hotel.java; // <-- LE PACKAGE DOIT ÊTRE EXACTEMENT CELUI-CI

public interface Manageable<T> {
    void add(T item);
    void update(T item);
    void delete(String id);
    void displayAll();
}