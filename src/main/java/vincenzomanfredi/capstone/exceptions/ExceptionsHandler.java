package vincenzomanfredi.capstone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

public class ExceptionsHandler {

    //Error 400
    @ExceptionHandler(BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsPayload handleBadRequest(BadRequest ex) {
        return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
    }

    //Error 404
    @ExceptionHandler(NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsPayload handlNotFound(NotFound ex) {
        return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
    }

    //Gestione errori generici
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsPayload handleGenericError(Exception ex) {
        ex.printStackTrace();
        return new ErrorsPayload("Errore interno del server! Riprova più tardi", LocalDateTime.now());
    }
}
