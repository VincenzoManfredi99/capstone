package vincenzomanfredi.capstone.exceptions;

import java.time.LocalDateTime;

public record ErrorsPayload(
        String message,
        LocalDateTime timestamp
) {
}
