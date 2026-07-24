package vincenzomanfredi.capstone.scena.entities;

import jakarta.persistence.*;
import lombok.*;
import vincenzomanfredi.capstone.sala.entities.Sala;

import java.util.UUID;

@Entity
@Table(name = "scena")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Scena {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String foto;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    public Scena(String foto, Sala sala) {
        this.foto = foto;
        this.sala = sala;
    }
}
