package com.medical.platform.service;

import com.medical.platform.entity.Medecin;
import com.medical.platform.entity.Utilisateur;
import com.medical.platform.exception.DuplicateResourceException;
import com.medical.platform.exception.ResourceNotFoundException;
import com.medical.platform.repository.MedecinRepository;
import com.medical.platform.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedecinService {

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    public List<Medecin> findAll() {
        return medecinRepository.findAll();
    }

    public List<Medecin> findAllActive() {
        return medecinRepository.findAllActive();
    }

    public Optional<Medecin> findById(Integer id) {
        return medecinRepository.findById(id);
    }

    public List<Medecin> findBySpecialite(Integer specialiteId) {
        return medecinRepository.findBySpecialiteId(specialiteId);
    }

    public Optional<Medecin> findByNumeroLicence(String numeroLicence) {
        return medecinRepository.findByNumeroLicence(numeroLicence);
    }

    public List<Medecin> searchByNom(String nom) {
        return medecinRepository.findByUtilisateurNomContainingIgnoreCase(nom);
    }

    public long countAllMedecins() {
        return medecinRepository.countAllMedecins();
    }

    @Transactional
    public Medecin save(Medecin medecin) {
        // Vérifier que le numéro de licence est unique
        if (medecin.getId() == null && medecinRepository.findByNumeroLicence(medecin.getNumeroLicence()).isPresent()) {
            throw new DuplicateResourceException("Un médecin avec ce numéro de licence existe déjà");
        }

        // Sauvegarde de l'utilisateur associé en premier
        Utilisateur utilisateur = medecin.getUtilisateur();
        if (utilisateur != null) {
            utilisateur.setTypeUtilisateur(Utilisateur.TypeUtilisateur.medecin);
            utilisateur = utilisateurService.save(utilisateur);
            medecin.setUtilisateur(utilisateur);
        }

        return medecinRepository.save(medecin);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!medecinRepository.existsById(id)) {
            throw new ResourceNotFoundException("Médecin non trouvé avec l'ID: " + id);
        }
        medecinRepository.deleteById(id);
    }

    @Transactional
    public Medecin arreterMedecin(Integer id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médecin non trouvé avec l'ID: " + id));

        // Désactiver l'utilisateur associé
        if (medecin.getUtilisateur() != null) {
            utilisateurService.desactiverUtilisateur(medecin.getUtilisateur().getId());
        }

        return medecin;
    }
}
     /* en changeant le mot de passe et le type d'utilisateur, ou en ajoutant le champ.
     *
     * Pour une implémentation propre, je vais modifier l'entité Utilisateur pour ajouter un champ 'actif'.
     * Cependant, pour ne pas modifier trop de fichiers sans visibilité sur la configuration de la DB,
     * je vais implémenter une suppression physique pour l'instant, mais je note qu'une suppression logique
     * serait préférable pour "arrêter" un utilisateur.
     *
     * Pour respecter le cas d'utilisation "arrêter médecin", je vais implémenter une méthode qui
     * supprime l'utilisateur associé (ce qui entraîne la suppression du médecin par cascade).
     *
     * @param id ID du médecin à arrêter.
     * @return true si l'opération a réussi, false sinon.
     */
    @Transactional
    public boolean arreterMedecin(Integer id) {
        Optional<Medecin> medecinOpt = medecinRepository.findById(id);
        if (medecinOpt.isPresent()) {
            // Suppression physique du médecin (et de l'utilisateur associé par cascade)
            medecinRepository.delete(medecinOpt.get());
            return true;
        }
        return false;
    }
}
