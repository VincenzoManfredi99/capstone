package vincenzomanfredi.capstone.museo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vincenzomanfredi.capstone.admin.entities.Admin;
import vincenzomanfredi.capstone.admin.services.AdminService;
import vincenzomanfredi.capstone.exceptions.NotFound;
import vincenzomanfredi.capstone.museo.entities.Museo;
import vincenzomanfredi.capstone.museo.payloads.MuseoDTO;
import vincenzomanfredi.capstone.museo.repositories.MuseoRepository;

import java.util.UUID;

@Service
@Slf4j
public class MuseoService {
    private final MuseoRepository museoRepository;
    private final AdminService adminService;

    public MuseoService(MuseoRepository museoRepository, AdminService adminService) {
        this.museoRepository = museoRepository;
        this.adminService = adminService;
    }

    //Save
    public Museo save(MuseoDTO payload) {
        Admin utente = this.adminService.findById(payload.utenteId());

        Museo newMuseo = new Museo(
                payload.denominazione(),
                payload.indirizzo(),
                payload.citta(),
                payload.provincia(),
                payload.cap(),
                utente,
                payload.accessoMuseo()
        );
        return this.museoRepository.save(newMuseo);
    }

    //Get All
    public Page<Museo> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.museoRepository.findAll(pageable);
    }

    //Find by id
    public Museo findById(UUID id) {
        return this.museoRepository.findById(id)
                .orElseThrow(() -> new NotFound("Museo con id " + id + " non trovato!"));
    }

    //Update
    public Museo findByIdAndUpdate(UUID id, MuseoDTO payload) {
        Museo found = this.findById(id);
        Admin utente = this.adminService.findById(payload.utenteId());

        found.setDenominazione(payload.denominazione());
        found.setIndirizzo(payload.indirizzo());
        found.setCitta(payload.citta());
        found.setProvincia(payload.provincia());
        found.setCap(payload.cap());
        found.setUtente(utente);
        found.setAccessoMuseo(payload.accessoMuseo());

        return this.museoRepository.save(found);
    }

    //Delete
    public void findByIdAndDelete(UUID id) {
        Museo found = this.findById(id);
        this.museoRepository.delete(found);
    }

}
