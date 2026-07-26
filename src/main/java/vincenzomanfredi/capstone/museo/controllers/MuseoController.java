package vincenzomanfredi.capstone.museo.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.museo.entities.Museo;
import vincenzomanfredi.capstone.museo.payloads.MuseoDTO;
import vincenzomanfredi.capstone.museo.services.MuseoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/musei")
public class MuseoController {

    private final MuseoService museoService;

    public MuseoController(MuseoService museoService) {
        this.museoService = museoService;
    }

    //Post
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Museo saveMuseo(@RequestBody @Validated MuseoDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        return this.museoService.save(body);
    }

    //Get All
    @GetMapping
    public Page<Museo> getMusei(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String orderBy) {
        return this.museoService.getAll(page, size, orderBy);
    }

    //Get by id
    @GetMapping("/{id}")
    public Museo getById(@PathVariable UUID id) {
        return this.museoService.findById(id);
    }

    //Update
    @PutMapping("/{id}")
    public Museo getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated MuseoDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.museoService.findByIdAndUpdate(id, body);
    }

    //Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.museoService.findByIdAndDelete(id);
    }
}
