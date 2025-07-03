package models;

import java.time.LocalDateTime;

public class RendezVous {
    private int id;
    private Patient patient;
    private Medecin medecin;
    private LocalDateTime dateHeure;
    private int duree;
    private String motif;
    private String etat;
    private String notes;

    // Constructeurs
    public RendezVous() {}

    public RendezVous(Patient patient, Medecin medecin, LocalDateTime dateHeure, String motif) {
        this.patient = patient;
        this.medecin = medecin;
        this.dateHeure = dateHeure;
        this.motif = motif;
        this.duree = 30; // Durée par défaut
        this.etat = "programme";
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}