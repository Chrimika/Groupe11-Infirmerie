package controllers;

import models.Medecin;
import models.Patient;
import models.RendezVous;
import services.MedecinService;
import services.PatientService;
import services.RendezVousService;

import java.time.LocalDateTime;
import java.util.List;

public class RendezVousController {
    private final RendezVousService rendezVousService;
    private final PatientService patientService;
    private final MedecinService medecinService;

    public RendezVousController(RendezVousService rendezVousService,
                                PatientService patientService,
                                MedecinService medecinService) {
        this.rendezVousService = rendezVousService;
        this.patientService = patientService;
        this.medecinService = medecinService;
    }

    public boolean programmerRendezVous(int idPatient, int idMedecin,
                                        LocalDateTime dateHeure, String motif) {
        Patient patient = patientService.obtenirPatientParId(idPatient);
        Medecin medecin = medecinService.obtenirMedecinParId(idMedecin);

        if (patient == null || medecin == null) {
            return false;
        }

        RendezVous rdv = new RendezVous();
        rdv.setPatient(patient);
        rdv.setMedecin(medecin);
        rdv.setDateHeure(dateHeure);
        rdv.setMotif(motif);
        rdv.setEtat("programme");

        rendezVousService.enregistrerRendezVous(rdv);
        return true;
    }

    public List<RendezVous> listerRendezVousParPeriode(LocalDateTime debut, LocalDateTime fin) {
        return rendezVousService.listerParPeriode(debut, fin);
    }

    public List<RendezVous> listerRendezVousParMedecin(int idMedecin) {
        return rendezVousService.listerParMedecin(idMedecin);
    }

    public PatientService getPatientService() {
        return patientService;
    }

    public MedecinService getMedecinService() {
        return medecinService;
    }
}