package org.example.logitrack.config;

import lombok.RequiredArgsConstructor;
import org.example.logitrack.entities.enums.Role;
import org.example.logitrack.entities.enums.Specialite;
import org.example.logitrack.entities.enums.TransporteurStatut;
import org.example.logitrack.entities.models.User;
import org.example.logitrack.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // ================= ADMIN =================
        if (userRepository.findByLogin("admin").isEmpty()) {
            User admin = new User();
            admin.setLogin("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);

            userRepository.save(admin);
            System.out.println("✅ Admin créé : login=admin / password=admin123");
        }

        // ================= TRANSPORTEUR STANDARD =================
        createTransporteurIfNotExists(
                "transporteur_standard",
                "pass123",
                Specialite.STANDARD
        );

        // ================= TRANSPORTEUR FRAGILE =================
        createTransporteurIfNotExists(
                "transporteur_fragile",
                "pass123",
                Specialite.FRAGILE
        );

        // ================= TRANSPORTEUR FRIGO =================
        createTransporteurIfNotExists(
                "transporteur_frigo",
                "pass123",
                Specialite.FRIGO
        );
    }

    private void createTransporteurIfNotExists(String login, String password, Specialite specialite) {
        if (userRepository.findByLogin(login).isEmpty()) {
            User transporteur = new User();
            transporteur.setLogin(login);
            transporteur.setPassword(passwordEncoder.encode(password));
            transporteur.setRole(Role.TRANSPORTEUR);
            transporteur.setStatut(TransporteurStatut.DISPONIBLE);
            transporteur.setSpecialite(specialite);
            transporteur.setActive(true);

            userRepository.save(transporteur);
            System.out.println("✅ Transporteur créé : " + login + " | " + specialite);
        }
    }
}
