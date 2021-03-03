package persistence;

import model.Folder;
import model.Folders;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// This class is modelled after the `JsonWriterTest` class present here:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/test/persistence/JsonWriterTest.java
public class JsonWriterTest extends JsonTest {
    @Test
    public void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyFolders() {
        try {
            Folders folders = new Folders();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyFolders.json");
            writer.open();
            writer.write(folders);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyFolders.json");
            folders = reader.read();
            assertEquals(0, folders.getFolders().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterNonEmptyFolders() {
        try {
            Folders folders = new Folders();
            Folder folderOne = new Folder("Folder 1");
            this.addFolderNotes(folderOne);
            Folder folderTwo = new Folder("Folder 2");
            this.addFolderNotes(folderTwo);
            folderTwo.getNotes().get(1).setTitle("Note 2");
            folderTwo.lock("folder password");
            Folder folderThree = new Folder("Folder 3");
            folders.add(folderOne);
            folders.add(folderTwo);
            folders.add(folderThree);

            JsonWriter writer = new JsonWriter("./data/testWriterNonEmptyFolders.json");
            writer.open();
            writer.write(folders);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterNonEmptyFolders.json");
            folders = reader.read();
            this.checkFolders(folders.getFolders());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
