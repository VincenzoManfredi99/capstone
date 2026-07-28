package vincenzomanfredi.capstone.asset.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vincenzomanfredi.capstone.opera.entities.Opera;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assets")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Asset {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "url_file")
    private String urlFile;

    @ManyToOne
    @JoinColumn(nullable = false, name = "opera_id")
    private Opera opera;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_url", nullable = false)
    private TipoUrl tipoUrl;

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

    public Asset(String urlFile, Opera opera, TipoUrl tipoUrl) {
        this.urlFile = urlFile;
        this.opera = opera;
        this.tipoUrl = tipoUrl;
    }
}
