package vincenzomanfredi.capstone.scena.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vincenzomanfredi.capstone.exceptions.NotFound;
import vincenzomanfredi.capstone.sala.entities.Sala;
import vincenzomanfredi.capstone.sala.repositories.SalaRepository;
import vincenzomanfredi.capstone.scena.entities.Scena;
import vincenzomanfredi.capstone.scena.payloads.ScenaDTO;
import vincenzomanfredi.capstone.scena.repositories.ScenaRepository;

import java.util.UUID;

@Service
@Slf4j
public class ScenaService {
    private final ScenaRepository scenaRepository;
    private final SalaRepository salaRepository;

    public ScenaService(ScenaRepository scenaRepository, SalaRepository salaRepository) {
        this.scenaRepository = scenaRepository;
        this.salaRepository = salaRepository;
    }

    //Save
    public Scena save(ScenaDTO payload) {
        Sala sala = this.salaRepository.findById(payload.salaId()).orElseThrow(() -> new NotFound("Sala con id" + payload.salaId() + " non trovata"));

        Scena newScena = new Scena(
                payload.foto(),
                sala
        );

        Scena saved = this.scenaRepository.save(newScena);
        log.info("Scena salvata con successo nella sala: " + sala.getNome());
        return saved;
    }

    //Get All
    public Page<Scena> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.scenaRepository.findAll(pageable);
    }

    //Find by id
    public Scena findById(UUID id) {
        return this.scenaRepository.findById(id).orElseThrow(() -> new NotFound("La scena con questo id non è stato trovato"));
    }

    //Update
    public Scena findByIdAndUpdate(UUID id, ScenaDTO payload) {
        Scena found = this.findById(id);

        Sala nuovaSala = this.salaRepository.findById(payload.salaId()).orElseThrow(() -> new NotFound("Sala con id " + payload.salaId() + " non trovata!"));

        found.setFoto(payload.foto());
        found.setSala(nuovaSala);

        Scena updated = this.scenaRepository.save(found);
        log.info("Scena con id " + id + " aggiornata con successo");
        return updated;
    }

    //Delete
    public void findByIdAndDelete(UUID id) {
        Scena found = this.findById(id);
        this.scenaRepository.delete(found);
        log.info("Scena con id " + id + " eliminato con successo!");
    }
}
