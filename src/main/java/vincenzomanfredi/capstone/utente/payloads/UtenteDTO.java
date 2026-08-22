package vincenzomanfredi.capstone.utente.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UtenteDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 3, max = 25, message = "Il nome deve restare tra i 3 ed i 25 caratteri")
        String nome,

        @NotBlank(message = "Il cognome è obbligatorio")
        @Size(min = 3, max = 25, message = "Il cognome deve restare tra i 3 ed i 25 caratteri")
        String cognome,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Formato email non valido")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 6, message = "La password deve essere di almeno 6 caratteri")
        String password
) {
}
