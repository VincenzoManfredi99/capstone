package vincenzomanfredi.capstone.hotspot.entities;

import jakarta.persistence.*;
import lombok.*;
import vincenzomanfredi.capstone.scena.entities.Scena;

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
    @JoinColumn(nullable = false, name = "scena_id")
    private Scena scena;

    private String titolo;

    private String descrizione;

    @Column(name = "url_immagine")
    private String immagine;

    @Column(name = "url_file3d")
    private String file3D;

    @Column(name = "url_audio")
    private String audio;

    @ManyToOne
    @JoinColumn(name = "target_scena_id")
    private Scena targetScenaId;

    public Hotspot(Tipo tipo, float pitch, float yaw, Scena scena, String titolo, String descrizione, String immagine, String file3D, String audio, Scena targetScenaId) {
        this.tipo = tipo;
        this.pitch = pitch;
        this.yaw = yaw;
        this.scena = scena;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.immagine = immagine;
        this.file3D = file3D;
        this.audio = audio;
        this.targetScenaId = targetScenaId;
    }
}
