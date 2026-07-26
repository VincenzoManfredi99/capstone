package vincenzomanfredi.capstone.opera.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OperaDTO(
        @NotBlank(message = "Il titolo dell'opera è obbligatorio")
        String titolo,

        @NotBlank(message = "La descrizione dell'opera è obbligatoria")
        String descrizione,

        String url_audio,

        @NotNull(message = "L'ID dell'hotspot associato è obbligatorio")
        UUID hotspotId
) {
}
