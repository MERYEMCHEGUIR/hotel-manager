package com.hotel.service;

import com.hotel.model.*;
import java.time.LocalDate;
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

        // Section 4: Top clients
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

        Map<Room.RoomType, Long> roomsByType = rooms.stream()
                .collect(Collectors.groupingBy(Room::getType, Collectors.counting()));

        roomsByType.forEach((type, count) -> {
            long available = rooms.stream()
                    .filter(r -> r.getType() == type && r.isAvailable())
                    .count();
            System.out.printf("• %-10s : %d chambres (%d disponibles)\n",
                    type.getDisplayName(), count, available);
        });
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

        long totalNights = reservations.stream()
                .mapToLong(Reservation::getDuration)
                .sum();

        System.out.printf("• Chiffre d'affaires total : %.2f DH\n", totalRevenue);
        System.out.printf("• Revenu moyen/réservation : %.2f DH\n", averageRevenue);
        System.out.printf("• Total nuitées vendues    : %d nuits\n", totalNights);
    }

    /**
     * Top clients fidèles
     */
    private static void generateTopCustomers(List<Customer> customers,
                                             List<Reservation> reservations) {
        System.out.println("\n🌟 TOP 3 CLIENTS FIDÈLES");
        System.out.println("─".repeat(60));

        customers.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getLoyaltyPoints(), c1.getLoyaltyPoints()))
                .limit(3)
                .forEach(customer -> {
                    long reservationCount = reservations.stream()
                            .filter(r -> r.getCustomer().equals(customer))
                            .count();

                    // CORRECTION DE L'ERREUR getName()
                    String fullName = customer.getFirstName() + " " + customer.getLastName();

                    System.out.printf("• %s - %d points (%d réservations)\n",
                            fullName, customer.getLoyaltyPoints(), reservationCount);
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
                    // CORRECTION DE L'ERREUR getName()
                    System.out.printf("• %s - Chambre %d - Client: %s %s\n",
                            r.getCheckInDate(),
                            r.getRoom().getRoomNumber(),
                            r.getCustomer().getFirstName(), // Utilisation du prénom
                            r.getCustomer().getLastName())); // Utilisation du nom de famille
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