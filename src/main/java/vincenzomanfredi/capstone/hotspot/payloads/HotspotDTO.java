package vincenzomanfredi.capstone.hotspot.payloads;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vincenzomanfredi.capstone.hotspot.entities.Tipo;

import java.util.UUID;

public record HotspotDTO(
        @NotNull(message = "Il tipo di hotspot è obbligatorio")
        Tipo tipo,

        @NotNull(message = "La coordinata pitch è obbligatoria")
        Float pitch,

        @NotNull(message = "La coordinata yaw è obbligatoria")
        Float yaw,

        @NotNull(message = "L'ID della scena di appartenenza è obbligatorio")
        UUID scenaId,

        @Size(max = 100, message = "Il titolo non può superare i 100 caratteri")
        String titolo,

        @Size(max = 1000, message = "La descrizione non può superare i 1000 caratteri")
        String descrizione,

        @Size(max = 255, message = "L'URL dell'immagine non può superare i 255 caratteri")
        String immagine,

        @Size(max = 255, message = "L'URL del file 3D non può superare i 255 caratteri")
        String file3D,

        @Size(max = 255, message = "L'URL dell'audio non può superare i 255 caratteri")
        String audio,

        UUID targetScenaId
) {
}
