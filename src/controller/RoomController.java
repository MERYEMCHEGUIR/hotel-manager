package com.hotel.controller;

import com.hotel.model.Room;
import java.util.ArrayList;
import java.util.List;

public class RoomController {

    private List<Room> rooms;

    public RoomController() {
        this.rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("✔ Chambre ajoutée !");
    }

    public void displayAllRooms() {
        if(rooms.isEmpty()) System.out.println("Liste des chambres vide !");
        else rooms.forEach(System.out::println);
    }

    public Room findRoomByNumber(int number) {
        return rooms.stream()
                .filter(r -> r.getRoomNumber() == number)
                .findFirst()
                .orElse(null);
    }

    public void removeRoom(Room room) {
        if(rooms.remove(room))
            System.out.println("✔ Chambre supprimée !");
        else
            System.out.println("⚠ Chambre non trouvée !");
    }

}

