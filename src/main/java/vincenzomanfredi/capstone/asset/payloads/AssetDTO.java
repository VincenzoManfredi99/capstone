package vincenzomanfredi.capstone.asset.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vincenzomanfredi.capstone.asset.entities.TipoUrl;

import java.util.UUID;

public record AssetDTO(
        @NotBlank(message = "L'URL del file è obbligatorio")
        String urlFile,
 
        @NotNull(message = "L'ID dell'opera associata è obbligatorio")
        UUID operaId,

        @NotNull(message = "Il tipo di URL/asset è obbligatorio")
        TipoUrl tipoUrl
) {
}
