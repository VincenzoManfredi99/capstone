package vincenzomanfredi.capstone.scena.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ScenaDTO(
        @NotBlank(message = "Il percorso  della foto è obbligatorio")
        String foto360,

        @NotNull(message = "L'ID della sala di appartenenza è obbligatorio")
        UUID salaId
) {
}
