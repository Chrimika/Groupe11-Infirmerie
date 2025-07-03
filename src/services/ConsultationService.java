package services;

import models.Consultation;
import java.util.List;

public interface ConsultationService {
    void enregistrerConsultation(Consultation consultation);
    Consultation obtenirConsultationParId(int id);
    List<Consultation> listerConsultationsParPatient(int idPatient);
    List<Consultation> listerConsultationsParMedecin(int idMedecin);
    void ajouterNotesConsultation(int id, String notes);
}
