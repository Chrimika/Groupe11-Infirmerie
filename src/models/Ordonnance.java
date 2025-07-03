package models;

import java.time.LocalDate;
import java.util.List;

public class Ordonnance {
    private int id;
    private Consultation consultation;
    private LocalDate datePrescription;
    private String instructions;
    private String dureeTraitement;
    private List<Medicament> medicaments;

    // Constructeurs
    public Ordonnance() {}

    public Ordonnance(Consultation consultation, LocalDate datePrescription) {
        this.consultation = consultation;
        this.datePrescription = datePrescription;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public LocalDate getDatePrescription() {
        return datePrescription;
    }

    public void setDatePrescription(LocalDate datePrescription) {
        this.datePrescription = datePrescription;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDureeTraitement() {
        return dureeTraitement;
    }

    public void setDureeTraitement(String dureeTraitement) {
        this.dureeTraitement = dureeTraitement;
    }

    public List<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(List<Medicament> medicaments) {
        this.medicaments = medicaments;
    }
}