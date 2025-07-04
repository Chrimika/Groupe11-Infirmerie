package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // URL de connexion mise à jour
    private static final String URL = "jdbc:mysql://localhost:3306/infirmerie_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "03-08-2004";

    // Chargement explicite du driver
    static {
        try {
            // Pour MySQL 8.0.33
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver JDBC chargé avec succès");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ ERREUR: Driver JDBC non trouvé");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("🔗 Tentative de connexion à: " + URL);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}