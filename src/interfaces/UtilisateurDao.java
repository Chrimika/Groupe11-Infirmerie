package interfaces;

import models.Utilisateur;

public interface UtilisateurDao {
    Utilisateur trouverParUsername(String username);
    void creer(Utilisateur utilisateur);
    void modifierMotDePasse(int id, String nouveauMotDePasse);
    void changerStatut(int id, boolean estActif);
}