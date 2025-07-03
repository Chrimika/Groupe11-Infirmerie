package controllers;

import models.Patient;
import services.PatientService;
import java.util.List;

public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    public void creerPatient(String nom, String prenom, String dateNaissance,
                             String sexe, String numeroDossier) {
        Patient patient = new Patient();
        patient.setNom(nom);
        patient.setPrenom(prenom);
        patient.setDateNaissance(java.time.LocalDate.parse(dateNaissance));
        patient.setSexe(sexe);
        patient.setNumeroDossier(numeroDossier);

        patientService.enregistrerPatient(patient);
    }

    public Patient obtenirPatient(int id) {
        return patientService.obtenirPatientParId(id);
    }

    public List<Patient> listerPatients() {
        return patientService.listerTousLesPatients();
    }

    public List<Patient> rechercherPatients(String nom) {
        return patientService.rechercherPatientsParNom(nom);
    }

    // Autres méthodes pour mettre à jour et supprimer
}