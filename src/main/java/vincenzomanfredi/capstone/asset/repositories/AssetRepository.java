package vincenzomanfredi.capstone.asset.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vincenzomanfredi.capstone.asset.entities.Asset;

import java.util.List;
import java.util.UUID;

public interface AssetRepository extends JpaRepository<Asset, UUID> {
    List<Asset> findByOperaId(UUID operaId);
}
