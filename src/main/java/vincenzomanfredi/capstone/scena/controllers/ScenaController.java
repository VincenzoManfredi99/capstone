package vincenzomanfredi.capstone.scena.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.scena.entities.Scena;
import vincenzomanfredi.capstone.scena.payloads.ScenaDTO;
import vincenzomanfredi.capstone.scena.payloads.ScenaResponseDTO;
import vincenzomanfredi.capstone.scena.services.ScenaService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/scene")
public class ScenaController {
    private final ScenaService scenaService;

    public ScenaController(ScenaService scenaService) {
        this.scenaService = scenaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScenaResponseDTO saveScena(
            @RequestParam("foto360") MultipartFile file,
            @RequestParam("salaId") UUID salaId
    ) throws IOException {
        Scena savedScena = this.scenaService.saveScenaConFile(file, salaId);
        return new ScenaResponseDTO(savedScena.getId());
    }

    // Get (Tutte le scene)
    @GetMapping
    public Page<Scena> getScena(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String orderBy) {
        return this.scenaService.getAll(page, size, orderBy);
    }

    // Get by Id
    @GetMapping("/{id}")
    public Scena getById(@PathVariable UUID id) {
        return this.scenaService.findById(id);
    }

    // Put (Modifica)
    @PutMapping("/{id}")
    public Scena getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated ScenaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.scenaService.findByIdAndUpdate(id, body);
    }

    // Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.scenaService.findByIdAndDelete(id);
    }
}