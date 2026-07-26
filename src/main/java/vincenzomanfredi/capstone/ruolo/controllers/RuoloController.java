package vincenzomanfredi.capstone.ruolo.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.ruolo.entities.Ruolo;
import vincenzomanfredi.capstone.ruolo.payloads.RuoloDTO;
import vincenzomanfredi.capstone.ruolo.payloads.RuoloResponseDTO;
import vincenzomanfredi.capstone.ruolo.services.RuoloService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ruoli")
public class RuoloController {
    private final RuoloService ruoloService;

    public RuoloController(RuoloService ruoloService) {
        this.ruoloService = ruoloService;
    }

    //Post
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RuoloResponseDTO saveHotspot(@RequestBody @Validated RuoloDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errosList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errosList);
        }
        Ruolo saved = this.ruoloService.save(body);
        return new RuoloResponseDTO(saved.getId());
    }

    //Get
    @GetMapping
    public Page<Ruolo> getHotspot(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String orderBy) {
        return this.ruoloService.getAll(page, size, orderBy);
    }

    //Get by Id
    @GetMapping("/{id}")
    public Ruolo getById(@PathVariable UUID id) {
        return this.ruoloService.findById(id);
    }

    //Put
    @PutMapping("/{id}")
    public Ruolo getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated RuoloDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.ruoloService.findByIdAndUpdate(id, body);
    }

    //Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.ruoloService.deleteById(id);
    }
}
