package services;

import models.Utilisateur;

public interface AuthentificationService {
    Utilisateur authentifier(String username, String password);
    void deconnecter();
    Utilisateur getUtilisateurConnecte();
}