package com.medical.platform.service;

import com.medical.platform.entity.Medecin;
import com.medical.platform.entity.Patient;
import com.medical.platform.entity.RendezVous;
import com.medical.platform.entity.Secretaire;
import com.medical.platform.repository.MedecinRepository;
import com.medical.platform.repository.PatientRepository;
import com.medical.platform.repository.RendezVousRepository;
import com.medical.platform.repository.SecretaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatistiqueService {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private SecretaireRepository secretaireRepository;

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Génère des statistiques générales sur la plateforme.
     * @return Map<String, Object> contenant les statistiques.
     */
    public Map<String, Object> getGeneralStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalMedecins", medecinRepository.countAllMedecins());
        stats.put("totalSecretaires", secretaireRepository.countAllSecretaires());
        stats.put("totalPatients", patientRepository.countAllPatients());

        List<RendezVous> allRendezVous = rendezVousRepository.findAll();
        stats.put("totalRendezVous", allRendezVous.size());

        Map<RendezVous.StatutRendezVous, Long> rendezVousParStatut = allRendezVous.stream()
                .collect(Collectors.groupingBy(RendezVous::getStatut, Collectors.counting()));
        stats.put("rendezVousParStatut", rendezVousParStatut);

        long consultationsTerminees = rendezVousRepository.countByStatut(RendezVous.StatutRendezVous.termine);
        long consultationsPlanifiees = rendezVousRepository.countByStatut(RendezVous.StatutRendezVous.planifie);
        long consultationsAnnulees = rendezVousRepository.countByStatut(RendezVous.StatutRendezVous.annule);

        stats.put("consultationsTerminees", consultationsTerminees);
        stats.put("consultationsPlanifiees", consultationsPlanifiees);
        stats.put("consultationsAnnulees", consultationsAnnulees);

        if (allRendezVous.size() > 0) {
            double tauxCompletude = (double) consultationsTerminees / allRendezVous.size() * 100;
            stats.put("tauxCompletude", String.format("%.2f", tauxCompletude) + "%");
        }

        return stats;
    }

    /**
     * Statistiques pour chaque médecin.
     * @return List<Map<String, Object>> contenant les métriques de chaque médecin.
     */
    public List<Map<String, Object>> getStatistiquesMedecins() {
        List<Medecin> medecins = medecinRepository.findAll();

        return medecins.stream()
                .map(medecin -> {
                    Map<String, Object> medecinStats = new HashMap<>();
                    medecinStats.put("medecinId", medecin.getId());
                    medecinStats.put("medecinNom", medecin.getUtilisateur().getNom() + " " + medecin.getUtilisateur().getPrenom());
                    medecinStats.put("specialite", medecin.getSpecialite().getNom());

                    long totalRendezVous = rendezVousRepository.countByMedecin(medecin.getId());
                    List<RendezVous> rendezVousMedecin = rendezVousRepository.findByMedecinId(medecin.getId());

                    long termines = rendezVousMedecin.stream()
                            .filter(rv -> rv.getStatut() == RendezVous.StatutRendezVous.termine)
                            .count();
                    long confirmes = rendezVousMedecin.stream()
                            .filter(rv -> rv.getStatut() == RendezVous.StatutRendezVous.confirme)
                            .count();
                    long annules = rendezVousMedecin.stream()
                            .filter(rv -> rv.getStatut() == RendezVous.StatutRendezVous.annule)
                            .count();

                    medecinStats.put("totalRendezVous", totalRendezVous);
                    medecinStats.put("rendezVousTermines", termines);
                    medecinStats.put("rendezVousConfirmes", confirmes);
                    medecinStats.put("rendezVousAnnules", annules);

                    if (totalRendezVous > 0) {
                        double tauxCompletude = (double) termines / totalRendezVous * 100;
                        medecinStats.put("tauxCompletude", String.format("%.2f", tauxCompletude) + "%");
                    }

                    return medecinStats;
                })
                .collect(Collectors.toList());
    }

    /**
     * Évalue la performance d'un médecin.
     * @param medecinId ID du médecin.
     * @return Map<String, Object> contenant les métriques de performance.
     */
    public Map<String, Object> evaluateMedecin(Integer medecinId) {
        Map<String, Object> evaluation = new HashMap<>();

        List<RendezVous> rendezVousMedecin = rendezVousRepository.findByMedecinId(medecinId);

        long termines = rendezVousMedecin.stream()
                .filter(rv -> rv.getStatut() == RendezVous.StatutRendezVous.termine)
                .count();
        long confirmes = rendezVousMedecin.stream()
                .filter(rv -> rv.getStatut() == RendezVous.StatutRendezVous.confirme)
                .count();
        long annules = rendezVousMedecin.stream()
                .filter(rv -> rv.getStatut() == RendezVous.StatutRendezVous.annule)
                .count();

        evaluation.put("totalRendezVous", rendezVousMedecin.size());
        evaluation.put("rendezVousTermines", termines);
        evaluation.put("rendezVousConfirmes", confirmes);
        evaluation.put("rendezVousAnnules", annules);

        if (rendezVousMedecin.size() > 0) {
            double tauxCompletude = (double) termines / rendezVousMedecin.size() * 100;
            double tauxAnnulation = (double) annules / rendezVousMedecin.size() * 100;
            evaluation.put("tauxCompletude", String.format("%.2f", tauxCompletude) + "%");
            evaluation.put("tauxAnnulation", String.format("%.2f", tauxAnnulation) + "%");
        }

        return evaluation;
    }

    /**
     * Évalue la performance d'une secrétaire.
     * @param secretaireId ID de la secrétaire.
     * @return Map<String, Object> contenant les métriques de performance.
     */
    public Map<String, Object> evaluateSecretaire(Integer secretaireId) {
        Map<String, Object> evaluation = new HashMap<>();

        // Pour la secrétaire, on peut compter les rendez-vous qu'elle a gérés
        // (nombre de médecins affectés, nombre total de rendez-vous gérés, etc.)

        evaluation.put("secretaireId", secretaireId);
        evaluation.put("message", "Évaluation secrétaire disponible");

        return evaluation;
    }

    /**
     * Rendez-vous planifiés à venir.
     * @return List<Map<String, Object>> contenant les rendez-vous à venir.
     */
    public List<Map<String, Object>> getRendezVousAVenir() {
        LocalDateTime maintenant = LocalDateTime.now();
        List<RendezVous> rendezVousAVenir = rendezVousRepository.findAll().stream()
                .filter(rv -> rv.getDateHeure().isAfter(maintenant))
                .filter(rv -> rv.getStatut() != RendezVous.StatutRendezVous.annule)
                .sorted((rv1, rv2) -> rv1.getDateHeure().compareTo(rv2.getDateHeure()))
                .collect(Collectors.toList());

        return rendezVousAVenir.stream()
                .map(rv -> {
                    Map<String, Object> info = new HashMap<>();
                    info.put("id", rv.getId());
                    info.put("patient", rv.getPatient().getUtilisateur().getNom() + " " + rv.getPatient().getUtilisateur().getPrenom());
                    info.put("medecin", rv.getMedecin().getUtilisateur().getNom() + " " + rv.getMedecin().getUtilisateur().getPrenom());
                    info.put("dateHeure", rv.getDateHeure());
                    info.put("statut", rv.getStatut());
                    return info;
                })
                .collect(Collectors.toList());
    }

    /**
     * Rendez-vous par spécialité.
     * @return Map<String, Long> contenant le nombre de rendez-vous par spécialité.
     */
    public Map<String, Long> getRendezVousParSpecialite() {
        List<RendezVous> allRendezVous = rendezVousRepository.findAll();

        return allRendezVous.stream()
                .collect(Collectors.groupingBy(
                        rv -> rv.getMedecin().getSpecialite().getNom(),
                        Collectors.counting()
                ));
    }

    /**
     * Taux de confirmation des rendez-vous.
     * @return Map<String, Object> contenant le taux de confirmation.
     */
    public Map<String, Object> getTauxConfirmation() {
        Map<String, Object> stats = new HashMap<>();

        List<RendezVous> allRendezVous = rendezVousRepository.findAll();
        long confirmes = rendezVousRepository.countByStatut(RendezVous.StatutRendezVous.confirme);

        if (allRendezVous.size() > 0) {
            double taux = (double) confirmes / allRendezVous.size() * 100;
            stats.put("tauxConfirmation", String.format("%.2f", taux) + "%");
            stats.put("confirmesCount", confirmes);
            stats.put("totalCount", allRendezVous.size());
        } else {
            stats.put("tauxConfirmation", "0%");
            stats.put("confirmesCount", 0);
            stats.put("totalCount", 0);
        }

        return stats;
    }
}
        Map<String, Object> evaluation = new HashMap<>();
        Optional<Medecin> medecin = medecinRepository.findById(medecinId);

        if (medecin.isEmpty()) {
            evaluation.put("error", "Médecin non trouvé");
            return evaluation;
        }

        List<RendezVous> rendezVousMedecin = rendezVousRepository.findByMedecin(medecin.get());
        long totalRendezVous = rendezVousMedecin.size();
        long rvConfirme = rendezVousMedecin.stream().filter(rv -> rv.getStatut() == RendezVous.StatutRendezVous.confirme).count();
        long rvAnnule = rendezVousMedecin.stream().filter(rv -> rv.getStatut() == RendezVous.StatutRendezVous.annule).count();
        long rvTermine = rendezVousMedecin.stream().filter(rv -> rv.getStatut() == RendezVous.StatutRendezVous.termine).count();

        evaluation.put("medecinId", medecinId);
        evaluation.put("totalRendezVous", totalRendezVous);
        evaluation.put("rendezVousConfirmes", rvConfirme);
        evaluation.put("rendezVousAnnules", rvAnnule);
        evaluation.put("rendezVousTermines", rvTermine);
        // Ajout d'un score simple
        double score = totalRendezVous > 0 ? (double) rvTermine / totalRendezVous : 0.0;
        evaluation.put("tauxDeCompletion", String.format("%.2f%%", score * 100));

        return evaluation;
    }

    /**
     * Évalue la performance d'une secrétaire (cas d'utilisation "évaluer secrétaire").
     * Pour l'instant, nous nous basons sur le nombre de médecins affectés.
     * @param secretaireId ID de la secrétaire.
     * @return Map<String, Object> contenant les métriques de performance.
     */
    public Map<String, Object> evaluateSecretaire(Integer secretaireId) {
        Map<String, Object> evaluation = new HashMap<>();
        Optional<Secretaire> secretaire = secretaireRepository.findById(secretaireId);

        if (secretaire.isEmpty()) {
            evaluation.put("error", "Secrétaire non trouvée");
            return evaluation;
        }

        // Le nombre de médecins affectés est une métrique simple pour l'évaluation
        int medecinsAffectes = secretaire.get().getMedecinAffectations().size();

        evaluation.put("secretaireId", secretaireId);
        evaluation.put("medecinsAffectes", medecinsAffectes);
        // D'autres métriques pourraient être ajoutées ici (ex: nombre de RV gérés par les médecins affectés)

        return evaluation;
    }
}
