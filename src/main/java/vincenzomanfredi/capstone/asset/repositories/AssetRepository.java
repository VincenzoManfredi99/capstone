package vincenzomanfredi.capstone.asset.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vincenzomanfredi.capstone.asset.entities.Asset;

import java.util.UUID;

public interface AssetRepository extends JpaRepository<Asset, UUID> {
}
