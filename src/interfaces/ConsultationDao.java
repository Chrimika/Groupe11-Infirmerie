package interfaces;

import models.Consultation;
import java.util.List;

public interface ConsultationDao {
    void creer(Consultation consultation);
    Consultation trouverParId(int id);
    List<Consultation> listerParPatient(int idPatient);
    List<Consultation> listerParMedecin(int idMedecin);
    void ajouterNotes(int id, String notes);
}