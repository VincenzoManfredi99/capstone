package vincenzomanfredi.capstone.sala.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "sala")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Sala {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descrizione;

    public Sala(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }
}
