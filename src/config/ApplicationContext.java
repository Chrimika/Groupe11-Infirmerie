package config;

import implementations.PatientDaoImpl;
import services.PatientServiceImpl;
import services.PatientService;
import java.sql.Connection;
import java.sql.SQLException;

public class ApplicationContext {
    private static Connection connection;
    private static PatientDaoImpl patientDao;
    private static PatientServiceImpl patientService;
    // Déclarer les autres DAO et services

    static {
        try {
            connection = DatabaseConfig.getConnection();

            // Initialisation des DAO
            patientDao = new PatientDaoImpl(connection);
            // Initialiser les autres DAO

            // Initialisation des services
            patientService = new PatientServiceImpl(patientDao);
            // Initialiser les autres services

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'initialisation de l'application", e);
        }
    }

    public static PatientService getPatientService() {
        return patientService;
    }

    // Méthodes pour obtenir les autres services

    public static void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        }
    }
}