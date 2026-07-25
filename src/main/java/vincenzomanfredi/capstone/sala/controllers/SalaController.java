package vincenzomanfredi.capstone.sala.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.sala.entities.Sala;
import vincenzomanfredi.capstone.sala.payloads.SalaDTO;
import vincenzomanfredi.capstone.sala.payloads.SalaResponseDTO;
import vincenzomanfredi.capstone.sala.services.SalaService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sala")
public class SalaController {
    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    //Post
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SalaResponseDTO saveSala(@RequestBody @Validated SalaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errosList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errosList);
        }
        Sala saved = this.salaService.save(body);
        return new SalaResponseDTO(saved.getId());
    }

    //Get
    @GetMapping
    public Page<Sala> getSala(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id") String orderBy) {
        return this.salaService.getAll(page, size, orderBy);
    }

    //Get by Id
    @GetMapping("/{id}")
    public Sala getById(@PathVariable UUID id) {
        return this.salaService.findById(id);
    }

    //Put
    @PutMapping("/{id}")
    public Sala getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated SalaDTO body, BindingResult validtionResult) {
        if (validtionResult.hasErrors()) {
            List<String> errorsList = validtionResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.salaService.findByIdAndUpdate(id, body);
    }

    //Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.salaService.findByIdAndDelete(id);
    }
}
