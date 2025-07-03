package services;

import interfaces.ConsultationDao;
import models.Consultation;
import services.ConsultationService;
import java.util.List;

public class ConsultationServiceImpl implements ConsultationService {
    private final ConsultationDao consultationDao;

    public ConsultationServiceImpl(ConsultationDao consultationDao) {
        this.consultationDao = consultationDao;
    }

    @Override
    public void enregistrerConsultation(Consultation consultation) {
        consultationDao.creer(consultation);
    }

    @Override
    public Consultation obtenirConsultationParId(int id) {
        return consultationDao.trouverParId(id);
    }

    @Override
    public List<Consultation> listerConsultationsParPatient(int idPatient) {
        return consultationDao.listerParPatient(idPatient);
    }

    @Override
    public List<Consultation> listerConsultationsParMedecin(int idMedecin) {
        return consultationDao.listerParMedecin(idMedecin);
    }

    @Override
    public void ajouterNotesConsultation(int id, String notes) {
        consultationDao.ajouterNotes(id, notes);
    }
}
