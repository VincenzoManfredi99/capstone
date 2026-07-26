package vincenzomanfredi.capstone.museo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vincenzomanfredi.capstone.admin.entities.Admin;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "musei")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Museo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String denominazione;

    @Column(nullable = false)
    private String indirizzo;

    @Column(nullable = false)
    private String citta;

    @Column(nullable = false)
    private String provincia;

    @Column(nullable = false)
    private int cap;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Admin utente;

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

    @Column(name = "accesso_museo")
    private String accessoMuseo;

    public Museo(String denominazione, String indirizzo, String citta, String provincia, int cap, Admin utente, String accessoMuseo) {
        this.denominazione = denominazione;
        this.indirizzo = indirizzo;
        this.citta = citta;
        this.provincia = provincia;
        this.cap = cap;
        this.utente = utente;
        this.accessoMuseo = accessoMuseo;
    }
}
