package vincenzomanfredi.capstone.scena.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vincenzomanfredi.capstone.scena.entities.Scena;

import java.util.UUID;

public interface ScenaRepository extends JpaRepository<Scena, UUID> {
}
