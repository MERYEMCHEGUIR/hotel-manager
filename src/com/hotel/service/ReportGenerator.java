package com.hotel.service;

import com.hotel.model.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit; // Import nécessaire pour calculer la durée
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {

    /**
     * Générer un rapport complet
     */
    public static void generateFullReport(List<Customer> customers,
                                          List<Room> rooms,
                                          List<Reservation> reservations) {

        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║          RAPPORT DE GESTION - HÔTEL                    ║");
        System.out.println("║          Date: " + LocalDate.now() + "                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        // Section 1: Vue d'ensemble
        generateOverviewSection(customers, rooms, reservations);

        // Section 2: Statistiques des chambres
        generateRoomStatistics(rooms);

        // Section 3: Statistiques financières
        generateFinancialStatistics(reservations);

        // Section 4: Top clients (Utilise le nombre de réservations car les points de fidélité sont manquants)
        generateTopCustomers(customers, reservations);

        // Section 5: Réservations à venir
        generateUpcomingReservations(reservations);

        System.out.println("\n" + "═".repeat(60));
    }

    /**
     * Section vue d'ensemble
     */
    private static void generateOverviewSection(List<Customer> customers,
                                                List<Room> rooms,
                                                List<Reservation> reservations) {
        System.out.println("\n📊 VUE D'ENSEMBLE");
        System.out.println("─".repeat(60));
        System.out.println("• Nombre de clients      : " + customers.size());
        System.out.println("• Nombre de chambres     : " + rooms.size());
        System.out.println("• Réservations actives   : " + reservations.size());

        long occupiedRooms = rooms.stream().filter(r -> !r.isAvailable()).count();
        double occupancyRate = rooms.isEmpty() ? 0 : (occupiedRooms * 100.0) / rooms.size();
        System.out.printf("• Taux d'occupation      : %.1f%%\n", occupancyRate);
    }

    /**
     * Statistiques des chambres
     */
    private static void generateRoomStatistics(List<Room> rooms) {
        System.out.println("\n🏨 STATISTIQUES DES CHAMBRES");
        System.out.println("─".repeat(60));

        // CORRECTION 1: Utilisation de getRoomType() au lieu de getType()
        Map<Room.RoomType, Long> roomsByType = rooms.stream()
                .collect(Collectors.groupingBy(Room::getRoomType, Collectors.counting()));

        roomsByType.forEach((type, count) -> {
            long available = rooms.stream()
                    .filter(r -> r.getRoomType() == type && r.isAvailable())
                    .count();

            // CORRECTION 2: Utilisation de type.name() au lieu de type.getDisplayName()
            System.out.printf("• %-10s : %d chambres (%d disponibles)\n",
                    type.name(), count, available);
        });
    }

    /**
     * Méthode d'aide pour calculer la durée d'une réservation (en jours).
     */
    private static long calculateDuration(Reservation reservation) {
        // Calcule la différence en jours entre la date d'arrivée et la date de départ
        return ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
    }

    /**
     * Statistiques financières
     */
    private static void generateFinancialStatistics(List<Reservation> reservations) {
        System.out.println("\n💰 STATISTIQUES FINANCIÈRES");
        System.out.println("─".repeat(60));

        double totalRevenue = reservations.stream()
                .mapToDouble(Reservation::getTotalPrice)
                .sum();

        double averageRevenue = reservations.isEmpty() ? 0 :
                totalRevenue / reservations.size();

        // CORRECTION 3: Utilisation de la méthode calculateDuration pour obtenir la durée
        long totalNights = reservations.stream()
                .mapToLong(ReportGenerator::calculateDuration)
                .sum();

        System.out.printf("• Chiffre d'affaires total : %.2f DH\n", totalRevenue);
        System.out.printf("• Revenu moyen/réservation : %.2f DH\n", averageRevenue);
        System.out.printf("• Total nuitées vendues    : %d nuits\n", totalNights);
    }

    /**
     * Top clients fidèles (classé par nombre de réservations, car getLoyaltyPoints est manquant)
     */
    private static void generateTopCustomers(List<Customer> customers,
                                             List<Reservation> reservations) {
        System.out.println("\n🌟 TOP 3 CLIENTS FIDÈLES");
        System.out.println("─".repeat(60));

        Map<Customer, Long> reservationCount = reservations.stream()
                .collect(Collectors.groupingBy(Reservation::getCustomer, Collectors.counting()));

        // Trie par nombre de réservations (le plus élevé d'abord)
        reservationCount.entrySet().stream()
                .sorted(Map.Entry.<Customer, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> {
                    Customer customer = entry.getKey();
                    long count = entry.getValue();

                    // CORRECTION 4: Utilisation de getFullName() au lieu des méthodes getFirstName/getLastName manquantes
                    String fullName = customer.getFullName();

                    System.out.printf("• %s (%d réservations)\n", fullName, count);
                });
    }

    /**
     * Réservations à venir
     */
    private static void generateUpcomingReservations(List<Reservation> reservations) {
        System.out.println("\n📅 PROCHAINES ARRIVÉES (7 JOURS)");
        System.out.println("─".repeat(60));

        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        List<Reservation> upcoming = reservations.stream()
                .filter(r -> !r.getCheckInDate().isBefore(today) &&
                        !r.getCheckInDate().isAfter(nextWeek))
                .sorted((r1, r2) -> r1.getCheckInDate().compareTo(r2.getCheckInDate()))
                .limit(5)
                .collect(Collectors.toList());

        if (upcoming.isEmpty()) {
            System.out.println("• Aucune arrivée prévue dans les 7 prochains jours");
        } else {
            upcoming.forEach(r ->
                    // CORRECTION 5: Utilisation de getFullName() au lieu des méthodes getFirstName/getLastName manquantes
                    System.out.printf("• %s - Chambre %d - Client: %s\n",
                            r.getCheckInDate(),
                            r.getRoom().getRoomNumber(),
                            r.getCustomer().getFullName()));
        }
    }

    /**
     * Rapport mensuel simplifié
     */
    public static void generateMonthlyReport(List<Reservation> reservations) {
        System.out.println("\n📈 RAPPORT MENSUEL");
        System.out.println("─".repeat(60));

        LocalDate now = LocalDate.now();

        List<Reservation> monthReservations = reservations.stream()
                .filter(r -> r.getCheckInDate().getMonth() == now.getMonth())
                .collect(Collectors.toList());

        double monthRevenue = monthReservations.stream()
                .mapToDouble(Reservation::getTotalPrice)
                .sum();

        System.out.printf("• Réservations du mois : %d\n", monthReservations.size());
        System.out.printf("• Revenus du mois      : %.2f DH\n", monthRevenue);
    }
}