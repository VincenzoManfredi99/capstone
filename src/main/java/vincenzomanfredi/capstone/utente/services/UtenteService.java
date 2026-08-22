package vincenzomanfredi.capstone.utente.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.exceptions.NotFound;

import vincenzomanfredi.capstone.utente.entities.Utente;
import vincenzomanfredi.capstone.utente.payloads.UtenteDTO;
import vincenzomanfredi.capstone.utente.repositories.UtenteRepository;

import java.util.UUID;

@Service
@Slf4j
public class UtenteService {
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder bcrypt;


    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder bcrypt) {
        this.utenteRepository = utenteRepository;
        this.bcrypt = bcrypt;
    }


    //Save
    public Utente save(UtenteDTO payload) {
        this.utenteRepository.findByEmail(payload.email()).ifPresent(admin -> {
            throw new BadRequest("L'email " + payload.email() + " è già in uso!");
        });


        Utente newUtente = new Utente(
                payload.nome(),
                payload.cognome(),
                payload.email(),
                this.bcrypt.encode(payload.password())
        );

        return this.utenteRepository.save(newUtente);
    }

    //Get All
    public Page<Utente> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.utenteRepository.findAll(pageable);
    }

    //Get By id
    public Utente findById(UUID id) {
        return this.utenteRepository.findById(id).orElseThrow(() -> new NotFound("Utente con id " + id + " non trovato!"));
    }

    //Find by email
    public Utente findByEmail(String email) {
        return this.utenteRepository.findByEmail(email).orElseThrow(() -> new NotFound("Utente con questa email " + email + " non trovata"));
    }

    //Update
    public Utente findByIdAndUpdate(UUID id, UtenteDTO payload) {
        Utente found = this.findById(id);

        if (!found.getEmail().equals(payload.email())) {
            this.utenteRepository.findByEmail(payload.email()).ifPresent(admin -> {
                throw new BadRequest("L'email " + payload.email() + " è già in uso!");
            });
        }


        found.setNome(payload.nome());
        found.setCognome(payload.cognome());
        found.setEmail(payload.email());
        found.setPassword(this.bcrypt.encode(payload.password()));


        return this.utenteRepository.save(found);
    }

    //Delete
    public void findByIdAndDelete(UUID id) {
        Utente found = this.findById(id);
        this.utenteRepository.delete(found);
    }
}
