package interfaces;

import models.Medecin;
import java.util.List;

public interface MedecinDao {
    void creer(Medecin medecin);
    Medecin trouverParId(int id);
    List<Medecin> listerTous();
    List<Medecin> trouverParSpecialite(String specialite);
    void modifier(Medecin medecin);
    void desactiver(int id);
}