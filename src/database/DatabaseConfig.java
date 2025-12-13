package com.hotel.database;

public class DatabaseConfig {

    private static boolean isConnected = false;

    // Nécessaire pour Main.java: if (!DatabaseConfig.testConnection())
    public static boolean testConnection() {
        System.out.println("... Tentative de connexion à la base de données (Simulée)");
        isConnected = true;
        return true;
    }

    // Nécessaire pour Main.java: DatabaseConfig.closeConnection();
    public static void closeConnection() {
        if (isConnected) {
            isConnected = false;
        }
    }
}