package services;

import models.Medecin;
import java.util.List;

public interface MedecinService {
    void enregistrerMedecin(Medecin medecin);
    Medecin obtenirMedecinParId(int id);
    List<Medecin> listerTousLesMedecins();
    List<Medecin> trouverParSpecialite(String specialite);
    void desactiverMedecin(int id);
}