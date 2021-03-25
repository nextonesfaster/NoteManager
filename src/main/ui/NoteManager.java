package ui;

import model.Folder;
import model.Folders;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.panels.*;
import model.Note;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

// GUI-based Note Manager application.
public class NoteManager extends JFrame {
    public static final int HEIGHT = 500;
    private static final Toolkit DEFAULT_TOOLKIT = Toolkit.getDefaultToolkit();
    private final SidePanel sidePanel;
    private final NotePanel notePanel;
    private final FolderActionsPanel folderActionsPanel;
    private Folders folders = new Folders();
    private Folder defaultFolder;
    private Folder selectedFolder;
    private Note selectedNote;
    private JsonReader jsonReader;

    // EFFECTS: creates a new NoteManager app and runs it
    public NoteManager() {
        super("Note Manager");
        this.initFolders();
        this.setSelectedToDefault();
        this.sidePanel = new SidePanel(this);
        this.add(this.sidePanel, BorderLayout.WEST);
        this.notePanel = new NotePanel(this);
        this.add(this.notePanel);
        NoteActionsPanel noteActionsPanel = new NoteActionsPanel(this);
        this.add(noteActionsPanel, BorderLayout.NORTH);
        IOPanel ioPanel = new IOPanel(this);
        this.add(ioPanel, BorderLayout.EAST);
        this.folderActionsPanel = new FolderActionsPanel(this);
        this.add(this.folderActionsPanel, BorderLayout.SOUTH);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                promptToSaveToFile();
                System.exit(0);
            }
        });
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes folders by asking if users want to load from file;
    //          initializes default if they don't
    private void initFolders() {
        if (!this.promptToLoadFromFile()) {
            this.initDefaultFolder();
        }
    }

    // MODIFIES: this
    // EFFECTS: reads user's data, setting folders and default folder and
    //          returning true if there is at least one folder, otherwise
    //          returns false
    private boolean readUsersData() throws IOException {
        Folders folders = this.jsonReader.read();
        if (folders.getFolders().size() > 0) {
            this.defaultFolder = folders.getFolders().get(0);
            this.setSelectedToDefault();
            this.folders = folders;
            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: creates the default folder and adds it to app folders
    private void initDefaultFolder() {
        this.defaultFolder = new Folder("Default");
        this.folders.add(this.defaultFolder);
    }

    // MODIFIES: this
    // EFFECTS: saves notes to a file; returning true if successful
    public boolean saveToFile(String fileName) {
        JsonWriter jsonWriter = new JsonWriter("data/" + fileName + ".json");
        try {
            jsonWriter.open();
            jsonWriter.write(this.folders);
            jsonWriter.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: displays the note in the note panel
    public void guiDisplayNote(Note note) {
        this.selectedNote = note;
        this.notePanel.refresh();
    }

    // MODIFIES: this
    // EFFECTS: refreshes the app by updating note-related panels
    public void refreshNotePanels() {
        this.notePanel.refresh();
        this.sidePanel.refresh();
    }

    // MODIFIES: this
    // EFFECTS: refreshes the app by updating folder actions panel
    public void refreshFolderActions(int index) {
        this.folderActionsPanel.refresh(index);
    }

    // MODIFIES: this
    // EFFECTS: locks all lockable folders and notes
    public void lock() {
        for (Folder folder : this.folders.getFolders()) {
            folder.lockIfPasswordSet();
            for (Note note : folder.getNotes()) {
                note.lockIfPasswordSet();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: saves notes to a file; returning true if successful
    public boolean loadFromFile(String fileName) {
        this.jsonReader = new JsonReader("data/" + fileName + ".json");
        try {
            return this.readUsersData();
        } catch (IOException e) {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: sets selected folder and note to sensible defaults
    public void setSelectedToDefault() {
        this.selectedFolder = this.defaultFolder;
        if (!this.selectedFolder.getNotes().isEmpty()) {
            this.selectedNote = this.selectedFolder.getNotes().get(0);
        } else {
            this.selectedNote = null;
        }
    }

    // MODIFIES: this
    // EFFECTS: sets selected note to sensible default
    public void setSelectedNoteToDefault() {
        if (!this.selectedFolder.getNotes().isEmpty()) {
            this.selectedNote = this.selectedFolder.getNotes().get(0);
        } else {
            this.selectedNote = null;
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to load folders from a file;
    //          returns true and updates folders if successful;
    //          false otherwise
    public boolean promptToLoadFromFile() {
        String fileName = JOptionPane.showInputDialog(
                "Enter name of the file to load from (press cancel to not load from file)"
        );
        if (fileName != null) {
            if (this.loadFromFile(fileName)) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Unable to load folders from the file.");
                return false;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: prompts user to save folders to a file;
    //          returns true and locks all lockable folders and notes
    //          if successful; false otherwise
    public boolean promptToSaveToFile() {
        String fileName = JOptionPane.showInputDialog(
                "Enter name of the file to save to (press cancel to not save to file)"
        );
        if (fileName != null && !fileName.isEmpty()) {
            this.lock();
            return this.saveToFile(fileName);
        }
        return false;
    }

    // MODIFIES: passwordField
    // EFFECTS: prompts user to enter password; returns entered password if user
    //          does not cancel or enter empty password, returns empty otherwise
    public Optional<String> getPassword(String title, JPasswordField passwordField) {
        int action = JOptionPane.showConfirmDialog(this, passwordField, title, JOptionPane.OK_CANCEL_OPTION);
        if (action == JOptionPane.OK_OPTION && passwordField.getPassword().length != 0) {
            return Optional.of(new String(passwordField.getPassword()));
        } else {
            return Optional.empty();
        }
    }

    // EFFECTS: plays a beep sound
    public void beep() {
        DEFAULT_TOOLKIT.beep();
    }

    /**
     * GETTERS AND SETTERS
     */
    public Folders getFolders() {
        return folders;
    }

    public void setFolders(Folders folders) {
        this.folders = folders;
    }

    public Folder getDefaultFolder() {
        return defaultFolder;
    }

    public Folder getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(Folder selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public Note getSelectedNote() {
        return selectedNote;
    }
}
