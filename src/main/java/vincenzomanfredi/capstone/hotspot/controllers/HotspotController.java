package vincenzomanfredi.capstone.hotspot.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzomanfredi.capstone.exceptions.BadRequest;
import vincenzomanfredi.capstone.hotspot.entities.Hotspot;
import vincenzomanfredi.capstone.hotspot.payloads.HotspotDTO;
import vincenzomanfredi.capstone.hotspot.payloads.HotspotResponseDTO;
import vincenzomanfredi.capstone.hotspot.services.HotspotService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hotspot")
public class HotspotController {
    private final HotspotService hotspotService;

    public HotspotController(HotspotService hotspotService) {
        this.hotspotService = hotspotService;
    }

    //Post
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HotspotResponseDTO saveHotspot(@RequestBody @Validated HotspotDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errosList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errosList);
        }
        Hotspot saved = this.hotspotService.save(body);
        return new HotspotResponseDTO(saved.getId());
    }

    //Get
    @GetMapping
    public Page<Hotspot> getHotspot(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String orderBy) {
        return this.hotspotService.getAll(page, size, orderBy);
    }

    //Get by Id
    @GetMapping("/{id}")
    public Hotspot getById(@PathVariable UUID id) {
        return this.hotspotService.findById(id);
    }

    //Put
    @PutMapping("/{id}")
    public Hotspot getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated HotspotDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.hotspotService.findByIdAndUpdate(id, body);
    }

    //Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.hotspotService.findByIdAndDelete(id);
    }
}

