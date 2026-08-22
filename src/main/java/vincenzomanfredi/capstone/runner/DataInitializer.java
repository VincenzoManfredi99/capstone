package vincenzomanfredi.capstone.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vincenzomanfredi.capstone.utente.entities.Utente;
import vincenzomanfredi.capstone.utente.repositories.UtenteRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UtenteRepository utenteRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            if (utenteRepository.count() == 0) {
                Utente utente = new Utente(
                        "Mario",
                        "Rossi",
                        "admin@example.com",
                        passwordEncoder.encode("password123")
                );
                utenteRepository.save(utente);
                System.out.println(">>> Utente Admin di test creato con successo! (email: admin@example.com, password: password123)");
            }
        };
    }
}