package utils;

import org.mindrot.jbcrypt.BCrypt;

// A class to model a lockable.
//
// Objects are NOT locked by default.
public class Lockable {
    private boolean locked = false;
    private String passwordHash;

    // EFFECTS: locks the object if password is set
    public void lockIfPasswordSet() {
        if (this.passwordHash != null) {
            this.locked = true;
        }
    }

    // Locks the object with a password
    // MODIFIES: this
    // EFFECTS: locks the object with the provided password
    public void lock(String password) {
        this.setLocked(true);
        this.setSecurePassword(password);
    }

    // Updates the password of the object
    // MODIFIES: this
    // EFFECTS: updates the password for the object
    public void updatePassword(String newPassword) {
        // lock if not locked
        this.setLocked(true);
        this.setSecurePassword(newPassword);
    }

    // Checks if the provided password is the correct password
    // REQUIRES: password is set
    // EFFECTS: returns true if the provided password is the
    //          password set for this object, false otherwise
    public boolean isCorrectPassword(String password) {
        return BCrypt.checkpw(password, this.getPasswordHash());
    }

    // Generates a secure hash for a password
    // EFFECTS: returns a secure hash from provided password
    private String securePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Sets password after securing it
    // MODIFIES: this
    // EFFECTS: generates a secure hash for the password and then sets it
    public void setSecurePassword(String password) {
        this.setPasswordHash(this.securePassword(password));
    }

    // Unlocks the object if the password is correct
    // MODIFIES: this
    // EFFECTS: returns true and unlocks the object if the password is correct;
    //          returns false otherwise
    public boolean unlock(String password) {
        if (this.isCorrectPassword(password)) {
            this.setLocked(false);
            return true;
        } else {
            return false;
        }
    }

    /**
     * GETTERS AND SETTERS
     */
    public boolean isLocked() {
        return locked;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
