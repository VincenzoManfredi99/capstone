package vincenzomanfredi.capstone.scena.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ScenaDTO(
        @NotBlank(message = "Il percorso o il nome della foto è obbligatorio")
        @Size(max = 255, message = "Il percorso della foto non può superare i 255 caratteri")
        String foto,

        @NotNull(message = "L'ID della sala di appartenenza è obbligatorio")
        UUID salaId
) {
}
