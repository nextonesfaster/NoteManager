package ui.panels;

import model.Note;
import ui.NoteManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// This panel displays notes in the sidebar.
public class SidePanel extends JPanel {
    public static final int WIDTH = 150;
    public static final int HEIGHT = NoteManager.HEIGHT;
    private final NoteManager noteManager;
    private final List<JButton> noteButtons = new ArrayList<>();

    // EFFECTS: creates a new side panel
    public SidePanel(NoteManager noteManager) {
        this.noteManager = noteManager;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.gray);
        this.setLayout(new GridLayout(20, 1));
        this.displayNotes(noteManager.getSelectedFolder().getNotes());
    }

    // MODIFIES: this
    // EFFECTS: displays notes in the sidebar by adding buttons per note
    private void displayNotes(Iterable<Note> notes) {
        for (Note note : notes) {
            String summary = note.getSummary();
            JButton noteButton = new JButton(summary);
            noteButton.addActionListener(e -> this.noteManager.guiDisplayNote(note));
            this.noteButtons.add(noteButton);
            this.add(noteButton);
        }
    }

    // MODIFIES: this
    // EFFECTS: refreshes the buttons by adding/removing new notes
    public void refresh() {
        for (JButton button : this.noteButtons) {
            this.remove(button);
        }
        this.revalidate();
        this.repaint();
        this.noteButtons.clear();
        this.displayNotes(this.noteManager.getSelectedFolder().getNotes());
    }
}
