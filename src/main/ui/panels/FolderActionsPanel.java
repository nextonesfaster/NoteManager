package ui.panels;

import model.Folder;
import ui.NoteManager;

import javax.swing.*;
import java.awt.*;

// This panel holds buttons for folder related actions.
public class FolderActionsPanel extends JPanel {
    public static final int WIDTH = SidePanel.WIDTH + NotePanel.WIDTH;
    public static final int HEIGHT = 25;
    private static final String CREATE_FOLDER_TEXT = "Create Folder";
    private static final String DELETE_FOLDER_TEXT = "Delete Folder";
    private final NoteManager noteManager;
    private JComboBox<Folder> foldersBox;

    // EFFECTS: creates a new folder actions panel
    public FolderActionsPanel(NoteManager noteManager) {
        this.noteManager = noteManager;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(new GridLayout(1, 3));
        this.addFolderSelectionBox();
        this.addCreateFolderButton();
        this.addDeleteFolderButton();
    }

    // MODIFIES: this
    // EFFECTS: adds a dropdown to select folder
    private void addFolderSelectionBox() {
        this.foldersBox = new JComboBox<>();
        for (Folder folder : this.noteManager.getFolders().getFolders()) {
            this.foldersBox.addItem(folder);
        }
        this.foldersBox.addActionListener(e -> {
            Folder newSelectedFolder = foldersBox.getItemAt(foldersBox.getSelectedIndex());
            if (newSelectedFolder == null) {
                return;
            }
            noteManager.setSelectedFolder(newSelectedFolder);
            noteManager.setSelectedNoteToDefault();
            noteManager.refreshNotePanels();
            refresh(foldersBox.getSelectedIndex());
        });
        this.add(this.foldersBox);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds create folder button
    private void addCreateFolderButton() {
        JButton createFolderButton = new JButton(CREATE_FOLDER_TEXT);
        createFolderButton.addActionListener(e -> {
            String folderName = JOptionPane.showInputDialog("Enter name of the folder");
            if (folderName != null) {
                Folder newFolder = new Folder(folderName);
                noteManager.getFolders().add(newFolder);
                noteManager.setSelectedFolder(newFolder);
                noteManager.setSelectedNoteToDefault();
                noteManager.refreshNotePanels();
                refresh(foldersBox.getItemCount());
            }
        });
        this.add(createFolderButton);
    }

    // MODIFIES: this
    // EFFECTS: creates and adds delete folder button
    private void addDeleteFolderButton() {
        JButton deleteFolderButton = new JButton(DELETE_FOLDER_TEXT);
        deleteFolderButton.addActionListener(e -> {
            if (noteManager.getSelectedFolder().equals(noteManager.getDefaultFolder())) {
                JOptionPane.showMessageDialog(noteManager, "Cannot delete the default folder!");
            } else {
                noteManager.getFolders().remove(noteManager.getSelectedFolder());
                noteManager.setSelectedToDefault();
                noteManager.refreshNotePanels();
                refresh(foldersBox.getItemCount() - 1);
            }
        });
        this.add(deleteFolderButton);
    }

    // MODIFIES: this
    // EFFECTS: refreshes the panel by updating the folders box
    public void refresh(int index) {
        this.foldersBox.removeAllItems();
        this.revalidate();
        this.repaint();
        for (Folder folder : this.noteManager.getFolders().getFolders()) {
            this.foldersBox.addItem(folder);
        }
        if (index >= this.foldersBox.getItemCount()) {
            index = 0;
        }
        this.foldersBox.setSelectedIndex(index);
    }
}
