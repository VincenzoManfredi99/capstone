package vincenzomanfredi.capstone.admin.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vincenzomanfredi.capstone.admin.entities.Admin;
import vincenzomanfredi.capstone.admin.payloads.AdminDTO;
import vincenzomanfredi.capstone.admin.repositories.AdminRepository;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.exceptions.NotFound;
import vincenzomanfredi.capstone.ruolo.entities.Ruolo;
import vincenzomanfredi.capstone.ruolo.services.RuoloService;

import java.util.UUID;

@Service
@Slf4j
public class AdminService {
    private final AdminRepository adminRepository;
    private final RuoloService ruoloService;

    public AdminService(AdminRepository adminRepository, RuoloService ruoloService) {
        this.adminRepository = adminRepository;
        this.ruoloService = ruoloService;
    }


    //Save
    public Admin save(AdminDTO payload) {
        this.adminRepository.findByEmail(payload.email()).ifPresent(admin -> {
            throw new BadRequest("L'email " + payload.email() + " è già in uso!");
        });

        Ruolo ruolo = this.ruoloService.findById(payload.ruoloId());

        Admin newAdmin = new Admin(
                payload.nome(),
                payload.cognome(),
                payload.email(),
                payload.password(),
                ruolo
        );

        return this.adminRepository.save(newAdmin);
    }

    //Get All
    public Page<Admin> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.adminRepository.findAll(pageable);
    }

    //Get By Id
    public Admin findById(UUID id) {
        return this.adminRepository.findById(id).orElseThrow(() -> new NotFound("Admin con id " + id + " non trovato!"));
    }

    //Update
    public Admin findByIdAndUpdate(UUID id, AdminDTO payload) {
        Admin found = this.findById(id);

        if (!found.getEmail().equals(payload.email())) {
            this.adminRepository.findByEmail(payload.email()).ifPresent(admin -> {
                throw new BadRequest("L'email " + payload.email() + " è già in uso!");
            });
        }

        Ruolo ruolo = this.ruoloService.findById(payload.ruoloId());

        found.setNome(payload.nome());
        found.setCognome(payload.cognome());
        found.setEmail(payload.email());
        found.setPassword(payload.password());
        found.setRuolo(ruolo);

        return this.adminRepository.save(found);
    }

    //Delete
    public void findByIdAndDelete(UUID id) {
        Admin found = this.findById(id);
        this.adminRepository.delete(found);
    }
}
