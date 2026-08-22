package vincenzomanfredi.capstone.utente.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.utente.entities.Utente;
import vincenzomanfredi.capstone.utente.payloads.UtenteDTO;
import vincenzomanfredi.capstone.utente.services.UtenteService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    //Post
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Utente saveAdmin(@RequestBody @Validated UtenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.utenteService.save(body);
    }

    // Get All
    @GetMapping
    public Page<Utente> getAdmins(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String orderBy) {
        return this.utenteService.getAll(page, size, orderBy);
    }

    // Get by Id
    @GetMapping("/{id}")
    public Utente getById(@PathVariable UUID id) {
        return this.utenteService.findById(id);
    }

    // Put
    @PutMapping("/{id}")
    public Utente getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated UtenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.utenteService.findByIdAndUpdate(id, body);
    }

    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.utenteService.findByIdAndDelete(id);
    }
}
