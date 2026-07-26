package vincenzomanfredi.capstone.opera.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vincenzomanfredi.capstone.hotspot.entities.Hotspot;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "opere")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Opera {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false)
    private String descrizione;

    private String url_audio;

    @ManyToOne
    @JoinColumn(nullable = false, name = "hotspot_id")
    private Hotspot hotspot;

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

    public Opera(String titolo, String descrizione, String url_audio, Hotspot hotspot) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.url_audio = url_audio;
        this.hotspot = hotspot;
    }
}
