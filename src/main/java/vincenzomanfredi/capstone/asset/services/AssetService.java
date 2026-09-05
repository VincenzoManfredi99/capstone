package vincenzomanfredi.capstone.asset.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vincenzomanfredi.capstone.asset.entities.Asset;
import vincenzomanfredi.capstone.asset.entities.TipoUrl;
import vincenzomanfredi.capstone.asset.payloads.AssetDTO;
import vincenzomanfredi.capstone.asset.repositories.AssetRepository;
import vincenzomanfredi.capstone.exceptions.NotFound;
import vincenzomanfredi.capstone.opera.entities.Opera;
import vincenzomanfredi.capstone.opera.services.OperaService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class AssetService {
    private final AssetRepository assetRepository;
    private final OperaService operaService;
    private final Cloudinary cloudinary; // <-- 1. Aggiungi Cloudinary qui

    public AssetService(AssetRepository assetRepository, OperaService operaService, Cloudinary cloudinary) {
        this.assetRepository = assetRepository;
        this.operaService = operaService;
        this.cloudinary = cloudinary;
    }

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

    public Asset saveWithFile(MultipartFile file, TipoUrl tipoUrl, UUID operaId) {
        Opera opera = this.operaService.findById(operaId);

        String urlFile;
        try {

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));

            urlFile = uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento del file su Cloudinary", e);
        }

        Asset newAsset = new Asset(urlFile, opera, tipoUrl);
        Asset saved = this.assetRepository.save(newAsset);

        log.info("Asset con Cloudinary e id " + saved.getId() + " salvato con successo!");
        return saved;
    }


    public Page<Asset> getAll(int page, int size, String orderBy) {
        if (size > 50) size = 50;
        if (size < 0) size = 10;
        if (page < 0) page = 0;

        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.assetRepository.findAll(pageable);
    }

    public List<Asset> getByOperaId(UUID operaId) {
        return this.assetRepository.findByOperaId(operaId);
    }

    public Asset findById(UUID id) {
        return this.assetRepository.findById(id).orElseThrow(() -> new NotFound("L'asset con id " + id + " non è stato trovato!"));
    }

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

    public void findByIdAndDelete(UUID id) {
        Asset found = this.findById(id);
        this.assetRepository.delete(found);
        log.info("Asset con id " + id + " eliminato con successo!");
    }
}