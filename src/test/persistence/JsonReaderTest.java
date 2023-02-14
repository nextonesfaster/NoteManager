package persistence;

import model.Folder;
import model.Folders;
import utils.SortableSet;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// This class is modelled after the `JsonReaderTest` class present here:
class JsonReaderTest extends JsonTest {
    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyFolders.json");
        try {
            Folders folders = reader.read();
            assertEquals(0, folders.getFolders().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderNonEmptyFolders.json");
        try {
            Folders folders = reader.read();
            SortableSet<Folder> listOfFolders = folders.getFolders();
            assertEquals(3, listOfFolders.size());
            this.checkFolders(listOfFolders);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}