package vincenzomanfredi.capstone.museo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vincenzomanfredi.capstone.museo.entities.Museo;

import java.util.UUID;

@Repository
public interface MuseoRepository extends JpaRepository<Museo, UUID> {
}
