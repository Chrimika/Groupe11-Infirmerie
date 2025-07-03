package controllers;

import models.Utilisateur;
import services.AuthentificationService;

public class AuthentificationController {
    private AuthentificationService authService = null;

    public AuthentificationController() {
        this.authService = authService;
    }

    public Utilisateur connecter(String username, String password) {
        return authService.authentifier(username, password);
    }

    public void deconnecter() {
        authService.deconnecter();
    }

    public boolean aLeDroit(String roleRequis) {
        return authService.getUtilisateurConnecte() != null &&
                authService.getUtilisateurConnecte().getRole().equals(roleRequis);
    }
}