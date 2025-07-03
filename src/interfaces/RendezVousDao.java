package interfaces;

import models.RendezVous;
import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousDao {
    void creer(RendezVous rendezVous);
    RendezVous trouverParId(int id);
    List<RendezVous> listerParMedecin(int idMedecin);
    List<RendezVous> listerParPatient(int idPatient);
    List<RendezVous> listerParPeriode(LocalDateTime debut, LocalDateTime fin);
    void modifierEtat(int id, String nouvelEtat);
    void annuler(int id);
    boolean estDisponible(int idMedecin, LocalDateTime dateHeure, int duree);
}