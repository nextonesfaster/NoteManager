package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import utils.Lockable;
import utils.SortOption;
import utils.SortableSet;

import java.util.Comparator;
import java.util.Optional;

// Represents a folder to contain notes.
public class Folder extends Lockable implements Writable {
    private final String name;
    private SortableSet<Note> notes;
    private SortOption sortOption = SortOption.NEWEST_ADDED_FIRST;

    // EFFECTS: creates a new folder with given name
    public Folder(String name) {
        this.name = name;
        this.notes = new SortableSet<>();
    }

    // Adds a note to the folder
    // MODIFIES: this
    // EFFECTS: adds a note to the folder
    public void addNote(Note note) {
        this.notes.add(note);
    }

    // Removes the note from this folder
    // MODIFIES: this
    // EFFECTS: removes the note from the folder,
    //          returning true if the note is present,
    //          false otherwise;
    public boolean removeNote(Note note) {
        return this.notes.remove(note);
    }

    // The total number of notes in the folder.
    // EFFECTS: returns the total number of notes in the folder
    public int totalNotes() {
        return this.notes.size();
    }

    // Searches for text in the folder's notes
    // EFFECTS: returns an Optional instance of note if the
    //          text is found in a note and the note is unlocked;
    //          returns an empty instance of Optional otherwise
    public Optional<Note> search(String text) {
        for (Note note : this.getNotes()) {
            if (!note.isLocked() && note.search(text)) {
                return Optional.of(note);
            }
        }

        return Optional.empty();
    }

    // Sorts the notes using the given sort option.
    // MODIFIES: this
    // EFFECTS: returns a list of sorted notes based on the sort option
    public SortableSet<Note> sort(SortOption sortOption) {
        this.setSortOption(sortOption);
        if (sortOption == SortOption.NEWEST_ADDED_FIRST) {
            this.notes.sort(Comparator.comparing(Note::getDateTimeAdded).reversed());
        } else if (sortOption == SortOption.OLDEST_ADDED_FIRST) {
            this.notes.sort(Comparator.comparing(Note::getDateTimeAdded));
        } else if (sortOption == SortOption.NEWEST_MODIFIED_FIRST) {
            this.notes.sort(Comparator.comparing(Note::getDateTimeModified).reversed());
        } else if (sortOption == SortOption.OLDEST_MODIFIED_FIRST) {
            this.notes.sort(Comparator.comparing(Note::getDateTimeModified));
        } else {
            return null;
        }

        return this.getNotes();
    }

    // REQUIRES: at least one note
    // EFFECTS: returns formatted string listing all folder notes
    public String displayNotes() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Notes:");
        int i = 0;
        for (Note note : this.notes) {
            i += 1;
            stringBuilder.append("\n- [").append(i).append("] ").append(note.getSummary());
        }

        return stringBuilder.toString();
    }

    // EFFECTS: returns a JSON representation of the folder,
    //          containing all the notes
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);

        JSONArray jsonArray = new JSONArray();
        for (Note note : this.notes) {
            jsonArray.put(note.toJson());
        }
        json.put("notes", jsonArray);

        this.addLockableToJson(json);

        return json;
    }

    /**
     * GETTERS AND SETTERS
     */
    public String getName() {
        return name;
    }

    public SortableSet<Note> getNotes() {
        return notes;
    }

    public SortOption getSortOption() {
        return sortOption;
    }

    public void setNotes(SortableSet<Note> notes) {
        this.notes = notes;
    }

    public void setSortOption(SortOption sortOption) {
        this.sortOption = sortOption;
    }
}
