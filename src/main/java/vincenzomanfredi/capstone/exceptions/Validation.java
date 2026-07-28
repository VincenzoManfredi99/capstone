package vincenzomanfredi.capstone.exceptions;

import java.util.List;

public class Validation extends RuntimeException {

    private List<String> errorsList;

    public Validation(List<String> errorsList) {
        super("Errori di validazione");
        this.errorsList = errorsList;
    }

    public Validation(String message) {
        super(message);
        this.errorsList = List.of(message);
    }

    public List<String> getErrorsList() {
        return errorsList;
    }
}
