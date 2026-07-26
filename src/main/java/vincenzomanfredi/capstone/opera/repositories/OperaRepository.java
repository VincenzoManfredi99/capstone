package vincenzomanfredi.capstone.opera.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vincenzomanfredi.capstone.opera.entities.Opera;

import java.util.UUID;

public interface OperaRepository extends JpaRepository<Opera, UUID> {
}
