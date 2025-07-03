package interfaces;

import models.Patient;
import java.util.List;

public interface PatientDao {
    void creer(Patient patient);
    Patient trouverParId(int id);
    List<Patient> listerTous();
    List<Patient> rechercherParNom(String nom);
    void modifier(Patient patient);
    void supprimer(int id);
}