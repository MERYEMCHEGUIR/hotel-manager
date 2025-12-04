package com.hotel.interfaces;

public interface Manageable<T> {

    /**
     * Ajouter un élément
     */
    void add(T item);

    /**
     * Mettre à jour un élément
     */
    void update(T item);

    /**
     * Supprimer un élément
     */
    void delete(String id);

    /**
     * Afficher tous les éléments
     */
    void displayAll();
}
