package implementations;

import interfaces.RendezVousDao;
import models.Medecin;
import models.Patient;
import models.RendezVous;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDaoImpl implements RendezVousDao {
    private final Connection connection;

    public RendezVousDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void modifierEtat(int id, String nouvelEtat) {
        String sql = "UPDATE rendezvous SET etat = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nouvelEtat);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de l'état du rendez-vous", e);
        }
    }

    @Override
    public void annuler(int id) {
        modifierEtat(id, "annule");
    }

    @Override
    public List<RendezVous> listerParPeriode(LocalDateTime debut, LocalDateTime fin) {
        List<RendezVous> rendezVous = new ArrayList<>();
        String sql = "SELECT r.*, p.nom as patient_nom, p.prenom as patient_prenom, " +
                "m.nom as medecin_nom, m.prenom as medecin_prenom, m.specialite " +
                "FROM rendezvous r " +
                "JOIN patients p ON r.id_patient = p.id " +
                "JOIN medecins m ON r.id_medecin = m.id " +
                "WHERE r.date_heure BETWEEN ? AND ? " +
                "ORDER BY r.date_heure";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(debut));
            stmt.setTimestamp(2, Timestamp.valueOf(fin));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rendezVous.add(mapRendezVous(rs));
                }
                return rendezVous;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des rendez-vous par période", e);
        }
    }

    @Override
    public void creer(RendezVous rendezVous) {
        String sql = "INSERT INTO rendezvous(id_patient, id_medecin, date_heure, duree, motif, etat, notes) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rendezVous.getPatient().getId());
            stmt.setInt(2, rendezVous.getMedecin().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(rendezVous.getDateHeure()));
            stmt.setInt(4, rendezVous.getDuree());
            stmt.setString(5, rendezVous.getMotif());
            stmt.setString(6, rendezVous.getEtat());
            stmt.setString(7, rendezVous.getNotes());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    rendezVous.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du rendez-vous", e);
        }
    }

    @Override
    public RendezVous trouverParId(int id) {
        String sql = "SELECT r.*, p.nom as patient_nom, p.prenom as patient_prenom, " +
                "m.nom as medecin_nom, m.prenom as medecin_prenom, m.specialite " +
                "FROM rendezvous r " +
                "JOIN patients p ON r.id_patient = p.id " +
                "JOIN medecins m ON r.id_medecin = m.id " +
                "WHERE r.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRendezVous(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du rendez-vous", e);
        }
    }

    @Override
    public List<RendezVous> listerParMedecin(int idMedecin) {
        List<RendezVous> rendezVous = new ArrayList<>();
        String sql = "SELECT r.*, p.nom as patient_nom, p.prenom as patient_prenom " +
                "FROM rendezvous r " +
                "JOIN patients p ON r.id_patient = p.id " +
                "WHERE r.id_medecin = ? AND r.date_heure >= NOW() " +
                "ORDER BY r.date_heure";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedecin);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rendezVous.add(mapRendezVous(rs));
                }
                return rendezVous;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des rendez-vous", e);
        }
    }

    @Override
    public List<RendezVous> listerParPatient(int idPatient) {
        List<RendezVous> rendezVous = new ArrayList<>();
        String sql = "SELECT r.*, m.nom as medecin_nom, m.prenom as medecin_prenom, m.specialite " +
                "FROM rendezvous r " +
                "JOIN medecins m ON r.id_medecin = m.id " +
                "WHERE r.id_patient = ? " +
                "ORDER BY r.date_heure DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPatient);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rendezVous.add(mapRendezVous(rs));
                }
                return rendezVous;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des rendez-vous", e);
        }
    }

    @Override
    public boolean estDisponible(int idMedecin, LocalDateTime dateHeure, int duree) {
        String sql = "SELECT COUNT(*) FROM rendezvous " +
                "WHERE id_medecin = ? " +
                "AND date_heure < DATE_ADD(?, INTERVAL ? MINUTE) " +
                "AND DATE_ADD(date_heure, INTERVAL duree MINUTE) > ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedecin);
            stmt.setTimestamp(2, Timestamp.valueOf(dateHeure.plusMinutes(duree)));
            stmt.setInt(3, duree);
            stmt.setTimestamp(4, Timestamp.valueOf(dateHeure));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification de disponibilité", e);
        }
    }

    // Autres méthodes implémentées de manière similaire...
    private RendezVous mapRendezVous(ResultSet rs) throws SQLException {
        RendezVous rdv = new RendezVous();
        rdv.setId(rs.getInt("id"));
        rdv.setDateHeure(rs.getTimestamp("date_heure").toLocalDateTime());
        rdv.setDuree(rs.getInt("duree"));
        rdv.setMotif(rs.getString("motif"));
        rdv.setEtat(rs.getString("etat"));
        rdv.setNotes(rs.getString("notes"));

        // Mapping du patient
        Patient patient = new Patient();
        patient.setId(rs.getInt("id_patient"));
        patient.setNom(rs.getString("patient_nom"));
        patient.setPrenom(rs.getString("patient_prenom"));
        rdv.setPatient(patient);

        // Mapping du médecin
        Medecin medecin = new Medecin();
        medecin.setId(rs.getInt("id_medecin"));
        medecin.setNom(rs.getString("medecin_nom"));
        medecin.setPrenom(rs.getString("medecin_prenom"));
        medecin.setSpecialite(rs.getString("specialite"));
        rdv.setMedecin(medecin);

        return rdv;
    }
}
