package vincenzomanfredi.capstone.ruolo.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RuoloDTO(
        @NotBlank(message = "La descrizione del ruolo è obbligatoria")
        @Size(min = 2, max = 50, message = "La descrizione deve essere compresa tra 2 e 50 caratteri")
        String descrizione
) {
}
