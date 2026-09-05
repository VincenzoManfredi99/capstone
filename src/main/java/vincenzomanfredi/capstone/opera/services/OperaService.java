package vincenzomanfredi.capstone.opera.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vincenzomanfredi.capstone.exceptions.NotFound;
import vincenzomanfredi.capstone.hotspot.entities.Hotspot;
import vincenzomanfredi.capstone.hotspot.services.HotspotService;
import vincenzomanfredi.capstone.opera.entities.Opera;
import vincenzomanfredi.capstone.opera.payloads.OperaDTO;
import vincenzomanfredi.capstone.opera.repositories.OperaRepository;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class OperaService {
    private final OperaRepository operaRepository;
    private final HotspotService hotspotService;
    private final Cloudinary cloudinary;

    public OperaService(OperaRepository operaRepository, HotspotService hotspotService, Cloudinary cloudinary) {
        this.operaRepository = operaRepository;
        this.hotspotService = hotspotService;
        this.cloudinary = cloudinary;
    }

    // Save standard
    public Opera save(OperaDTO payload) {
        Hotspot hotspot = this.hotspotService.findById(payload.hotspotId());

        Opera newOpera = new Opera(
                payload.titolo(),
                payload.descrizione(),
                payload.url_audio(),
                hotspot
        );
        Opera saved = this.operaRepository.save(newOpera);
        log.info("Opera con id " + saved.getId() + " salvata con successo");
        return saved;
    }

    // Save con Cloudinary (MultipartFile)
    public Opera saveOperaConFile(MultipartFile file, String titolo, String descrizione, UUID hotspotId) throws IOException {
        // 'resource_type' => 'auto' dice a Cloudinary di rilevare autonomamente se è un'immagine, un video o un file audio (come l'MP3)
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        String audioUrl = (String) uploadResult.get("secure_url");

        Hotspot hotspot = this.hotspotService.findById(hotspotId);

        Opera newOpera = new Opera(
                titolo,
                descrizione,
                audioUrl,
                hotspot
        );
        Opera saved = this.operaRepository.save(newOpera);
        log.info("Opera con file audio caricato e id " + saved.getId() + " salvata con successo");
        return saved;
    }

    // Get All
    public Page<Opera> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.operaRepository.findAll(pageable);
    }

    // Find by id
    public Opera findById(UUID id) {
        return this.operaRepository.findById(id)
                .orElseThrow(() -> new NotFound("L'opera con id " + id + " non è stata trovata!"));
    }

    // Update
    public Opera findByIdAndUpdate(UUID id, OperaDTO payload) {
        Opera found = this.findById(id);
        Hotspot newHotspot = this.hotspotService.findById(payload.hotspotId());

        found.setTitolo(payload.titolo());
        found.setDescrizione(payload.descrizione());
        found.setUrl_audio(payload.url_audio());
        found.setHotspot(newHotspot);

        Opera updated = this.operaRepository.save(found);
        log.info("Opera con id " + id + " aggiornata con successo!");
        return updated;
    }

    // Delete
    public void findByIdAndDelete(UUID id) {
        Opera found = this.findById(id);
        this.operaRepository.delete(found);
        log.info("Opera con id " + id + " eliminata con successo!");
    }
}