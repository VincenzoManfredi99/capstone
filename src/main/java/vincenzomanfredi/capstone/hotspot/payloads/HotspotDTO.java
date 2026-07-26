package vincenzomanfredi.capstone.hotspot.payloads;

import jakarta.validation.constraints.NotNull;
import vincenzomanfredi.capstone.hotspot.entities.Tipo;

import java.util.UUID;

public record HotspotDTO(
        @NotNull(message = "Il tipo di hotspot è obbligatorio")
        Tipo tipo,

        @NotNull(message = "Il valore del pitch è obbligatorio")
        float pitch,

        @NotNull(message = "Il valore dello yaw è obbligatorio")
        float yaw,

        UUID targetScenaId,

        @NotNull(message = "L'ID della scena di appartenenza è obbligatorio")
        UUID scenaId
) {
}
