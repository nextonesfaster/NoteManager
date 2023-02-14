package persistence;

import model.Folder;
import model.Folders;
import model.Note;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Lockable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads folders from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Folders read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return this.parseFolders(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses folders from JSON object and returns it
    private Folders parseFolders(JSONObject jsonObject) {
        Folders folders = new Folders();
        this.addFolders(folders, jsonObject);
        return folders;
    }

    // MODIFIES: folders
    // EFFECTS: parses folder from JSON object and adds it to folders
    private void addFolders(Folders folders, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("folders");
        for (Object json : jsonArray) {
            JSONObject nextFolder = (JSONObject) json;
            addFolder(folders, nextFolder);
        }
    }

    // MODIFIES: folders
    // EFFECTS: parses folder from JSON object and adds it to folders
    private void addFolder(Folders folders, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Folder folder = new Folder(name);
        JSONArray jsonArray = jsonObject.getJSONArray("notes");
        for (Object json : jsonArray) {
            JSONObject nextNote = (JSONObject) json;
            addNote(folder, nextNote);
        }
        this.setLockableFields(folder, jsonObject);
        folders.add(folder);
    }

    // MODIFIES: folder
    // EFFECTS: parses note from JSON object and adds it to the folder
    private void addNote(Folder folder, JSONObject jsonObject) {
        String text = jsonObject.getString("text");
        String title = jsonObject.optString("title", null);
        String dateTimeAdded = jsonObject.getString("dateTimeAdded");
        String dateTimeModified = jsonObject.getString("dateTimeModified");

        Note note = new Note(text, folder);
        note.setDateTimeAdded(LocalDateTime.parse(dateTimeAdded));
        note.setDateTimeModified(LocalDateTime.parse(dateTimeModified));
        if (title != null) {
            note.setTitle(title);
        }
        this.setLockableFields(note, jsonObject);
    }

    // MODIFIES: this
    // EFFECTS: parses lockable details from JSON object and sets the appropriate
    // fields
    private void setLockableFields(Lockable lockable, JSONObject jsonObject) {
        lockable.setLocked(jsonObject.getBoolean("locked"));

        String passwordHash = jsonObject.optString("passwordHash", null);
        if (passwordHash != null) {
            lockable.setPasswordHash(passwordHash);
        }
    }
}
