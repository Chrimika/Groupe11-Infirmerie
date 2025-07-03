package services;

import interfaces.MedecinDao;
import models.Medecin;
import services.MedecinService;
import java.util.List;

public class MedecinServiceImpl implements MedecinService {
    private final MedecinDao medecinDao;

    public MedecinServiceImpl(MedecinDao medecinDao) {
        this.medecinDao = medecinDao;
    }

    @Override
    public void enregistrerMedecin(Medecin medecin) {
        medecinDao.creer(medecin);
    }

    @Override
    public Medecin obtenirMedecinParId(int id) {
        return medecinDao.trouverParId(id);
    }

    @Override
    public List<Medecin> listerTousLesMedecins() {
        return medecinDao.listerTous();
    }

    @Override
    public List<Medecin> trouverParSpecialite(String specialite) {
        return medecinDao.trouverParSpecialite(specialite);
    }

    @Override
    public void desactiverMedecin(int id) {
        medecinDao.desactiver(id);
    }
}