package vincenzomanfredi.capstone.asset.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vincenzomanfredi.capstone.asset.entities.Asset;
import vincenzomanfredi.capstone.asset.payloads.AssetDTO;
import vincenzomanfredi.capstone.asset.repositories.AssetRepository;
import vincenzomanfredi.capstone.exceptions.NotFound;
import vincenzomanfredi.capstone.opera.entities.Opera;
import vincenzomanfredi.capstone.opera.services.OperaService;

import java.util.UUID;

@Service
@Slf4j
public class AssetService {
    private final AssetRepository assetRepository;
    private final OperaService operaService;

    public AssetService(AssetRepository assetRepository, OperaService operaService) {
        this.assetRepository = assetRepository;
        this.operaService = operaService;
    }

    //Save
    public Asset save(AssetDTO payload) {
        Opera opera = this.operaService.findById(payload.operaId());

        Asset newAsset = new Asset(
                payload.urlFile(),
                opera,
                payload.tipoUrl()
        );

        Asset saved = this.assetRepository.save(newAsset);
        log.info("Asset con id " + saved.getId() + " salvato con successo!");
        return saved;
    }

    //Get All
    public Page<Asset> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.assetRepository.findAll(pageable);
    }

    //Find by id
    public Asset findById(UUID id) {
        return this.assetRepository.findById(id).orElseThrow(() -> new NotFound("L'asset con id " + id + " non è stato trovato!"));
    }


    //Update
    public Asset findByIdAndUpdate(UUID id, AssetDTO payload) {
        Asset found = this.findById(id);
        Opera opera = this.operaService.findById(payload.operaId());

        found.setUrlFile(payload.urlFile());
        found.setOpera(opera);
        found.setTipoUrl(payload.tipoUrl());

        Asset updated = this.assetRepository.save(found);
        log.info("Asset con id " + id + " aggiornato con successo!");
        return updated;
    }

    //Delete
    public void findByIdAndDelete(UUID id) {
        Asset found = this.findById(id);
        this.assetRepository.delete(found);
        log.info("Asset con id " + id + " eliminato con successo!");
    }

}
