package services;

import interfaces.RendezVousDao;
import models.RendezVous;
import services.RendezVousService;
import java.time.LocalDateTime;
import java.util.List;

public abstract class RendezVousServiceImpl implements RendezVousService {
    private final RendezVousDao rendezVousDao;

    public RendezVousServiceImpl(RendezVousDao rendezVousDao) {
        this.rendezVousDao = rendezVousDao;
    }

    @Override
    public void enregistrerRendezVous(RendezVous rendezVous) {
        rendezVousDao.creer(rendezVous);
    }

    @Override
    public RendezVous obtenirRendezVousParId(int id) {
        return rendezVousDao.trouverParId(id);
    }

    @Override
    public List<RendezVous> listerRendezVousParMedecin(int idMedecin) {
        return rendezVousDao.listerParMedecin(idMedecin);
    }

    @Override
    public List<RendezVous> listerRendezVousParPatient(int idPatient) {
        return rendezVousDao.listerParPatient(idPatient);
    }

    @Override
    public List<RendezVous> listerRendezVousParPeriode(LocalDateTime debut, LocalDateTime fin) {
        return rendezVousDao.listerParPeriode(debut, fin);
    }

    @Override
    public void modifierEtatRendezVous(int id, String nouvelEtat) {
        rendezVousDao.modifierEtat(id, nouvelEtat);
    }

    @Override
    public void annulerRendezVous(int id) {
        rendezVousDao.annuler(id);
    }

    @Override
    public boolean estDisponible(int idMedecin, LocalDateTime dateHeure, int duree) {
        return rendezVousDao.estDisponible(idMedecin, dateHeure, duree);
    }
}