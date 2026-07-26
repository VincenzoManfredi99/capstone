package vincenzomanfredi.capstone.hotspot.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vincenzomanfredi.capstone.scena.entities.Scena;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Hotspot {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Column(nullable = false)
    private float pitch;

    @Column(nullable = false)
    private float yaw;

    @ManyToOne
    @JoinColumn(name = "target_scena_id")
    private Scena targetScena;

    @ManyToOne
    @JoinColumn(nullable = false, name = "scena_id")
    private Scena scena;

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

    public Hotspot(Tipo tipo, float pitch, float yaw, Scena targetScena, Scena scena) {
        this.tipo = tipo;
        this.pitch = pitch;
        this.yaw = yaw;
        this.targetScena = targetScena;
        this.scena = scena;
    }
}
