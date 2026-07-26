package vincenzomanfredi.capstone.scena.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vincenzomanfredi.capstone.sala.entities.Sala;

import java.time.LocalDateTime;
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
    private String foto360;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;

    public Scena(String foto360, Sala sala) {
        this.foto360 = foto360;
        this.sala = sala;
    }

}
