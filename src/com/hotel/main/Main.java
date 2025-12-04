package com.hotel.main;

import com.hotel.model.*;
import com.hotel.service.HotelService;
import com.hotel.utils.DateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static HotelService hotelService = new HotelService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║     SYSTÈME DE GESTION HÔTELIÈRE - VERSION PRO         ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        // Charger données de test
        System.out.print("\nCharger des données de test ? (o/n) : ");
        String choice = scanner.nextLine().trim().toLowerCase();
        if (choice.equals("o") || choice.equals("oui")) {
            hotelService.loadFakeData();
        }

        //
        // Menu principal
        boolean running = true;
        while (running) {
            try {
                displayMainMenu();
                int option = getIntInput("Votre choix : ");

                switch (option) {
                    case 1:
                        customerMenu();
                        break;
                    case 2:
                        roomMenu();
                        break;
                    case 3:
                        reservationMenu();
                        break;
                    case 4:
                        reportsMenu();
                        break;
                    case 0:
                        running = false;
                        System.out.println("\n👋 Merci d'avoir utilisé notre système !");
                        break;
                    default:
                        System.out.println("⚠ Option invalide");
                }

            } catch (Exception e) {
                System.out.println("⚠ Erreur : " + e.getMessage());
            }
        }

        scanner.close();
    }

    // ========== MENU PRINCIPAL ==========
    private static void displayMainMenu() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("                    MENU PRINCIPAL");
        System.out.println("═".repeat(60));
        System.out.println("1. 👥 Gestion des clients");
        System.out.println("2. 🏨 Gestion des chambres");
        System.out.println("3. 📅 Gestion des réservations");
        System.out.println("4. 📊 Rapports et statistiques");
        System.out.println("0. 🚪 Quitter");
        System.out.println("═".repeat(60));
    }

    // ========== MENU CLIENTS ==========
    private static void customerMenu() {
        System.out.println("\n--- GESTION DES CLIENTS ---");
        System.out.println("1. Ajouter un client");
        System.out.println("2. Rechercher un client");
        System.out.println("3. Afficher tous les clients");
        System.out.println("0. Retour");

        int choice = getIntInput("Choix : ");

        switch (choice) {
            case 1:
                addCustomer();
                break;
            case 2:
                searchCustomer();
                break;
            case 3:
                hotelService.displayAllCustomers();
                break;
            case 0:
                return;
            default:
                System.out.println("⚠ Option invalide");
        }
    }

    private static void addCustomer() {
        System.out.println("\n--- AJOUTER UN CLIENT ---");

        try {
            System.out.print("Nom complet : ");
            String name = scanner.nextLine().trim();

            System.out.print("Email : ");
            String email = scanner.nextLine().trim();

            System.out.print("Téléphone (ex: 0612345678) : ");
            String phone = scanner.nextLine().trim();

            System.out.print("Adresse : ");
            String address = scanner.nextLine().trim();

            System.out.print("CIN (ex: AB123456) : ");
            String cin = scanner.nextLine().trim();

            // Ici on assume que Customer a ce constructeur : Customer(String id?, String name, String email, String phone, String address, String nationalID)
            // Si dans votre modèle Customer prend (int id, ...) adaptez la création.
            Customer customer = new Customer(name, email, phone, address, cin);
            hotelService.addCustomer(customer);

            System.out.println("\n✓ Client créé avec l'ID : " + customer.getCustomerId());

        } catch (IllegalArgumentException e) {
            System.out.println("⚠ Erreur : " + e.getMessage());
        }
    }

    private static void searchCustomer() {
        System.out.print("\nRechercher par (1-ID / 2-CIN) : ");
        int choice = getIntInput("");

        Customer customer = null;

        if (choice == 1) {
            System.out.print("ID du client : ");
            String id = scanner.nextLine().trim();
            customer = hotelService.findCustomerById(id);
        } else if (choice == 2) {
            System.out.print("CIN : ");
            String cin = scanner.nextLine().trim();
            customer = hotelService.findCustomerByCIN(cin);
        } else {
            System.out.println("⚠ Option invalide");
            return;
        }

        if (customer != null) {
            System.out.println("\n✓ Client trouvé :");
            System.out.println(customer);

            // Afficher l'historique
            List<Reservation> history = hotelService.getCustomerReservations(customer.getCustomerId());
            System.out.println("\nNombre de réservations : " + history.size());
            history.forEach(System.out::println);
        } else {
            System.out.println("⚠ Client introuvable");
        }
    }

    // ========== MENU CHAMBRES ==========
    private static void roomMenu() {
        System.out.println("\n--- GESTION DES CHAMBRES ---");
        System.out.println("1. Ajouter une chambre");
        System.out.println("2. Afficher toutes les chambres");
        System.out.println("3. Afficher chambres disponibles");
        System.out.println("4. Rechercher par type");
        System.out.println("0. Retour");

        int choice = getIntInput("Choix : ");

        switch (choice) {
            case 1:
                addRoom();
                break;
            case 2:
                hotelService.displayAllRooms();
                break;
            case 3:
                hotelService.displayAvailableRooms();
                break;
            case 4:
                searchRoomsByType();
                break;
            case 0:
                return;
            default:
                System.out.println("⚠ Option invalide");
        }
    }

    private static void addRoom() {
        System.out.println("\n--- AJOUTER UNE CHAMBRE ---");
        System.out.println("Types disponibles :");
        System.out.println("1. SINGLE (300 DH)");
        System.out.println("2. DOUBLE (500 DH)");
        System.out.println("3. SUITE (900 DH)");
        System.out.println("4. DELUXE (1500 DH)");

        int typeChoice = getIntInput("Type : ");
        int floor = getIntInput("Étage : ");

        Room.RoomType type;
        switch (typeChoice) {
            case 1: type = Room.RoomType.SINGLE; break;
            case 2: type = Room.RoomType.DOUBLE; break;
            case 3: type = Room.RoomType.SUITE; break;
            case 4: type = Room.RoomType.DELUXE; break;
            default:
                System.out.println("⚠ Type invalide");
                return;
        }

        Room room = new Room(type, floor);
        hotelService.addRoom(room);
        System.out.println("✓ Chambre ajoutée : " + room);
    }

    private static void searchRoomsByType() {
        System.out.println("\n--- RECHERCHER PAR TYPE ---");
        System.out.println("1. SINGLE");
        System.out.println("2. DOUBLE");
        System.out.println("3. SUITE");
        System.out.println("4. DELUXE");

        int choice = getIntInput("Type : ");

        Room.RoomType type;
        switch (choice) {
            case 1: type = Room.RoomType.SINGLE; break;
            case 2: type = Room.RoomType.DOUBLE; break;
            case 3: type = Room.RoomType.SUITE; break;
            case 4: type = Room.RoomType.DELUXE; break;
            default:
                System.out.println("⚠ Type invalide");
                return;
        }

        List<Room> rooms = hotelService.searchRoomsByType(type);

        if (rooms == null || rooms.isEmpty()) {
            System.out.println("⚠ Aucune chambre disponible de ce type");
        } else {
            System.out.println("\n✓ Chambres " + type + " disponibles :");
            rooms.forEach(System.out::println);
        }
    }

    // ========== MENU RÉSERVATIONS ==========
    private static void reservationMenu() {
        System.out.println("\n--- GESTION DES RÉSERVATIONS ---");
        System.out.println("1. Créer une réservation");
        System.out.println("2. Annuler une réservation");
        System.out.println("3. Afficher toutes les réservations");
        System.out.println("4. Rechercher réservations d'un client");
        System.out.println("0. Retour");

        int choice = getIntInput("Choix : ");

        switch (choice) {
            case 1:
                createReservation();
                break;
            case 2:
                cancelReservation();
                break;
            case 3:
                hotelService.displayAllReservations();
                break;
            case 4:
                searchCustomerReservations();
                break;
            case 0:
                return;
            default:
                System.out.println("⚠ Option invalide");
        }
    }

    // ========== CREATE RESERVATION (مصحح) ==========
    private static void createReservation() {
        System.out.println("\n--- CRÉER UNE RÉSERVATION ---");

        // 1) Choisir client ou en créer un nouveau
        System.out.println("1. Utiliser un client existant");
        System.out.println("2. Créer un nouveau client");
        int clientChoice = getIntInput("Choix : ");

        Customer customer = null;
        if (clientChoice == 1) {
            System.out.print("Entrez l'ID du client : ");
            String cid = scanner.nextLine().trim();
            customer = hotelService.findCustomerById(cid);
            if (customer == null) {
                System.out.println("⚠ Client introuvable");
                return;
            }
        } else if (clientChoice == 2) {
            // Réutilise addCustomer flow (simplifié)
            System.out.print("Nom complet : ");
            String name = scanner.nextLine().trim();
            System.out.print("Email : ");
            String email = scanner.nextLine().trim();
            System.out.print("Téléphone : ");
            String phone = scanner.nextLine().trim();
            System.out.print("Adresse : ");
            String address = scanner.nextLine().trim();
            System.out.print("CIN : ");
            String cin = scanner.nextLine().trim();

            customer = new Customer(name, email, phone, address, cin);
            hotelService.addCustomer(customer);
            System.out.println("✓ Client créé : ID = " + customer.getCustomerId());
        } else {
            System.out.println("⚠ Option invalide");
            return;
        }

        // 2) Choisir type de chambre
        System.out.println("\nTypes disponibles : 1-SINGLE 2-DOUBLE 3-SUITE 4-DELUXE");
        int t = getIntInput("Type : ");
        Room.RoomType type;
        switch (t) {
            case 1: type = Room.RoomType.SINGLE; break;
            case 2: type = Room.RoomType.DOUBLE; break;
            case 3: type = Room.RoomType.SUITE; break;
            case 4: type = Room.RoomType.DELUXE; break;
            default:
                System.out.println("⚠ Type invalide");
                return;
        }

        // 3) Période réservation
        LocalDate checkIn = getDateInput("Date d'arrivée (YYYY-MM-DD) : ");
        LocalDate checkOut = getDateInput("Date de départ (YYYY-MM-DD) : ");
        if (checkOut.isBefore(checkIn) || checkOut.equals(checkIn)) {
            System.out.println("⚠ Dates invalides : la date de départ doit être après la date d'arrivée.");
            return;
        }

        // 4) Chercher chambres disponibles pour ce type / période
        List<Room> available = hotelService.searchAvailableRooms(type, checkIn, checkOut);
        if (available == null || available.isEmpty()) {
            System.out.println("⚠ Aucune chambre disponible pour ces dates et ce type.");
            return;
        }

        System.out.println("\nChambres disponibles :");
        for (int i = 0; i < available.size(); i++) {
            System.out.println((i + 1) + ". " + available.get(i));
        }

        int roomChoice = getIntInput("Sélectionnez la chambre (numéro de la liste) : ");
        if (roomChoice < 1 || roomChoice > available.size()) {
            System.out.println("⚠ Sélection invalide");
            return;
        }

        Room chosen = available.get(roomChoice - 1);

        // 5) Créer réservation via service
        Reservation reservation = hotelService.createReservation(customer, chosen, checkIn, checkOut);
        if (reservation != null) {
            System.out.println("\n✓ Réservation créée avec succès !");
            System.out.println(reservation);
        } else {
            System.out.println("⚠ Échec lors de la création de la réservation.");
        }
    }

    private static void cancelReservation() {
        System.out.print("\nEntrez l'ID de la réservation à annuler : ");
        String rid = scanner.nextLine().trim();
        boolean ok = hotelService.cancelReservation(rid);
        if (ok) {
            System.out.println("✓ Réservation annulée.");
        } else {
            System.out.println("⚠ Réservation introuvable ou impossible à annuler.");
        }
    }

    private static void searchCustomerReservations() {
        System.out.print("\nEntrez l'ID du client : ");
        String cid = scanner.nextLine().trim();
        List<Reservation> list = hotelService.getCustomerReservations(cid);
        if (list == null || list.isEmpty()) {
            System.out.println("⚠ Aucune réservation trouvée pour ce client.");
        } else {
            System.out.println("\nRéservations du client :");
            list.forEach(System.out::println);
        }
    }

    // ========== RAPPORTS ==========
    private static void reportsMenu() {
        System.out.println("\n--- RAPPORTS & STATISTIQUES ---");
        System.out.println("1. Taux d'occupation");
        System.out.println("2. Chiffre d'affaires");
        System.out.println("3. Top clients");
        System.out.println("0. Retour");

        int choice = getIntInput("Choix : ");
        switch (choice) {
            case 1:
                System.out.println(hotelService.getOccupancyReport());
                break;
            case 2:
                System.out.println(hotelService.getRevenueReport());
                break;
            case 3:
                System.out.println(hotelService.getTopClientsReport());
                break;
            case 0:
                return;
            default:
                System.out.println("⚠ Option invalide");
        }
    }

    // ========== HELPERS ==========
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                if (!prompt.isEmpty()) System.out.print(prompt);
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("⚠ Entrée invalide; veuillez entrer un nombre entier.");
            }
        }
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine().trim();
                // Utilise DateUtils si vous l'avez (sinon LocalDate.parse)
                try {
                    return DateUtils.parse(line); // si DateUtils a parse(String)
                } catch (NoSuchMethodError | Exception ignored) {
                    // fallback
                    return LocalDate.parse(line);
                }
            } catch (DateTimeParseException e) {
                System.out.println("⚠ Format de date invalide. Utilisez YYYY-MM-DD.");
            }
        }
    }
}
