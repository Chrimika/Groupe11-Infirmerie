package implementations;

import interfaces.ConsultationDao;
import models.Consultation;
import models.Medecin;
import models.Patient;
import models.RendezVous;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDaoImpl implements ConsultationDao {
    private final Connection connection;

    public ConsultationDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void ajouterNotes(int id, String notes) {
        String sql = "UPDATE consultations SET notes = CONCAT(IFNULL(notes,''), ?) WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "\n" + notes); // Ajoute un saut de ligne avant les nouvelles notes
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout des notes à la consultation", e);
        }
    }

    @Override
    public List<Consultation> listerParMedecin(int idMedecin) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT c.*, r.date_heure, p.nom as patient_nom, p.prenom as patient_prenom " +
                "FROM consultations c " +
                "JOIN rendezvous r ON c.id_rendezvous = r.id " +
                "JOIN patients p ON r.id_patient = p.id " +
                "WHERE r.id_medecin = ? " +
                "ORDER BY c.date_consultation DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedecin);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultations.add(mapConsultation(rs));
                }
                return consultations;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des consultations par médecin", e);
        }
    }

    @Override
    public void creer(Consultation consultation) {
        String sql = "INSERT INTO consultations(id_rendezvous, diagnostic, traitement, notes, " +
                "poids, taille, tension, temperature) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, consultation.getRendezVous().getId());
            stmt.setString(2, consultation.getDiagnostic());
            stmt.setString(3, consultation.getTraitement());
            stmt.setString(4, consultation.getNotes());
            stmt.setDouble(5, consultation.getPoids());
            stmt.setDouble(6, consultation.getTaille());
            stmt.setString(7, consultation.getTension());
            stmt.setDouble(8, consultation.getTemperature());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    consultation.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la consultation", e);
        }
    }

    @Override
    public Consultation trouverParId(int id) {
        String sql = "SELECT c.*, r.date_heure, p.nom as patient_nom, p.prenom as patient_prenom, " +
                "m.nom as medecin_nom, m.prenom as medecin_prenom " +
                "FROM consultations c " +
                "JOIN rendezvous r ON c.id_rendezvous = r.id " +
                "JOIN patients p ON r.id_patient = p.id " +
                "JOIN medecins m ON r.id_medecin = m.id " +
                "WHERE c.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapConsultation(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la consultation", e);
        }
    }

    @Override
    public List<Consultation> listerParPatient(int idPatient) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT c.*, r.date_heure, m.nom as medecin_nom, m.prenom as medecin_prenom " +
                "FROM consultations c " +
                "JOIN rendezvous r ON c.id_rendezvous = r.id " +
                "JOIN medecins m ON r.id_medecin = m.id " +
                "WHERE r.id_patient = ? " +
                "ORDER BY c.date_consultation DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPatient);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultations.add(mapConsultation(rs));
                }
                return consultations;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des consultations", e);
        }
    }

    private Consultation mapConsultation(ResultSet rs) throws SQLException {
        Consultation consultation = new Consultation();
        consultation.setId(rs.getInt("id"));
        consultation.setDateConsultation(rs.getTimestamp("date_consultation").toLocalDateTime());
        consultation.setDiagnostic(rs.getString("diagnostic"));
        consultation.setTraitement(rs.getString("traitement"));
        consultation.setNotes(rs.getString("notes"));
        consultation.setPoids(rs.getDouble("poids"));
        consultation.setTaille(rs.getDouble("taille"));
        consultation.setTension(rs.getString("tension"));
        consultation.setTemperature(rs.getDouble("temperature"));

        // Création d'un rendez-vous minimal pour la relation
        RendezVous rdv = new RendezVous();
        rdv.setId(rs.getInt("id_rendezvous"));
        rdv.setDateHeure(rs.getTimestamp("date_heure").toLocalDateTime());

        // Si les informations patient/médecin sont disponibles
        if (hasColumn(rs, "patient_nom")) {
            Patient patient = new Patient();
            patient.setNom(rs.getString("patient_nom"));
            patient.setPrenom(rs.getString("patient_prenom"));
            rdv.setPatient(patient);
        }

        if (hasColumn(rs, "medecin_nom")) {
            Medecin medecin = new Medecin();
            medecin.setNom(rs.getString("medecin_nom"));
            medecin.setPrenom(rs.getString("medecin_prenom"));
            rdv.setMedecin(medecin);
        }

        consultation.setRendezVous(rdv);
        return consultation;
    }

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            if (columnName.equals(rs.getMetaData().getColumnName(i))) {
                return true;
            }
        }
        return false;
    }
}