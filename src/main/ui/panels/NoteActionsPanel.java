package ui.panels;

import model.Note;
import ui.NoteManager;

import javax.swing.*;
import java.awt.*;

// This panel holds buttons to add and delete notes and edit password
public class NoteActionsPanel extends JPanel {
    public static final int WIDTH = SidePanel.WIDTH + NotePanel.WIDTH;
    public static final int HEIGHT = 25;
    private static final String CREATE_NOTE_BUTTON = "Create Note";
    private static final String DELETE_NOTE_TEXT = "Delete Note";
    private static final String PASSWORD_TEXT = "Set/Edit Password";
    private final NoteManager noteManager;

    // EFFECTS: creates new note actions panel
    public NoteActionsPanel(NoteManager noteManager) {
        this.noteManager = noteManager;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(new GridLayout(1, 3));
        this.addCreateNoteButton();
        this.addDeleteNoteButton();
        this.addEditPasswordButton();
    }

    // MODIFIES: this
    // EFFECTS: creates and adds create note button
    private void addCreateNoteButton() {
        JButton createNoteButton = new JButton(CREATE_NOTE_BUTTON);
        createNoteButton.addActionListener(e -> {
            Note newNote = new Note("Enter Note Text", noteManager.getSelectedFolder());
            noteManager.getSelectedFolder().addNote(newNote);
            noteManager.guiDisplayNote(newNote);
            noteManager.refreshNotePanels();
        });
        this.add(createNoteButton);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds delete note button
    private void addDeleteNoteButton() {
        JButton deleteNoteButton = new JButton(DELETE_NOTE_TEXT);
        deleteNoteButton.addActionListener(e -> {
            noteManager.getSelectedFolder().removeNote(noteManager.getSelectedNote());
            noteManager.setSelectedNoteToDefault();
            noteManager.guiDisplayNote(noteManager.getSelectedNote());
            noteManager.refreshNotePanels();
        });
        this.add(deleteNoteButton);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds delete note button
    private void addEditPasswordButton() {
        JButton editPasswordButton = new JButton(PASSWORD_TEXT);
        editPasswordButton.addActionListener(e -> this.editPasswordActionHandler());
        this.add(editPasswordButton);
    }

    // MODIFIES: this
    // EFFECTS: edits selected note's password after prompting user to enter current/new password
    private void editPasswordActionHandler() {
        Note note = this.noteManager.getSelectedNote();
        if (note == null) {
            JOptionPane.showMessageDialog(this.noteManager, "No note is selected!");
            return;
        }
        if (note.isLocked()) {
            JPasswordField pwd = new JPasswordField();
            if (this.noteManager.getPassword("Enter Current Password", pwd)) {
                return;
            }
            if (!note.unlock(new String(pwd.getPassword()))) {
                JOptionPane.showMessageDialog(this.noteManager, "Incorrect password!");
                return;
            }
        }
        JPasswordField pwd = new JPasswordField();
        if (this.noteManager.getPassword("Enter New Password", pwd)) {
            return;
        }
        note.lock(new String(pwd.getPassword()));
    }
}
