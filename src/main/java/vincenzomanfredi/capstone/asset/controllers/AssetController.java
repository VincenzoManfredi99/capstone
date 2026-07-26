package vincenzomanfredi.capstone.asset.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzomanfredi.capstone.asset.entities.Asset;
import vincenzomanfredi.capstone.asset.payloads.AssetDTO;
import vincenzomanfredi.capstone.asset.payloads.AssetResponseDTO;
import vincenzomanfredi.capstone.asset.services.AssetService;
import vincenzomanfredi.capstone.exceptions.BadRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assets")
public class AssetController {
    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssetResponseDTO saveAsset(@RequestBody @Validated AssetDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione: " + errorsList);
        }
        Asset saved = this.assetService.save(body);
        return new AssetResponseDTO(
                saved.getId()
        );
    }

    @GetMapping
    public Page<Asset> getAssets(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String orderBy) {
        return this.assetService.getAll(page, size, orderBy);
    }

    @GetMapping("/{id}")
    public Asset getById(@PathVariable UUID id) {
        return this.assetService.findById(id);
    }

    @PutMapping("/{id}")
    public Asset getByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated AssetDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.assetService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.assetService.findByIdAndDelete(id);
    }
}
