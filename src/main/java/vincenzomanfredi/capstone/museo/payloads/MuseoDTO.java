package vincenzomanfredi.capstone.museo.payloads;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record MuseoDTO(
        @NotBlank(message = "La denominazione del museo è obbligatoria")
        @Size(min = 2, max = 100, message = "La denominazione deve essere compresa tra 2 e 100 caratteri")
        String denominazione,

        @NotBlank(message = "L'indirizzo è obbligatorio")
        String indirizzo,

        @NotBlank(message = "La città è obbligatoria")
        String citta,

        @NotBlank(message = "La provincia è obbligatoria")
        @Size(min = 2, max = 2, message = "La provincia deve essere composta da 2 caratteri (es. MI, RM)")
        String provincia,

        @NotNull(message = "Il CAP è obbligatorio")
        @Min(value = 1000, message = "Il CAP non è valido")
        @Max(value = 99999, message = "Il CAP non è valido")
        Integer cap,

        @NotNull(message = "L'ID dell'utente/admin creatore è obbligatorio")
        UUID utenteId,

        String accessoMuseo
) {
}
