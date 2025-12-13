package com.hotel.main;

import com.hotel.model.*;
import com.hotel.service.HotelService;
import com.hotel.utils.IdGenerator;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        HotelService service = new HotelService();

        System.out.println("=== HOTEL MANAGER SYSTEM ===");

        while (true) {
            System.out.println("\n1. Ajouter client");
            System.out.println("2. Ajouter chambre");
            System.out.println("3. Créer réservation");
            System.out.println("4. Quitter");
            System.out.print("Choix: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Nom: ");
                    String name = scanner.nextLine();

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    System.out.print("Téléphone: ");
                    String phone = scanner.nextLine();

                    Customer customer = new Customer(
                            IdGenerator.generateId(),
                            name,
                            email,
                            phone
                    );
                    service.add(customer);
                    System.out.println("✔ Client ajouté");
                }

                case 2 -> {
                    System.out.print("Numéro chambre: ");
                    String number = scanner.nextLine();

                    Room room = new Room(number, "Simple", 500);
                    service.add(room);
                    System.out.println("✔ Chambre ajoutée");
                }

                case 3 -> {
                    System.out.println("⚠ Démo simple : réservation automatique");

                    Customer c = new Customer(
                            IdGenerator.generateId(),
                            "Demo Client",
                            "demo@mail.com",
                            "0600000000"
                    );
                    Room r = new Room("102", "Double", 700);

                    service.add(c);
                    service.add(r);

                    Reservation res = new Reservation(
                            IdGenerator.generateId(),
                            c,
                            r,
                            LocalDate.now(),
                            LocalDate.now().plusDays(2)
                    );
                    service.reserveRoom(res);

                    System.out.println("✔ Réservation créée");
                }

                case 4 -> {
                    System.out.println("👋 Au revoir");
                    return;
                }

                default -> System.out.println("❌ Choix invalide");
            }
        }
    }
}
