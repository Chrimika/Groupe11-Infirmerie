package implementations;

import interfaces.PatientDao;
import models.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDaoImpl implements PatientDao {
    private final Connection connection;

    public PatientDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void creer(Patient patient) {
        String sql = "INSERT INTO patients(nom, prenom, date_naissance, sexe, numero_dossier, " +
                "adresse, telephone, email, groupe_sanguin, allergies, antecedents) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, patient.getNom());
            stmt.setString(2, patient.getPrenom());
            stmt.setDate(3, Date.valueOf(patient.getDateNaissance()));
            stmt.setString(4, patient.getSexe());
            stmt.setString(5, patient.getNumeroDossier());
            stmt.setString(6, patient.getAdresse());
            stmt.setString(7, patient.getTelephone());
            stmt.setString(8, patient.getEmail());
            stmt.setString(9, patient.getGroupeSanguin());
            stmt.setString(10, patient.getAllergies());
            stmt.setString(11, patient.getAntecedents());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    patient.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du patient", e);
        }
    }

    @Override
    public Patient trouverParId(int id) {
        String sql = "SELECT * FROM patients WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapPatient(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du patient", e);
        }
    }

    @Override
    public List<Patient> listerTous() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY nom, prenom";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(mapPatient(rs));
            }
            return patients;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des patients", e);
        }
    }

    @Override
    public List<Patient> rechercherParNom(String nom) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE nom LIKE ? ORDER BY nom, prenom";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nom + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapPatient(rs));
                }
                return patients;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des patients par nom", e);
        }
    }

    @Override
    public void modifier(Patient patient) {
        // Implémentation à ajouter
    }

    @Override
    public void supprimer(int id) {
        // Implémentation à ajouter
    }

    private Patient mapPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getInt("id"));
        patient.setNom(rs.getString("nom"));
        patient.setPrenom(rs.getString("prenom"));
        patient.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
        patient.setSexe(rs.getString("sexe"));
        patient.setNumeroDossier(rs.getString("numero_dossier"));
        patient.setAdresse(rs.getString("adresse"));
        patient.setTelephone(rs.getString("telephone"));
        patient.setEmail(rs.getString("email"));
        patient.setGroupeSanguin(rs.getString("groupe_sanguin"));
        patient.setAllergies(rs.getString("allergies"));
        patient.setAntecedents(rs.getString("antecedents"));
        return patient;
    }
}