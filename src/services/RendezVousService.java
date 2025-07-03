package services;

import models.RendezVous;
import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousService {
    void enregistrerRendezVous(RendezVous rendezVous);
    RendezVous obtenirRendezVousParId(int id);
    List<RendezVous> listerRendezVousParMedecin(int idMedecin);
    List<RendezVous> listerRendezVousParPatient(int idPatient);
    List<RendezVous> listerRendezVousParPeriode(LocalDateTime debut, LocalDateTime fin);
    void modifierEtatRendezVous(int id, String nouvelEtat);
    void annulerRendezVous(int id);
    boolean estDisponible(int idMedecin, LocalDateTime dateHeure, int duree);

    List<RendezVous> listerParPeriode(LocalDateTime debut, LocalDateTime fin);

    List<RendezVous> listerParMedecin(int idMedecin);
}