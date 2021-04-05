package model.exceptions;

// Exception thrown when a model is locked but was supposed to be unlocked.
public class LockedException extends Exception {
    // EFFECTS: creates a new `LockedException`
    public LockedException(String exc) {
        super(exc);
    }
}
