package vincenzomanfredi.capstone.ruolo.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "ruoli")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String descrizione;

    public Ruolo(String descrizione) {
        this.descrizione = descrizione;
    }
}