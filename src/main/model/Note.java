package model;

import utils.Lockable;

import java.time.LocalDateTime;
import java.util.StringTokenizer;

// Represents a note.
public class Note extends Lockable {
    private String title;
    private String text;
    private final LocalDateTime dateTimeAdded = LocalDateTime.now();
    private LocalDateTime dateTimeModified = LocalDateTime.now();
    private Folder folder;

    // MODIFIES: folder
    // EFFECTS: creates a new note with given title, text and password inside provided folder
    //          the note is added to the provided folder
    public Note(String title, String text, String password, Folder folder) {
        this.setTitle(title);
        this.setText(text);
        this.lock(password);
        this.setFolder(folder);
    }

    // MODIFIES: folder
    // EFFECTS: creates a new note with given title and text inside provided folder
    //          the note is added to the provided folder
    public Note(String title, String text, Folder folder) {
        this.setTitle(title);
        this.setText(text);
        this.setFolder(folder);
    }

    // MODIFIES: folder
    // EFFECTS: creates a new note with given text inside provided folder
    //          the note is added to the provided folder
    public Note(String text, Folder folder) {
        this.setText(text);
        this.setFolder(folder);
    }

    // EFFECTS: creates a new note *not* in any folder
    public Note(String text) {
        this.setText(text);
    }

    // Moves the note to the new folder
    // MODIFIES: this.folder, newFolder
    // EFFECTS: changes the folder of the note to the new folder
    //          this includes removing the note from the previous folder
    //          and adding it to the new folder;
    //          returns true if change was successful, false otherwise
    public boolean changeFolder(Folder newFolder) {
        this.folder.removeNote(this);
        this.setFolder(newFolder);
        return true;
    }

    // Returns the character count of the note without including the title.
    //
    // The character count includes whitespaces and newlines.
    public int characterCount() {
        return this.countCharacters(this.text);
    }

    // Returns the character count of the note including the title.
    //
    // The character count includes whitespaces and newlines.
    public int characterCountWithTitle() {
        return this.countCharacters(this.text + this.title);
    }

    // Returns the word count of the note without including the title.
    public int wordCount() {
        return this.countWords(this.text);
    }

    // Returns the word count of the note including the title.
    public int wordCountWithTitle() {
        return this.countWords(this.text + " " + this.title);
    }

    // Returns the character count of the provided text
    private int countCharacters(String text) {
        return text.length();
    }

    // Returns the word count of the provided text
    private int countWords(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text);
        return tokenizer.countTokens();
    }

    // Edits the text of the note
    // MODIFIES: this
    // EFFECTS: updates the text of the note
    public void edit(String newText) {
        this.setText(newText);
        this.setDateTimeModified(LocalDateTime.now());
    }

    // Edits the text of the note
    // MODIFIES: this
    // EFFECTS: updates the text of the note
    public void editTitle(String newTitle) {
        this.setTitle(newTitle);
        this.setDateTimeModified(LocalDateTime.now());
    }

    // Searches for given text in the note
    // EFFECTS: returns true if the given text is found in the note title or text;
    //          false otherwise
    public boolean search(String text) {
        if (this.title != null && this.searchInTitle(text)) {
            return true;
        } else {
            return this.searchInText(text);
        }
    }

    // Searches for given text in the note's text
    // EFFECTS: returns true if the given text is found in the note text;
    //          false otherwise
    public boolean searchInText(String text) {
        return this.searchText(this.text, text);
    }

    // Searches for given text in the note's title
    // REQUIRES: title is set
    // EFFECTS: returns true if the given text is found in the note title;
    //          false otherwise
    public boolean searchInTitle(String text) {
        return this.searchText(this.title, text);
    }

    // Searches for toSearch in provided text
    // EFFECTS: returns true if toSearch is found in text; false otherwise
    private boolean searchText(String text, String toSearch) {
        return text.contains(toSearch);
    }

    // Returns a summary of the note
    // EFFECTS: returns a summary of the note;
    //          the summary is just the title if it is provided,
    //          otherwise it is at most the first 20 characters
    //          with "..." appended
    public String getSummary() {
        if (this.getTitle() != null) {
            return this.getTitle();
        } else {
            if (this.getText().length() > 20) {
                return this.getText().substring(0, 20) + "...";
            } else {
                return this.getText();
            }
        }
    }

    // REQUIRES: note is unlocked
    // EFFECTS: returns a well-formatted display of the note
    public String display() {
        StringBuilder stringBuilder = new StringBuilder();

        if (this.title != null) {
            stringBuilder.append("Title: ").append(this.title).append("\n\n");
        }

        stringBuilder.append(this.text).append("\n\n").append("Words: ").append(this.wordCount());

        return stringBuilder.toString();
    }

    // MODIFIES: this, folder
    // EFFECTS: sets the folder for this note and then adds the note to the folder
    public void setFolder(Folder folder) {
        this.folder = folder;
        this.folder.addNote(this);
    }

    /**
     * GETTERS AND SETTERS
     */
    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDateTimeAdded() {
        return dateTimeAdded;
    }

    public LocalDateTime getDateTimeModified() {
        return dateTimeModified;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDateTimeModified(LocalDateTime dateTimeModified) {
        this.dateTimeModified = dateTimeModified;
    }

}