package vincenzomanfredi.capstone.opera.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.opera.entities.Opera;
import vincenzomanfredi.capstone.opera.payloads.OperaDTO;
import vincenzomanfredi.capstone.opera.payloads.OperaResponseDTO;
import vincenzomanfredi.capstone.opera.services.OperaService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/opere")
public class OperaController {

    private final OperaService operaService;

    public OperaController(OperaService operaService) {
        this.operaService = operaService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public OperaResponseDTO saveOpera(
            @RequestParam("titolo") String titolo,
            @RequestParam("descrizione") String descrizione,
            @RequestParam("hotspotId") UUID hotspotId,
            @RequestParam(value = "audioFile", required = false) MultipartFile audioFile
    ) throws IOException {

        Opera saved;
        if (audioFile != null && !audioFile.isEmpty()) {
            // Se l'utente ha caricato un file audio, usa il metodo con Cloudinary
            saved = this.operaService.saveOperaConFile(audioFile, titolo, descrizione, hotspotId);
        } else {
            // Altrimenti salva senza file audio
            OperaDTO dto = new OperaDTO(titolo, descrizione, null, hotspotId);
            saved = this.operaService.save(dto);
        }

        return new OperaResponseDTO(saved.getId());
    }

    @GetMapping
    public Page<Opera> getOpere(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String orderBy) {
        return this.operaService.getAll(page, size, orderBy);
    }

    @GetMapping("/{id}")
    public Opera getById(@PathVariable UUID id) {
        return this.operaService.findById(id);
    }

    @PutMapping("/{id}")
    public Opera getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated OperaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.operaService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.operaService.findByIdAndDelete(id);
    }
}
