package services;

import models.Patient;
import java.util.List;

public interface PatientService {
    void enregistrerPatient(Patient patient);
    Patient obtenirPatientParId(int id);
    List<Patient> listerTousLesPatients();
    List<Patient> rechercherPatientsParNom(String nom);
    void mettreAJourPatient(Patient patient);
    void supprimerPatient(int id);
}