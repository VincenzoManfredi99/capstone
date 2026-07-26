package vincenzomanfredi.capstone.ruolo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.exceptions.NotFound;
import vincenzomanfredi.capstone.ruolo.entities.Ruolo;
import vincenzomanfredi.capstone.ruolo.payloads.RuoloDTO;
import vincenzomanfredi.capstone.ruolo.repositories.RuoloRepository;

import java.util.UUID;

@Service
@Slf4j
public class RuoloService {
    private final RuoloRepository ruoloRepository;

    public RuoloService(RuoloRepository ruoloRepository) {
        this.ruoloRepository = ruoloRepository;
    }

    //Save
    public Ruolo save(RuoloDTO payload) {
        if (this.ruoloRepository.existsByDescrizione(payload.descrizione())) {
            throw new BadRequest("Questo ruolo esiste già");
        }

        Ruolo newRuolo = new Ruolo(
                payload.descrizione()
        );

        Ruolo saved = this.ruoloRepository.save(newRuolo);
        log.info("Ruolo: " + saved.getDescrizione() + " salvato con successo");
        return saved;
    }

    //Get All
    public Page<Ruolo> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.ruoloRepository.findAll(pageable);
    }

    //Find By id
    public Ruolo findById(UUID id) {
        return this.ruoloRepository.findById(id).orElseThrow(() -> new NotFound("Il ruolo con ID " + id + "non è stato trovato"));
    }

    //Update
    public Ruolo findByIdAndUpdate(UUID id, RuoloDTO payload) {
        Ruolo found = this.findById(id);

        found.setDescrizione(payload.descrizione());

        Ruolo updated = this.ruoloRepository.save(found);
        log.info("Ruolo con ID " + id + " aggiornato con successo!");
        return updated;
    }

    //Delete
    public void deleteById(UUID id) {
        Ruolo found = this.findById(id);
        this.ruoloRepository.delete(found);
        log.info("Ruolo con ID " + id + " eliminato con successo!");
    }
}

