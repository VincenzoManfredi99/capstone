package vincenzomanfredi.capstone.exceptions;

import java.time.LocalDateTime;

public record ErrorsDTO(String message, LocalDateTime timestamp) {
}
