package vincenzomanfredi.capstone.sala.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vincenzomanfredi.capstone.museo.entities.Museo;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sala")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
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

    @ManyToOne
    @JoinColumn(name = "museo_id", nullable = false)
    private Museo museo;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by")
    private UUID createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(nullable = false)
    private int ordine;

    public Sala(Museo museo, String descrizione, String nome, int ordine) {
        this.museo = museo;
        this.descrizione = descrizione;
        this.nome = nome;
        this.ordine = ordine;
    }
}
