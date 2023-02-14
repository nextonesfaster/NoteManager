package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.SortableSet;

import static org.junit.jupiter.api.Assertions.*;

public class FoldersTest {
    private Folders folders;

    @BeforeEach
    public void setUp() {
        this.folders = new Folders();
    }

    @Test
    public void testAddNotPresent() {
        Folder folder = new Folder("name");
        assertTrue(this.folders.add(folder));
    }

    @Test
    public void testAddAlreadyPresent() {
        Folder folder = new Folder("name");
        SortableSet<Folder> folders = new SortableSet<>();
        folders.add(folder);
        this.folders.setFolders(folders);

        assertFalse(this.folders.add(folder));
    }

    @Test
    public void testRemove() {
        Folder folder = new Folder("name");
        this.folders.add(folder);
        assertTrue(this.folders.remove(folder));
        Folder anotherFolder = new Folder("name");
        assertFalse(this.folders.remove(anotherFolder));
    }

    @Test
    public void testGetFolders() {
        Folder folder = new Folder("name");
        this.folders.add(folder);
        SortableSet<Folder> folders = new SortableSet<>();
        folders.add(folder);
        assertEquals(folders, this.folders.getFolders());
    }

    @Test
    public void testDisplay() {
        Folder folderOne = new Folder("one");
        Folder folderTwo = new Folder("two");
        this.folders.add(folderOne);
        this.folders.add(folderTwo);

        assertEquals("Folders:\n- [1] one\n- [2] two", this.folders.display());
    }
}
