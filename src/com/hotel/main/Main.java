package com.hotel.main; // Assurez-vous que c'est bien le package de votre application

import com.hotel.model.*;
import com.hotel.service.*;
import com.hotel.database.DatabaseConfig;
import com.hotel.utils.DateUtils; // Pour la conversion de dates
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static HotelService hotelService;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   HOTEL MANAGER SYSTEM - Database    ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("\n🔌 Connexion à la base de données...");

        // 1. Test de la connexion DB (via votre DatabaseConfig)
        if (!DatabaseConfig.testConnection()) {
            System.err.println("\n❌ ERREUR : Connexion à la base de données échouée. Vérifiez vos configurations.");
            return;
        }

        // 2. Initialisation du Service
        try {
            hotelService = new HotelService();
            System.out.println("✅ Système et connexion DB prêts!\n");
        } catch (Exception e) {
            System.err.println("❌ ERREUR lors de l'initialisation du service : " + e.getMessage());
            return;
        }


        // 3. Boucle principale du menu
        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = getIntInput("Entrez votre choix: ");

            try {
                switch (choice) {
                    case 1:
                        manageCustomers();
                        break;
                    case 2:
                        manageRooms();
                        break;
                    case 3:
                        manageReservations();
                        break;
                    case 4:
                        hotelService.generateReport();
                        break;
                    case 5:
                        hotelService.loadFakeData(); // Si vous avez cette méthode pour tester
                        break;
                    case 6:
                        running = false;
                        DatabaseConfig.closeConnection();
                        System.out.println("\n✅ Déconnexion de la DB. Merci d'avoir utilisé le système.");
                        break;
                    default:
                        System.out.println("\n❌ Choix invalide! Veuillez réessayer.");
                }
            } catch (Exception e) {
                System.err.println("\n🚨 ERREUR D'EXÉCUTION : " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void showMainMenu() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│          MENU PRINCIPAL             │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│ 1. Gérer les Clients (CRUD)         │");
        System.out.println("│ 2. Gérer les Chambres               │");
        System.out.println("│ 3. Gérer les Réservations           │");
        System.out.println("│ 4. Générer Rapport (Statistiques)   │");
        System.out.println("│ 5. Charger Données de Test          │");
        System.out.println("│ 6. Quitter                          │");
        System.out.println("└─────────────────────────────────────┘");
    }

    // ===================================
    // CLIENTS
    // ===================================

    private static void manageCustomers() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│       GESTION DES CLIENTS           │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│ 1. Ajouter Nouveau Client           │");
        System.out.println("│ 2. Afficher Tous les Clients        │");
        System.out.println("│ 3. Chercher Client par ID           │");
        System.out.println("│ 4. Retour                           │");
        System.out.println("└─────────────────────────────────────┘");

        int choice = getIntInput("Entrez votre choix: ");

        switch (choice) {
            case 1:
                addNewCustomer();
                break;
            case 2:
                hotelService.displayAllCustomers();
                break;
            case 3:
                searchCustomer();
                break;
            case 4:
                return;
            default:
                System.out.println("\n❌ Choix invalide!");
        }
    }

    private static void addNewCustomer() {
        System.out.println("\n--- AJOUT NOUVEAU CLIENT ---");
        // NOTE : On utilise les méthodes addCustomer de HotelService, qui doit appeler le CustomerDAO
        String fullName = getStringInput("Nom Complet (Prénom Nom): ");
        String email = getStringInput("Email: ");
        String phone = getStringInput("Téléphone: ");
        String address = getStringInput("Adresse: ");
        String nationalId = getStringInput("CIN/Passeport (ID National): ");

        // Création de l'objet Customer (le constructeur gère la décomposition du nom)
        Customer newCustomer = new Customer(fullName, email, phone, address, nationalId);

        // Sauvegarde dans la DB via HotelService
        hotelService.addCustomer(newCustomer);
        System.out.println("Client " + newCustomer.getCustomerId() + " sauvegardé en DB.");
    }

    private static void searchCustomer() {
        String id = getStringInput("Entrez l'ID Client (ex: CUST...): ");
        Customer customer = hotelService.findCustomerById(id);

        if (customer == null) {
            System.out.println("\n❌ Client introuvable!");
        } else {
            System.out.println("\n✅ Client trouvé :");
            System.out.println(customer); // Utilise le toString() de Customer
        }
    }

    // ===================================
    // CHAMBRES
    // ===================================

    private static void manageRooms() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│       GESTION DES CHAMBRES          │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│ 1. Afficher Toutes les Chambres     │");
        System.out.println("│ 2. Afficher Chambres Disponibles    │");
        System.out.println("│ 3. Rechercher par Type              │");
        System.out.println("│ 4. Retour                           │");
        System.out.println("└─────────────────────────────────────┘");

        int choice = getIntInput("Entrez votre choix: ");

        switch (choice) {
            case 1:
                hotelService.displayAllRooms();
                break;
            case 2:
                hotelService.displayAvailableRooms();
                break;
            case 3:
                searchRoomsByType();
                break;
            case 4:
                return;
            default:
                System.out.println("\n❌ Choix invalide!");
        }
    }

    private static void searchRoomsByType() {
        System.out.println("Types disponibles: SINGLE, DOUBLE, SUITE, DELUXE");
        String typeStr = getStringInput("Entrez le type de chambre à rechercher: ").toUpperCase();

        try {
            Room.RoomType type = Room.RoomType.valueOf(typeStr);
            List<Room> availableRooms = hotelService.searchRoomsByType(type);

            if (availableRooms.isEmpty()) {
                System.out.println("\n⚠ Aucune chambre " + typeStr + " disponible.");
            } else {
                System.out.println("\n✅ Chambres " + typeStr + " disponibles :");
                availableRooms.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Type de chambre invalide!");
        }
    }


    // ===================================
    // RÉSERVATIONS
    // ===================================

    private static void manageReservations() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│     GESTION DES RÉSERVATIONS        │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│ 1. Créer Nouvelle Réservation       │");
        System.out.println("│ 2. Afficher Toutes les Réservations │");
        System.out.println("│ 3. Annuler Réservation              │");
        System.out.println("│ 4. Retour                           │");
        System.out.println("└─────────────────────────────────────┘");

        int choice = getIntInput("Entrez votre choix: ");

        switch (choice) {
            case 1:
                createReservation();
                break;
            case 2:
                hotelService.displayAllReservations();
                break;
            case 3:
                cancelReservation();
                break;
            case 4:
                return;
            default:
                System.out.println("\n❌ Choix invalide!");
        }
    }

    private static void createReservation() {
        System.out.println("\n--- CRÉATION DE RÉSERVATION ---");
        String customerId = getStringInput("ID Client (ex: CUST...): ");
        int roomNumber = getIntInput("Numéro de Chambre : ");

        System.out.println("Entrez les dates au format JJ/MM/AAAA");
        String checkInStr = getStringInput("Date d'Arrivée (Check-in): ");
        String checkOutStr = getStringInput("Date de Départ (Check-out): ");

        try {
            LocalDate checkIn = DateUtils.parseDate(checkInStr);
            LocalDate checkOut = DateUtils.parseDate(checkOutStr);

            // Appel de la logique métier pour créer la réservation et la stocker
            Reservation reservation = hotelService.createReservation(
                    customerId, roomNumber, checkIn, checkOut
            );

            System.out.println("\n✅ Réservation " + reservation.getReservationId() + " créée et stockée en DB!");
            System.out.printf("   Prix Total : %.2f DH\n", reservation.getTotalPrice());

        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC DE LA RÉSERVATION : " + e.getMessage());
        }
    }

    private static void cancelReservation() {
        String id = getStringInput("Entrez l'ID de la Réservation à annuler (ex: RES...): ");

        // Appel de la logique métier pour annuler et mettre à jour la DB
        if (hotelService.cancelReservation(id)) {
            // Le message de succès est déjà dans HotelService
        }
    }


    // ===================================
    // UTILITAIRES SCANNER
    // ===================================

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine().trim();
                // Si l'utilisateur entre une chaîne vide, cela peut casser Integer.parseInt, donc on gère cela
                if (line.isEmpty()) {
                    System.out.println("❌ Veuillez entrer une valeur.");
                    continue;
                }
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer un nombre valide!");
            }
        }
    }
}