package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/votre_base_de_donnees";
    private static final String DB_USER = "utilisateur";
    private static final String DB_PASSWORD = "motdepasse";

    public static String getRoleByEmail(String email) {
        String role = null;
        String query = "SELECT role FROM utilisateur WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                role = rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }
}