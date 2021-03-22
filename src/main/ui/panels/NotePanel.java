package ui.panels;

import model.Note;
import ui.NoteManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// This panel displays note.
public class NotePanel extends JPanel {
    public static final int WIDTH = 500;
    public static final int HEIGHT = NoteManager.HEIGHT;
    private static final String NO_NOTE_TEXT = "Create or select a note to view it!";
    private static final int MARGIN = 10;
    private static final String DEFAULT_TITLE = "Enter title";
    private final NoteManager noteManager;
    private final JButton enterPasswordButton = new JButton("Enter Password");
    private JTextArea noteDisplayArea;
    private JTextField noteTitleField;
    private JTextField noNoteField;

    // EFFECTS: creates a new note panel
    public NotePanel(NoteManager noteManager) {
        this.noteManager = noteManager;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(new BorderLayout());

        this.enterPasswordButton.addActionListener(e -> {
            if (noteManager.getSelectedNote() != null && noteManager.getSelectedNote().isLocked()) {
                if (tryUnlockNote(noteManager.getSelectedNote())) {
                    updateNoteDisplay(noteManager.getSelectedNote());
                }
            } else {
                JOptionPane.showMessageDialog(noteManager, "No locked note is selected.");
            }
        });
        this.enterPasswordButton.setVisible(false);
        this.add(this.enterPasswordButton, BorderLayout.SOUTH);
        if (noteManager.getSelectedNote() != null) {
            this.displayNote(noteManager.getSelectedNote());
        } else {
            this.displayNoNote();
        }
    }

    // MODIFIES: this
    // EFFECTS: displays the note after creating the note display area
    private void displayNote(Note note) {
        if (note.isLocked()) {
            if (!this.tryUnlockNote(note)) {
                this.lockedNoteDisplay();
                return;
            }
        }
        this.noteTitleField = new JTextField(this.getNoteTitle(note));
        this.noteTitleField.setEditable(true);
        this.noteTitleField.setMargin(new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
        this.noteTitleField.addKeyListener(this.getSaveKeyHandler());
        this.add(this.noteTitleField, BorderLayout.NORTH);
        this.addNoteDisplayArea(note);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds note display area, displaying the specified note
    private void addNoteDisplayArea(Note note) {
        this.noteDisplayArea = new JTextArea(note.getText());
        this.noteDisplayArea.setEditable(true);
        this.noteDisplayArea.setMargin(new Insets(MARGIN, MARGIN, MARGIN, MARGIN));
        this.noteDisplayArea.addKeyListener(this.getSaveKeyHandler());
        this.add(this.noteDisplayArea);
    }

    // MODIFIES: this
    // EFFECTS: displays no note text after creating the display field
    private void displayNoNote() {
        this.noNoteField = new JTextField(NO_NOTE_TEXT);
        this.add(this.noNoteField);
    }

    // MODIFIES: this
    // EFFECTS: updates the note display area with new note, creating the area if required
    protected void updateNoteDisplay(Note note) {
        this.hideNoNoteField();
        this.enterPasswordButton.setVisible(false);
        if (this.noteDisplayArea != null) {
            if (note.isLocked()) {
                if (!tryUnlockNote(note)) {
                    this.lockedNoteDisplay();
                    return;
                }
            }
            this.noteDisplayArea.setText(note.getText());
            this.noteTitleField.setText(this.getNoteTitle(note));
            this.noteDisplayArea.setVisible(true);
            this.noteTitleField.setVisible(true);
        } else {
            this.displayNote(note);
        }
    }

    // MODIFIES: this
    // EFFECTS: shows the no note field
    protected void showNoNoteField() {
        this.hideNoteFields();
        this.enterPasswordButton.setVisible(false);
        if (this.noNoteField != null) {
            this.noNoteField.setVisible(true);
        } else {
            this.displayNoNote();
        }
    }

    // MODIFIES: this
    // EFFECTS: hides the noNoteField if it exists
    private void hideNoNoteField() {
        if (this.noNoteField != null) {
            this.noNoteField.setVisible(false);
        }
    }

    // MODIFIES: this
    // EFFECTS: hides note fields if they exist
    private void hideNoteFields() {
        if (this.noteDisplayArea != null) {
            this.noteDisplayArea.setVisible(false);
            this.noteTitleField.setVisible(false);
        }
    }

    // EFFECTS: returns the note's title if it has one;
    //          otherwise returns the default title text
    private String getNoteTitle(Note note) {
        if (note.getTitle() != null) {
            return note.getTitle();
        } else {
            return DEFAULT_TITLE;
        }
    }

    // MODIFIES: this
    // EFFECTS: hides note and no note display and shows enter password button
    private void lockedNoteDisplay() {
        this.hideNoteFields();
        this.hideNoNoteField();
        this.enterPasswordButton.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: refreshes the text fields and areas depending on new data
    public void refresh() {
        if (this.noteManager.getSelectedNote() != null) {
            this.updateNoteDisplay(this.noteManager.getSelectedNote());
        } else {
            this.showNoNoteField();
        }
    }

    // REQUIRES: note should be locked with a password
    // MODIFIES: note (if user enters correct password)
    // EFFECTS: prompts user to unlock the note
    private boolean tryUnlockNote(Note note) {
        JPasswordField pwd = new JPasswordField();
        if (!this.noteManager.getPassword("Enter Password", pwd)) {
            return false;
        }
        if (!note.unlock(new String(pwd.getPassword()))) {
            JOptionPane.showMessageDialog(this.noteManager, "Incorrect password!");
            return false;
        }
        return true;
    }

    // MODIFIES: this
    // EFFECTS: updates selected note based on the changes
    private void updateNote() {
        if (!this.noteTitleField.getText().equals(DEFAULT_TITLE) && !this.noteTitleField.getText().isEmpty()) {
            this.noteManager.getSelectedNote().editTitle(noteTitleField.getText());
        }
        this.noteManager.getSelectedNote().edit(noteDisplayArea.getText());
    }

    // EFFECTS: returns a key handler that saves notes when ctrl/cmd+s is pressed
    private KeyListener getSaveKeyHandler() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) { }

            @Override
            public void keyPressed(KeyEvent e) {
                if (noteManager.getSelectedNote() == null) {
                    return;
                }
                if (e.isMetaDown() && e.getKeyChar() == 's') {
                    updateNote();
                    noteManager.refreshNotePanels();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { }
        };
    }
}
