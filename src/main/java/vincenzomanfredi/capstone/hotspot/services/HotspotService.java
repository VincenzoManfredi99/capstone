package vincenzomanfredi.capstone.hotspot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vincenzomanfredi.capstone.exceptions.NotFound;
import vincenzomanfredi.capstone.hotspot.entities.Hotspot;
import vincenzomanfredi.capstone.hotspot.payloads.HotspotDTO;
import vincenzomanfredi.capstone.hotspot.repositories.HotspotRepository;
import vincenzomanfredi.capstone.scena.entities.Scena;
import vincenzomanfredi.capstone.scena.repositories.ScenaRepository;

import java.util.UUID;

@Service
@Slf4j
public class HotspotService {
    private final HotspotRepository hotspotRepository;
    private final ScenaRepository scenaRepository;

    public HotspotService(HotspotRepository hotspotRepository, ScenaRepository scenaRepository) {
        this.hotspotRepository = hotspotRepository;
        this.scenaRepository = scenaRepository;
    }

    //Save
    public Hotspot save(HotspotDTO payload) {
        Scena scena = this.scenaRepository.findById(payload.scenaId()).orElseThrow(() -> new NotFound("Hotspot con id" + payload.scenaId() + " non trovata"));

        Scena targetScena = null;
        if (payload.targetScenaId() != null) {
            targetScena = this.scenaRepository.findById(payload.targetScenaId()).orElseThrow(() -> new NotFound("Scena target con id " + payload.targetScenaId() + " non trovata"));
        }

        Hotspot newHotspot = new Hotspot(
                payload.tipo(),
                payload.pitch(),
                payload.yaw(),
                scena,
                payload.titolo(),
                payload.descrizione(),
                payload.immagine(),
                payload.file3D(),
                payload.audio(),
                targetScena
        );

        Hotspot saved = this.hotspotRepository.save(newHotspot);
        log.info("Hotspot salvato con successo nella scena: " + scena.getId());
        return saved;
    }

    //Get All
    public Page<Hotspot> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.hotspotRepository.findAll(pageable);
    }

    //Find by Id
    public Hotspot findById(UUID id) {
        return this.hotspotRepository.findById(id).orElseThrow(() -> new NotFound("L'hotspot con questo id non è stato trovato"));
    }

    //Update
    public Hotspot findByIdAndUpdate(UUID id, HotspotDTO payload) {
        Hotspot found = this.findById(id);

        Scena newScena = this.scenaRepository.findById(payload.scenaId()).orElseThrow(() -> new NotFound("Scena con id " + payload.scenaId() + " non trovata!"));
        Scena targetScena = null;
        if (payload.targetScenaId() != null) {
            targetScena = this.scenaRepository.findById(payload.targetScenaId()).orElseThrow(() -> new NotFound("Scena target con id " + payload.targetScenaId() + " non trovata"));
        }

        found.setTipo(payload.tipo());
        found.setPitch(payload.pitch());
        found.setYaw(payload.yaw());
        found.setScena(newScena);
        found.setTitolo(payload.titolo());
        found.setDescrizione(payload.descrizione());
        found.setImmagine(payload.immagine());
        found.setFile3D(payload.file3D());
        found.setAudio(payload.audio());
        found.setTargetScenaId(targetScena);

        Hotspot updated = this.hotspotRepository.save(found);
        log.info("Hotspot con id " + id + " aggiornata con successo");
        return updated;
    }

    //Delete
    public void findByIdAndDelete(UUID id) {
        Hotspot found = this.findById(id);
        this.hotspotRepository.delete(found);
        log.info("Hotspot con id " + id + " eliminato con successo!");
    }
}
