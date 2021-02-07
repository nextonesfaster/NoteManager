package model;

import utils.SortableSet;

// A container class for all folders.
public class Folders {
    private SortableSet<Folder> folders;

    // EFFECTS: creates an empty list of folders
    public Folders() {
        this.folders = new SortableSet<>();
    }

    // MODIFIES: this
    // EFFECTS: adds folder and returns true if it is not already present;
    //          returns false otherwise
    public boolean add(Folder folder) {
        return this.folders.add(folder);
    }

    // MODIFIES: this
    // EFFECTS: returns true and removes folder from list if it is present;
    //          returns false otherwise
    public boolean remove(Folder folder) {
        return this.folders.remove(folder);
    }

    // REQUIRES: at least one folder
    // EFFECTS: returns formatted string listing all folders
    public String display() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Folders:");
        int i = 0;
        for (Folder folder : this.folders) {
            i += 1;
            stringBuilder.append("\n- [").append(i).append("] ").append(folder.getName());
        }
        return stringBuilder.toString();
    }

    /**
     * GETTERS AND SETTERS
     */
    public SortableSet<Folder> getFolders() {
        return folders;
    }

    public void setFolders(SortableSet<Folder> folders) {
        this.folders = folders;
    }
}
