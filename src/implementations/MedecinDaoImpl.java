package implementations;

import interfaces.MedecinDao;
import models.Medecin;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MedecinDaoImpl implements MedecinDao {
    private final Connection connection;

    public MedecinDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void modifier(Medecin medecin) {
        String sql = "UPDATE medecins SET nom = ?, prenom = ?, specialite = ?, " +
                "telephone = ?, email = ?, est_actif = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, medecin.getNom());
            stmt.setString(2, medecin.getPrenom());
            stmt.setString(3, medecin.getSpecialite());
            stmt.setString(4, medecin.getTelephone());
            stmt.setString(5, medecin.getEmail());
            stmt.setBoolean(6, medecin.isEstActif());
            stmt.setInt(7, medecin.getId());

            stmt.executeUpdate();

            // Mise à jour des jours de travail
            supprimerJoursTravail(medecin.getId());
            if (medecin.getJoursTravail() != null && !medecin.getJoursTravail().isEmpty()) {
                ajouterJoursTravail(medecin.getId(), medecin.getJoursTravail());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du médecin", e);
        }
    }

    private void supprimerJoursTravail(int idMedecin) throws SQLException {
        String sql = "DELETE FROM disponibilites WHERE id_medecin = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedecin);
            stmt.executeUpdate();
        }
    }

    @Override
    public void creer(Medecin medecin) {
        String sql = "INSERT INTO medecins(nom, prenom, specialite, telephone, email, est_actif) " +
                "VALUES(?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, medecin.getNom());
            stmt.setString(2, medecin.getPrenom());
            stmt.setString(3, medecin.getSpecialite());
            stmt.setString(4, medecin.getTelephone());
            stmt.setString(5, medecin.getEmail());
            stmt.setBoolean(6, medecin.isEstActif());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    medecin.setId(rs.getInt(1));
                }
            }

            // Enregistrer les jours de travail
            if (medecin.getJoursTravail() != null && !medecin.getJoursTravail().isEmpty()) {
                ajouterJoursTravail(medecin.getId(), medecin.getJoursTravail());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du médecin", e);
        }
    }

    private void ajouterJoursTravail(int idMedecin, Set<String> joursTravail) throws SQLException {
        String sql = "INSERT INTO disponibilites(id_medecin, jour_semaine) VALUES(?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (String jour : joursTravail) {
                stmt.setInt(1, idMedecin);
                stmt.setString(2, jour);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public Medecin trouverParId(int id) {
        String sql = "SELECT * FROM medecins WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Medecin medecin = mapMedecin(rs);
                    medecin.setJoursTravail(getJoursTravail(id));
                    return medecin;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du médecin", e);
        }
    }

    private Set<String> getJoursTravail(int idMedecin) throws SQLException {
        Set<String> jours = new HashSet<>();
        String sql = "SELECT jour_semaine FROM disponibilites WHERE id_medecin = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedecin);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    jours.add(rs.getString("jour_semaine"));
                }
            }
        }
        return jours;
    }

    @Override
    public List<Medecin> listerTous() {
        List<Medecin> medecins = new ArrayList<>();
        String sql = "SELECT * FROM medecins WHERE est_actif = TRUE ORDER BY nom, prenom";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Medecin medecin = mapMedecin(rs);
                medecin.setJoursTravail(getJoursTravail(medecin.getId()));
                medecins.add(medecin);
            }
            return medecins;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du listage des médecins", e);
        }
    }

    @Override
    public List<Medecin> trouverParSpecialite(String specialite) {
        List<Medecin> medecins = new ArrayList<>();
        String sql = "SELECT * FROM medecins WHERE specialite LIKE ? AND est_actif = TRUE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + specialite + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Medecin medecin = mapMedecin(rs);
                    medecin.setJoursTravail(getJoursTravail(medecin.getId()));
                    medecins.add(medecin);
                }
                return medecins;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par spécialité", e);
        }
    }

    @Override
    public void desactiver(int id) {
        String sql = "UPDATE medecins SET est_actif = FALSE WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la désactivation du médecin", e);
        }
    }

    private Medecin mapMedecin(ResultSet rs) throws SQLException {
        Medecin medecin = new Medecin();
        medecin.setId(rs.getInt("id"));
        medecin.setNom(rs.getString("nom"));
        medecin.setPrenom(rs.getString("prenom"));
        medecin.setSpecialite(rs.getString("specialite"));
        medecin.setTelephone(rs.getString("telephone"));
        medecin.setEmail(rs.getString("email"));
        medecin.setEstActif(rs.getBoolean("est_actif"));
        return medecin;
    }
}