package services;

import interfaces.UtilisateurDao;
import models.Utilisateur;
import services.AuthentificationService;

public class AuthentificationServiceImpl implements AuthentificationService {
    private final UtilisateurDao utilisateurDao;
    private Utilisateur utilisateurConnecte;

    public AuthentificationServiceImpl(UtilisateurDao utilisateurDao) {
        this.utilisateurDao = utilisateurDao;
    }

    @Override
    public Utilisateur authentifier(String username, String password) {
        Utilisateur utilisateur = utilisateurDao.trouverParUsername(username);
        if (utilisateur != null && utilisateur.getPassword().equals(password)) {
            this.utilisateurConnecte = utilisateur;
            return utilisateur;
        }
        return null;
    }

    @Override
    public void deconnecter() {
        this.utilisateurConnecte = null;
    }

    @Override
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
}