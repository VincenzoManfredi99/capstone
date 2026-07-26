package vincenzomanfredi.capstone.ruolo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vincenzomanfredi.capstone.ruolo.entities.Ruolo;

import java.util.UUID;

@Repository
public interface RuoloRepository extends JpaRepository<Ruolo, UUID> {
    boolean existsByDescrizione(String descrizione);
}
