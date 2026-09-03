package vincenzomanfredi.capstone.hotspot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vincenzomanfredi.capstone.hotspot.entities.Hotspot;

import java.util.List;
import java.util.UUID;

public interface HotspotRepository extends JpaRepository<Hotspot, UUID> {

    List<Hotspot> findByScenaId(UUID scenaId);
}
