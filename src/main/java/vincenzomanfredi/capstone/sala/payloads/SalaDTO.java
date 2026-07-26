package vincenzomanfredi.capstone.sala.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record SalaDTO(
        @NotBlank(message = "Il nome della sala è obbligatorio")
        @Size(min = 2, max = 100, message = "Il nome deve essere compreso tra 2 e 100 caratteri")
        String nome,

        @NotBlank(message = "La descrizione è obbligatoria")
        String descrizione,

        @NotNull(message = "L'ID del museo è obbligatorio")
        UUID museoId,

        @NotNull(message = "L'ordine è obbligatorio")
        int ordine
) {
}
