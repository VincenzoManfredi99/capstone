package vincenzomanfredi.capstone.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vincenzomanfredi.capstone.admin.entities.Admin;
import vincenzomanfredi.capstone.admin.repositories.AdminRepository;
import vincenzomanfredi.capstone.ruolo.entities.Ruolo;
import vincenzomanfredi.capstone.ruolo.repositories.RuoloRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RuoloRepository ruoloRepository,
                                   AdminRepository adminRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Controlla e crea il ruolo se non esiste
            Ruolo ruoloAdmin = ruoloRepository.findByDescrizione("ADMIN")
                    .orElseGet(() -> ruoloRepository.save(new Ruolo("ADMIN")));

            // 2. Controlla e crea un Admin di test se la tabella è vuota
            if (adminRepository.count() == 0) {
                Admin admin = new Admin(
                        "Mario",
                        "Rossi",
                        "admin@example.com",
                        passwordEncoder.encode("password123"),
                        ruoloAdmin
                );
                adminRepository.save(admin);
                System.out.println(">>> Utente Admin di test creato con successo! (email: admin@example.com, password: password123)");
            }
        };
    }
}
