package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.SortOption;
import utils.SortableSet;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FolderTest {
    private Folder folder;

    @BeforeEach
    public void setUp() {
        this.folder = new Folder("folder-name");
    }

    @Test
    public void testGetNote() {
        assertEquals("folder-name", this.folder.getName());
    }

    @Test
    public void testAddNote() {
        assertEquals(0, this.folder.totalNotes());

        Note note = new Note("abstraction 1");
        this.folder.addNote(note);
        assertEquals(1, this.folder.totalNotes());
        assertEquals(note, this.folder.getNotes().get(0));
    }

    @Test
    public void testRemoveNotePresent() {
        Note note = new Note("abstraction 1");
        this.folder.addNote(note);

        assertEquals(1, this.folder.totalNotes());

        assertTrue(this.folder.removeNote(note));

        assertEquals(0, this.folder.totalNotes());
    }

    @Test
    public void testRemoveNoteNotPresent() {
        Note note = new Note("abstraction 1");
        assertFalse(this.folder.removeNote(note));
    }

    @Test
    public void testFolderWithPassword() {
        Folder folder = new Folder("locked folder");
        folder.lock("super secret password");
        assertTrue(folder.isLocked());
        assertTrue(folder.isCorrectPassword("super secret password"));
    }

    @Test
    public void testFolderWithoutPassword() {
        assertFalse(this.folder.isLocked());
    }

    @Test
    public void testSearchFound() {
        new Note("the quick brown fox", this.folder);
        new Note("title", "jumps over the lazy dog", "password", this.folder);
        Note note = new Note("jumps over the lazy dog", this.folder);

        Optional<Note> result = this.folder.search("over the lazy");
        assertTrue(result.isPresent());
        assertEquals(note, result.get());
    }

    @Test
    public void testSearchNotFound() {
        new Note("the quick brown fox", this.folder);
        assertFalse(this.folder.search("dog").isPresent());
    }

    @Test
    public void testSortNewestAddedFirst() {
        this.createSortNotes();
        SortableSet<Note> expectedOrder = new SortableSet<>();
        expectedOrder.add(this.getNote(2));
        expectedOrder.add(this.getNote(1));
        expectedOrder.add(this.getNote(0));

        assertEquals(expectedOrder, this.folder.sort(SortOption.NEWEST_ADDED_FIRST));
        this.checkSortOption(SortOption.NEWEST_ADDED_FIRST);
    }

    @Test
    public void testSortOldestAddedFirst() {
        this.createSortNotes();
        SortableSet<Note> expectedOrder = new SortableSet<>();
        expectedOrder.add(this.getNote(0));
        expectedOrder.add(this.getNote(1));
        expectedOrder.add(this.getNote(2));

        assertEquals(expectedOrder, this.folder.sort(SortOption.OLDEST_ADDED_FIRST));
        this.checkSortOption(SortOption.OLDEST_ADDED_FIRST);
    }

    @Test
    public void testSortNewestModifiedFirst() {
        this.createSortNotes();
        this.modifySortNotes();
        SortableSet<Note> expectedOrder = new SortableSet<>();
        expectedOrder.add(this.getNote(2));
        expectedOrder.add(this.getNote(0));
        expectedOrder.add(this.getNote(1));

        assertEquals(expectedOrder, this.folder.sort(SortOption.NEWEST_MODIFIED_FIRST));
        this.checkSortOption(SortOption.NEWEST_MODIFIED_FIRST);
    }

    @Test
    public void testSortOldestModifiedFirst() {
        this.createSortNotes();
        this.modifySortNotes();
        SortableSet<Note> expectedOrder = new SortableSet<>();
        expectedOrder.add(this.getNote(1));
        expectedOrder.add(this.getNote(0));
        expectedOrder.add(this.getNote(2));

        assertEquals(expectedOrder, this.folder.sort(SortOption.OLDEST_MODIFIED_FIRST));
        this.checkSortOption(SortOption.OLDEST_MODIFIED_FIRST);
    }

    @Test
    public void testSortNull() {
        assertNull(this.folder.sort(null));
    }

    @Test
    public void testTotalNotes() {
        assertEquals(0, this.folder.totalNotes());
        new Note("one", this.folder);
        new Note("two", this.folder);
        assertEquals(2, this.folder.totalNotes());
        assertEquals(this.folder.getNotes().size(), this.folder.totalNotes());
    }

    @SuppressWarnings("SameParameterValue")
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createSortNotes() {
        Note one = new Note("note one");
        this.sleep(100);
        Note two = new Note("note two");
        this.sleep(100);
        Note three = new Note("note three");

        SortableSet<Note> notes = new SortableSet<>();
        notes.add(one);
        notes.add(two);
        notes.add(three);
        this.folder.setNotes(notes);
    }

    private Note getNote(int index) {
        return this.folder.getNotes().get(index);
    }

    private void modifySortNotes() {
        this.getNote(1).edit("modified first");
        this.sleep(100);
        this.getNote(0).edit("modified second");
        this.sleep(100);
        this.getNote(2).edit("modified third");
    }

    private void checkSortOption(SortOption sortOption) {
        assertEquals(sortOption, this.folder.getSortOption());
    }

    @Test
    public void testDisplayNotes() {
        Note noteOne = new Note("one");
        Note noteTwo = new Note("two");
        this.folder.addNote(noteOne);
        this.folder.addNote(noteTwo);

        assertEquals("Notes:\n- [1] one\n- [2] two", this.folder.displayNotes());
    }

    @Test
    public void testToString() {
        assertEquals("folder-name", this.folder.toString());
    }
}
