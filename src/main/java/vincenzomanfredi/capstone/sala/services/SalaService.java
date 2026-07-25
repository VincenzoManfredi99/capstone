package vincenzomanfredi.capstone.sala.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.exceptions.NotFound;
import vincenzomanfredi.capstone.sala.entities.Sala;
import vincenzomanfredi.capstone.sala.payloads.SalaDTO;
import vincenzomanfredi.capstone.sala.repositories.SalaRepository;

import java.util.UUID;

@Service
@Slf4j
public class SalaService {

    private final SalaRepository salaRepository;

    public SalaService(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    //Save
    public Sala save(SalaDTO payload) {
        if (this.salaRepository.existsByNome(payload.nome())) {
            throw new BadRequest("Questo nome è già in uso");
        }

        Sala newSala = new Sala(
                payload.nome(),
                payload.descrizione()
        );

        Sala saved = this.salaRepository.save(newSala);
        log.info("Sala " + saved.getNome() + " salvata con successo!");
        return saved;
    }

    //Get All
    public Page<Sala> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.salaRepository.findAll(pageable);
    }

    //Find by id
    public Sala findById(UUID id) {
        return this.salaRepository.findById(id).orElseThrow(() -> new NotFound("La sala con questo id non è stato trovato"));
    }

    //Update
    public Sala findByIdAndUpdate(UUID id, SalaDTO payload) {
        Sala found = this.findById(id);

        found.setNome(payload.nome());
        found.setDescrizione(payload.descrizione());

        Sala updated = this.salaRepository.save(found);
        log.info("Sala con id " + id + " aggiornata con successo");
        return updated;
    }

    //Delete
    public void findByIdAndDelete(UUID id) {
        Sala found = this.findById(id);
        this.salaRepository.delete(found);
        log.info("Sala con id " + id + " eliminato con successo!");
    }
}
