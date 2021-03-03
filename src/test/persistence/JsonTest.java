package persistence;

import model.Folder;
import model.Note;
import utils.Lockable;
import utils.SortableSet;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {
    void checkFolders(SortableSet<Folder> folders) {
        Folder folderOne = new Folder("Folder 1");
        this.addFolderNotes(folderOne);
        Folder folderTwo = new Folder("Folder 2");
        this.addFolderNotes(folderTwo);
        folderTwo.getNotes().get(1).setTitle("Note 2");
        folderTwo.lock("folder password");
        Folder folderThree = new Folder("Folder 3");

        this.checkFolder(folders.get(0), folderOne);
        this.checkFolder(folders.get(1), folderTwo);
        this.checkFolder(folders.get(2), folderThree);
    }

    void addFolderNotes(Folder folder) {
        Note noteOne = new Note("Note 1", "Sample Text", folder);
        this.setNoteDateTime(noteOne);
        Note noteTwo = new Note("Sample Text Two", folder);
        noteTwo.lock("password");
        this.setNoteDateTime(noteTwo);
    }

    private void checkFolder(Folder expected, Folder actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getNotes().size(), actual.getNotes().size());
        for (int i = 0; i < expected.getNotes().size(); i++) {
            this.checkNote(expected.getNotes().get(i), actual.getNotes().get(i));
        }
        this.checkLockable(expected, actual, "folder password");
    }

    private void checkNote(Note expected, Note actual) {
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getDateTimeAdded(), actual.getDateTimeAdded());
        assertEquals(expected.getDateTimeModified(), actual.getDateTimeModified());
        this.checkLockable(expected, actual, "password");
    }

    private void checkLockable(Lockable expected, Lockable actual, String actualPassword) {
        assertEquals(expected.isLocked(), actual.isLocked());
        if (actual.getPasswordHash() == null) {
            assertNull(expected.getPasswordHash());
        } else {
            assertTrue(expected.isCorrectPassword(actualPassword));
        }
    }

    private void setNoteDateTime(Note note) {
        note.setDateTimeAdded(LocalDateTime.parse("2021-03-04T03:02:16.320"));
        note.setDateTimeModified(LocalDateTime.parse("2021-03-04T03:12:26.247"));
    }
}
