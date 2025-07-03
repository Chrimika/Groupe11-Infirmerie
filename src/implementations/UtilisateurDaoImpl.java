package implementations;
import interfaces.UtilisateurDao;
import models.Utilisateur;
import java.sql.*;

public class UtilisateurDaoImpl implements UtilisateurDao {
    private final Connection connection;

    public UtilisateurDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void modifierMotDePasse(int id, String nouveauMotDePasse) {
        String sql = "UPDATE utilisateurs SET password = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nouveauMotDePasse);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du mot de passe", e);
        }
    }

    @Override
    public void changerStatut(int id, boolean estActif) {
        String sql = "UPDATE utilisateurs SET est_actif = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, estActif);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du changement de statut de l'utilisateur", e);
        }
    }

    @Override
    public Utilisateur trouverParUsername(String username) {
        String sql = "SELECT * FROM utilisateurs WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("id"));
                    utilisateur.setUsername(rs.getString("username"));
                    utilisateur.setPassword(rs.getString("password"));
                    utilisateur.setRole(rs.getString("role"));
                    utilisateur.setIdPersonne(rs.getInt("id_personne"));
                    utilisateur.setEstActif(rs.getBoolean("est_actif"));
                    return utilisateur;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'utilisateur", e);
        }
    }

    @Override
    public void creer(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs(username, password, role, id_personne) " +
                "VALUES(?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getUsername());
            stmt.setString(2, utilisateur.getPassword());
            stmt.setString(3, utilisateur.getRole());
            stmt.setInt(4, utilisateur.getIdPersonne());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'utilisateur", e);
        }
    }

    // Autres méthodes implémentées de manière similaire...
}