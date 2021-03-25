package ui.panels;

import ui.NoteManager;

import javax.swing.*;
import java.awt.*;

// This panel is used to read and write to and from files.
public class IOPanel extends JPanel {
    public static final int WIDTH = 150;
    public static final int HEIGHT = NoteManager.HEIGHT;
    private static final String saveText = "Save";
    private static final String loadText = "Load";
    private final NoteManager noteManager;

    // EFFECTS: creates a new IOPanel
    public IOPanel(NoteManager noteManager) {
        this.noteManager = noteManager;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.addSaveButton();
        this.addLoadButton();
    }

    // MODIFIES: this
    // EFFECTS: creates and adds the save button
    private void addSaveButton() {
        JButton saveButton = new JButton(saveText);
        saveButton.addActionListener(e -> {
            if (!this.noteManager.promptToSaveToFile()) {
                JOptionPane.showMessageDialog(this, "There was an error while saving the notes.");
            } else {
                this.noteManager.beep();
            }
        });
        this.add(saveButton);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds the load button
    private void addLoadButton() {
        JButton loadButton = new JButton(loadText);
        loadButton.addActionListener(e -> {
            if (noteManager.promptToLoadFromFile()) {
                noteManager.refreshNotePanels();
                noteManager.refreshFolderActions(0);
                this.noteManager.beep();
            }
        });
        this.add(loadButton);
    }
}
