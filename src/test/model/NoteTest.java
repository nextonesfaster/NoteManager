package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {
    private Note note;

    @BeforeEach
    public void setUp() {
        Folder folder = new Folder("initial folder");
        this.note = new Note("title", "This is just a simple note.\nHas a newline.", folder);
    }

    @Test
    public void testChangeFolder() {
        assertEquals("initial folder", this.note.getFolder().getName());

        Folder newFolder = new Folder("new folder");

        assertTrue(this.note.changeFolder(newFolder));
        assertEquals("new folder", this.note.getFolder().getName());
    }

    @Test
    public void testCharacterCount() {
        assertEquals(42, this.note.characterCount());
    }

    @Test
    public void testCharacterCountWithTitle() {
        assertEquals(47, this.note.characterCountWithTitle());
    }

    @Test
    public void testWordCount() {
        assertEquals(9, this.note.wordCount());
    }

    @Test
    public void testWordCountWithTitle() {
        assertEquals(10, this.note.wordCountWithTitle());
    }

    @Test
    public void testEdit() {
        String newText = "new note text!";
        this.note.edit(newText);
        assertEquals(newText, this.note.getText());
    }

    @Test
    public void testEditTitle() {
        String newTitle = "new note title!";
        this.note.editTitle(newTitle);
        assertEquals(newTitle, this.note.getTitle());
    }

    @Test
    public void testSearch() {
        assertTrue(this.note.search("title"));
        assertTrue(this.note.search("This is just a"));
    }

    @Test
    public void testSearchInTitle() {
        assertTrue(this.note.searchInTitle("ti"));
    }

    @Test
    public void testSearchInText() {
        assertTrue(this.note.searchInText("Has a newline."));
    }

    @Test
    public void testSearchInTextWithNullTitle() {
        Note note = new Note("Has a newline.");
        assertTrue(note.search("Has a newline."));
    }

    @Test
    public void testGetSummaryTitle() {
        assertEquals("title", this.note.getSummary());
    }

    @Test
    public void testGetSummaryTextLessThan20() {
        Note note = new Note("hello");
        assertEquals("hello", note.getSummary());
    }

    @Test
    public void testGetSummaryTextExactly20() {
        Note note = new Note("should be 20 chars!!");
        assertEquals("should be 20 chars!!", note.getSummary());
    }

    @Test
    public void testGetSummaryTextMoreThan20() {
        Note note = new Note("hello world, this is a long sentence");
        assertEquals("hello world, this is...", note.getSummary());
    }

    @Test
    public void testNoteWithPassword() {
        Folder folder = new Folder("folder");
        Note note = new Note("title", "text", "password", folder);
        assertEquals("title", note.getTitle());
        assertEquals("text", note.getText());
        assertTrue(note.isCorrectPassword("password"));
        assertEquals(folder, note.getFolder());
    }

    @Test
    public void testNoteWithoutTitle() {
        Folder folder = new Folder("folder");
        Note note = new Note("text", folder);
        assertNull(note.getTitle());
        assertEquals("text", note.getText());
        assertEquals(folder, note.getFolder());
    }

    @Test
    public void testGetDateTime() {
        LocalDateTime now = LocalDateTime.now();
        Note note = new Note("text");
        assertTrue(Duration.between(now, note.getDateTimeAdded()).toMillis() < 1000);
        assertTrue(Duration.between(now, note.getDateTimeModified()).toMillis() < 1000);
    }

    @Test
    public void testDisplayWithTitle() {
        String msg = "Title: title\n\nThis is just a simple note.\nHas a newline.\n\nWords: 9";
        assertEquals(msg, this.note.display());
    }

    @Test
    public void testDisplayWithoutTitle() {
        Note note = new Note("Just some text.");
        String msg = "Just some text.\n\nWords: 3";
        assertEquals(msg, note.display());
    }
}
