package vincenzomanfredi.capstone.sala.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SalaDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 3, max = 25, message = "Il nome deve restare tra i 3 ed i 25 caratteri")
        String nome,

        @NotBlank(message = "La descrizione è obbligatoria")
        @Size(min = 3, max = 300, message = "Il nome deve restare tra i 3 ed i 300 caratteri")
        String descrizione
) {
}
