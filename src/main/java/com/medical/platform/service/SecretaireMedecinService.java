package com.medical.platform.service;

import com.medical.platform.entity.SecretaireMedecin;
import com.medical.platform.entity.Medecin;
import com.medical.platform.entity.Secretaire;
import com.medical.platform.exception.ResourceNotFoundException;
import com.medical.platform.repository.SecretaireMedecinRepository;
import com.medical.platform.repository.MedecinRepository;
import com.medical.platform.repository.SecretaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SecretaireMedecinService {

    @Autowired
    private SecretaireMedecinRepository secretaireMedecinRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private SecretaireRepository secretaireRepository;

    public List<SecretaireMedecin> findAll() {
        return secretaireMedecinRepository.findAll();
    }

    public Optional<SecretaireMedecin> findById(Integer id) {
        return secretaireMedecinRepository.findById(id);
    }

    public List<SecretaireMedecin> findByMedecin(Integer medecinId) {
        return secretaireMedecinRepository.findByMedecinId(medecinId);
    }

    public List<SecretaireMedecin> findBySecretaire(Integer secretaireId) {
        return secretaireMedecinRepository.findBySecretaireId(secretaireId);
    }

    public List<SecretaireMedecin> findActiveSecretairesForMedecin(Integer medecinId) {
        return secretaireMedecinRepository.findActiveSecretairesForMedecin(medecinId);
    }

    @Transactional
    public SecretaireMedecin affecterSecretaire(Integer medecinId, Integer secretaireId) {
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new ResourceNotFoundException("Médecin non trouvé avec l'ID: " + medecinId));
        Secretaire secretaire = secretaireRepository.findById(secretaireId)
                .orElseThrow(() -> new ResourceNotFoundException("Secrétaire non trouvée avec l'ID: " + secretaireId));

        SecretaireMedecin affectation = new SecretaireMedecin();
        affectation.setMedecin(medecin);
        affectation.setSecretaire(secretaire);
        affectation.setDateAffectation(LocalDateTime.now());

        return secretaireMedecinRepository.save(affectation);
    }

    @Transactional
    public SecretaireMedecin save(SecretaireMedecin secretaireMedecin) {
        if (secretaireMedecin.getDateAffectation() == null) {
            secretaireMedecin.setDateAffectation(LocalDateTime.now());
        }

        return secretaireMedecinRepository.save(secretaireMedecin);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!secretaireMedecinRepository.existsById(id)) {
            throw new ResourceNotFoundException("Affectation secrétaire-médecin non trouvée avec l'ID: " + id);
        }
        secretaireMedecinRepository.deleteById(id);
    }

    @Transactional
    public void desaffecterSecretaire(Integer medecinId, Integer secretaireId) {
        List<SecretaireMedecin> affectations = secretaireMedecinRepository.findByMedecinId(medecinId);

        for (SecretaireMedecin affectation : affectations) {
            if (affectation.getSecretaire().getId().equals(secretaireId)) {
                secretaireMedecinRepository.deleteById(affectation.getId());
                return;
            }
        }

        throw new ResourceNotFoundException("Affectation non trouvée pour ce médecin et cette secrétaire");
    }
}


