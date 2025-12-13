package com.hotel.java;

import java.util.List;

public interface Manageable<T> {
    void add(T item);
    void remove(String id);
    T findById(String id);
    List<T> getAll();
}
