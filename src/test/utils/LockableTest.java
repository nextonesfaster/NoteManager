package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LockableTest {
    private Lockable lockable;

    @BeforeEach
    public void setUp() {
        this.lockable = new Lockable();
    }

    @Test
    public void testLock() {
        assertFalse(this.lockable.isLocked());
        this.lockable.lock("password");
        assertTrue(this.lockable.isLocked());
        assertTrue(this.lockable.isCorrectPassword("password"));
    }

    @Test
    public void testIsCorrectPassword() {
        this.lockable.updatePassword("password");
        assertTrue(this.lockable.isCorrectPassword("password"));
        assertFalse(this.lockable.isCorrectPassword("wrong password"));
    }

    @Test
    public void testUnlockCorrectPassword() {
        this.lockable.updatePassword("password");
        assertTrue(this.lockable.unlock("password"));
        assertFalse(this.lockable.isLocked());
    }

    @Test
    public void testUnlockIncorrectPassword() {
        this.lockable.updatePassword("password");
        assertFalse(this.lockable.unlock("wrong password"));
        assertTrue(this.lockable.isLocked());
    }

    @Test
    public void testLockIfPasswordSet() {
        this.lockable.lockIfPasswordSet();
        assertFalse(this.lockable.isLocked());
        this.lockable.setSecurePassword("password");
        this.lockable.lockIfPasswordSet();
        assertTrue(this.lockable.isLocked());
    }

    @Test
    public void testUpdatePassword() {
        this.lockable.updatePassword("password");
        assertTrue(this.lockable.isLocked());
        assertTrue(this.lockable.isCorrectPassword("password"));
    }

    @Test
    public void testSetSecurePassword() {
        this.lockable.setSecurePassword("password");
        assertTrue(this.lockable.isCorrectPassword("password"));
    }
}
