package model.exceptions;

// Exception thrown when a note doesn't have title set but it was supposed to
public class TitleNotSetException extends Exception {
    // EFFECTS: creates a new `TitleNotSetException`
    public TitleNotSetException(String exc) {
        super(exc);
    }
}
