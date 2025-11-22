package com.medical.platform.controller;

import com.medical.platform.dto.AuthResponse;
import com.medical.platform.dto.LoginRequest;
import com.medical.platform.entity.Utilisateur;
import com.medical.platform.service.UtilisateurService;
import com.medical.platform.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<Utilisateur> utilisateurOpt = utilisateurService.findByEmail(loginRequest.getEmail());

        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        // Vérifier que l'utilisateur est actif
        if (!utilisateur.getActif()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Compte utilisateur désactivé");
        }

        // Valider le mot de passe
        if (!utilisateurService.validatePassword(loginRequest.getMotDePasse(), utilisateur.getMotDePasse())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
        }

        // Générer le token JWT
        String token = jwtUtil.generateToken(utilisateur.getEmail());

        AuthResponse response = new AuthResponse(
                token,
                utilisateur.getEmail(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getTypeUtilisateur().toString()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Déconnexion réussie");
    }

    @GetMapping("/validate/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String token) {
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractEmail(token);
            return ResponseEntity.ok("Token valide pour : " + email);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide ou expiré");
        }
    }
}

