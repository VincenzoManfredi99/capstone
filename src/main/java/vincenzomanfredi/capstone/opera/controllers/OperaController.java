package vincenzomanfredi.capstone.opera.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.opera.entities.Opera;
import vincenzomanfredi.capstone.opera.payloads.OperaDTO;
import vincenzomanfredi.capstone.opera.payloads.OperaResponseDTO;
import vincenzomanfredi.capstone.opera.services.OperaService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/opere")
public class OperaController {

    private final OperaService operaService;

    public OperaController(OperaService operaService) {
        this.operaService = operaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OperaResponseDTO saveOpera(@RequestBody @Validated OperaDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        Opera saved = this.operaService.save(body);
        return new OperaResponseDTO(
                saved.getId()
        );
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
