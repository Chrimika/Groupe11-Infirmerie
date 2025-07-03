package services;

import interfaces.PatientDao;
import models.Patient;
import services.PatientService;
import java.util.List;

public class PatientServiceImpl implements PatientService {
    private final PatientDao patientDao;

    public PatientServiceImpl(PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    @Override
    public void enregistrerPatient(Patient patient) {
        patientDao.creer(patient);
    }

    @Override
    public Patient obtenirPatientParId(int id) {
        return patientDao.trouverParId(id);
    }

    @Override
    public List<Patient> listerTousLesPatients() {
        return patientDao.listerTous();
    }

    @Override
    public List<Patient> rechercherPatientsParNom(String nom) {
        return patientDao.rechercherParNom(nom);
    }

    @Override
    public void mettreAJourPatient(Patient patient) {
        patientDao.modifier(patient);
    }

    @Override
    public void supprimerPatient(int id) {
        patientDao.supprimer(id);
    }
}