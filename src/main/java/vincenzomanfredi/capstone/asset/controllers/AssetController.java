package vincenzomanfredi.capstone.asset.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vincenzomanfredi.capstone.asset.entities.Asset;
import vincenzomanfredi.capstone.asset.entities.TipoUrl;
import vincenzomanfredi.capstone.asset.payloads.AssetDTO;
import vincenzomanfredi.capstone.asset.payloads.AssetResponseDTO;
import vincenzomanfredi.capstone.asset.services.AssetService;
import vincenzomanfredi.capstone.exceptions.BadRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/opere") // Cambiato da /assets a /opere
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    // Ora corrisponde esattamente a: POST /opere/{operaId}/assets
    @PostMapping("/{operaId}/assets")
    @ResponseStatus(HttpStatus.CREATED)
    public AssetResponseDTO saveAsset(
            @PathVariable UUID operaId, // Prende l'id direttamente dall'URL
            @RequestParam("file") MultipartFile file,
            @RequestParam("tipoUrl") TipoUrl tipoUrl
    ) {
        Asset saved = this.assetService.saveWithFile(file, tipoUrl, operaId);
        return new AssetResponseDTO(saved.getId());
    }

    // Gli altri endpoint rimangono invariati o puoi mapparli coerentemente
    @GetMapping("/assets")
    public Page<Asset> getAssets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String orderBy
    ) {
        return this.assetService.getAll(page, size, orderBy);
    }

    @GetMapping("/assets/{id}")
    public Asset getById(@PathVariable UUID id) {
        return this.assetService.findById(id);
    }

    // Restituisce la lista di tutti gli asset associati a una specifica opera
    @GetMapping("/{operaId}/assets")
    public List<Asset> getAssetsByOpera(@PathVariable UUID operaId) {
        return this.assetService.getByOperaId(operaId);
    }

    @PutMapping("/assets/{id}")
    public Asset getByIdAndUpdate(
            @PathVariable UUID id,
            @RequestBody @Validated AssetDTO body,
            BindingResult validationResult
    ) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new BadRequest("Errori di validazione nella modifica: " + errorsList);
        }
        return this.assetService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/assets/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID id) {
        this.assetService.findByIdAndDelete(id);
    }
}