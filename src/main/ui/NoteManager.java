package ui;

import model.Folder;
import model.Folders;
import utils.Lockable;
import model.Note;

import java.io.Console;
import java.util.Optional;
import java.util.Scanner;

public class NoteManager {
    private Folders folders = new Folders();
    private Scanner scanner = new Scanner(System.in);
    private Folder defaultFolder;
    private final Console console = System.console();

    // EFFECTS: creates a new NoteManager app and runs it
    public NoteManager() {
        this.initDefaultFolder();
        this.runApp();
    }

    // REQUIRES: default folder and scanner must be set
    // MODIFIES: this
    // EFFECTS: runs the NoteManager app
    private void runApp() {
        this.printInitialMessage();

        while (true) {
            this.printMainMenuMessage();
            String input = this.getNextInput();

            int choice = this.tryGetPositiveInt(input);
            if (choice == -1) {
                continue;
            }

            if (choice == 1 && !this.viewFolders()) {
                break;
            } else if (choice == 2 && !this.createFolder()) {
                break;
            } else if (choice == 3 && !this.deleteFolder()) {
                break;
            } else if (choice == 4) {
                System.out.println("Quitting!");
                System.exit(0);
            } else if (choice < 1 || choice > 4) {
                System.out.println("Invalid response. Try again...");
            }
        }
    }

    // EFFECTS: prints the initial message on stdout
    private void printInitialMessage() {
        System.out.println("Welcome to Note Manager!");
        System.out.println("You can enter \"quit\" or \"exit\" to exit anytime!");
    }

    // EFFECTS: prints the main menu message on stdout
    private void printMainMenuMessage() {
        String message = "\nPlease select an option to continue:\n\n"
                + "[1] view folders\n"
                + "[2] add folder\n"
                + "[3] remove folder\n"
                + "[4|quit] quit\n";
        System.out.println(message);
    }

    // MODIFIES: this
    // EFFECTS: creates the default folder and adds it to app folders
    private void initDefaultFolder() {
        this.defaultFolder = new Folder("Default");
        this.folders.add(this.defaultFolder);
    }

    // Displays all folders
    // REQUIRES: at least one folder
    // MODIFIES: this may be modified by subsequent method calls
    // EFFECTS: shows all folders and then asks user for next action;
    //          exits if "quit" signal is provided,
    //          returns true if user wants to return back to main menu,
    //          false otherwise
    private boolean viewFolders() {
        System.out.println("\n" + this.folders.display());

        Folder folder = this.selectFolder();
        if (folder == null) {
            return true;
        } else {
            return this.showNotes(folder);
        }
    }

    // Creates a new folder interactively
    // MODIFIES: this
    // EFFECTS: interactively creates a new folder;
    //          exiting if "quit" signal is provided,
    //          returns true if user wants to return back to main menu,
    //          false otherwise
    private boolean createFolder() {
        System.out.println("Please enter name of the folder: ");
        String name = scanner.nextLine();
        this.checkQuit(name);

        Folder folder = new Folder(name);
        this.folders.add(folder);

        return true;
    }

    // Deletes a folder interactively
    // MODIFIES: this
    // EFFECTS: interactively deletes a new folder;
    //          exiting if "quit" signal is provided,
    //          returns true if user wants to return back to main menu,
    //          false otherwise
    private boolean deleteFolder() {
        if (this.folders.getFolders().isEmpty()) {
            System.out.println("There are no folders to delete.");
            return true;
        }

        System.out.println("\n" + this.folders.display());
        System.out.println("Enter the number of the folder you'd like to delete.");
        while (true) {
            String input = this.getNextInput();

            int choice = this.tryGetPositiveInt(input);
            if (choice == 0) {
                return true;
            }
            Optional<Folder> optionalFolder = this.folders.getFolders().safeGetIndexOne(choice);
            if (optionalFolder.isPresent()) {
                handleFolderDelete(optionalFolder.get());
                return true;
            } else {
                System.out.println("Invalid number. Try again...");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: deletes folder after prompting user if its not the default folder;
    //          informs users that default folders can't be deleted otherwise
    private void handleFolderDelete(Folder folder) {
        if (folder.equals(this.defaultFolder)) {
            System.out.println("Cannot delete the default folder.");
        } else {
            if (this.deleteFolderConfirmation()) {
                this.folders.remove(folder);
            }
        }
    }

    // Checks if the input is the quit signal
    // EFFECTS: Exits application if input is "quit"
    private void checkQuit(String input) {
        String cleanInput = input.toLowerCase().trim();
        if (cleanInput.equals("quit") || cleanInput.equals("exit")) {
            System.out.println("Quitting!");
            System.exit(0);
        }
    }

    // EFFECTS: tries to parse input into a positive integer,
    //          returning the integer if successful;
    //          otherwise prints an error message and returns -1
    //          -1 along that
    private int tryGetPositiveInt(String input) {
        int choice;
        try {
            choice = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            System.out.println("Your response must be a number! Try again... ");
            return -1;
        }

        if (choice < 0) {
            return -1;
        } else {
            return choice;
        }
    }

    // EFFECTS: reads next line on stdin, returning the trimmed string if it is not
    //          an exit signal.
    private String getNextInput() {
        String input = this.scanner.nextLine();
        this.checkQuit(input);
        return input.trim();
    }

    // EFFECTS: prompts user to select a folder, returning the selected folder;
    //          returns null if user wants to return to the main menu
    private Folder selectFolder() {
        System.out.println("\nEnter the number of the folder you'd like to enter. Enter 0 to return to main menu.");
        while (true) {
            String input = this.scanner.nextLine();
            this.checkQuit(input);
            int choice = this.tryGetPositiveInt(input);
            if (choice == 0) {
                return null;
            }

            Optional<Folder> optionalFolder = folders.getFolders().safeGetIndexOne(choice);
            if (optionalFolder.isPresent()) {
                return optionalFolder.get();
            } else {
                System.out.println("Invalid number. Try again...");
            }
        }
    }

    // EFFECTS: displays all notes in the given folder and prompts user for next action
    //          returns true if user wants to exit to main menu, false otherwise
    private boolean showNotes(Folder folder) {
        // empty line for formatting purpose
        System.out.println();
        if (folder.getNotes().isEmpty()) {
            System.out.println("Empty folder!");
        } else {
            System.out.println(folder.displayNotes());
        }
        return this.folderOperations(folder);
    }

    // MODIFIES: subsequent user actions may modify this
    // EFFECTS: prompts user to perform folder related operations
    //          returns true if user wants to exit to main menu, false otherwise
    private boolean folderOperations(Folder folder) {
        String msg = "\nEnter the number of the note you'd like to view."
                + "\nEnter \"c\" if you'd like to create a new note."
                + "\nEnter \"d\" if you'd like to delete a note."
                + "\nEnter 0 to return to main menu.";

        System.out.println(msg);

        while (true) {
            String input = this.getNextInput();
            if (handleNoteCreateAndDelete(input, folder)) {
                return this.showNotes(folder);
            }

            int choice = this.tryGetPositiveInt(input);
            if (choice == 0) {
                return true;
            }

            Optional<Note> optionalNote = folder.getNotes().safeGetIndexOne(choice);
            if (optionalNote.isPresent()) {
                return this.displayNote(optionalNote.get());
            } else {
                System.out.println("Invalid response. Try again...");
            }
        }
    }

    // EFFECTS: displays note and prompts user for next action,
    //          returns true if user wants to exit to main menu, false otherwise
    private boolean displayNote(Note note) {
        if (!note.isLocked()) {
            System.out.println("\n" + note.display() + "\n");
        } else {
            System.out.println("Note is locked! Enter password to view it: ");
            if (tryUnlockLockable(note, 3)) {
                System.out.println(note.display() + "\n");
            }
        }

        return this.afterNoteDisplay(note);
    }

    // EFFECTS: interactively tries to unlock the lockable,
    //          returning true if successful
    private boolean tryUnlockLockable(Lockable lockable, int tries) {
        for (int i = tries - 1; i >= 0; i--) {
            String password = this.readPassword();
            // empty line for display purposes
            System.out.println();
            if (!lockable.unlock(password)) {
                if (i > 0) {
                    System.out.println("Incorrect password. You have " + i + " tries left.");
                }
            } else {
                return true;
            }
        }

        return false;
    }

    // MODIFIES: subsequent user actions may modify this
    // EFFECTS: handles creation and deletion of notes,
    //          returns true if user wants to exit to main menu, false otherwise
    private boolean handleNoteCreateAndDelete(String input, Folder folder) {
        if (input.equals("c")) {
            this.createNote(folder);
            return true;
        } else if (input.equals("d")) {
            if (!folder.getNotes().getItems().isEmpty()) {
                this.deleteNote(folder);
            }
            return true;
        }

        return false;
    }

    // MODIFIES: folder (and in turn, this)
    // EFFECTS: interactively creates a new note
    private void createNote(Folder folder) {
        System.out.println("\nPlease enter title of the note. Simply press enter to not give a title.");
        String title = this.getNextInput();

        // Limitation of console-based input.
        System.out.println("\nPlease enter the text of the note. The note must be a single line.");
        String text = this.getNextInput();

        System.out.println("\nPlease enter a password if you'd like to lock the note."
                        + "\nSimply press enter to not lock the note.");
        String password = this.readPassword();

        Note note = new Note(text, folder);

        if (!title.isEmpty()) {
            note.setTitle(title);
        }
        if (!password.isEmpty()) {
            note.lock(password);
        }
        System.out.println("Created the note!");
    }

    // MODIFIES: folder (and in turn, this)
    // EFFECTS: interactively removes a note from the folder
    private void deleteNote(Folder folder) {
        while (true) {
            System.out.println("Enter the number of the note you'd like to delete.");
            String input = this.getNextInput();

            int choice = this.tryGetPositiveInt(input);
            Optional<Note> optionalNote = folder.getNotes().safeGetIndexOne(choice);
            if (optionalNote.isPresent()) {
                folder.removeNote(optionalNote.get());
                break;
            } else {
                System.out.println("Invalid number. Try again...");
            }
        }
    }

    // MODIFIES: subsequent user actions may modify this
    // EFFECTS: prompts user to edit note or return to folder view
    private boolean afterNoteDisplay(Note note) {
        this.displayNoteMenuOptions();

        while (true) {
            switch (this.getNextInput()) {
                case "t":
                    this.handleNoteTitleEdit(note);
                    break;
                case "e":
                    this.handleNoteTextEdit(note);
                    break;
                case "p":
                    this.handleNotePasswordEdit(note);
                    break;
                case "r":
                    this.handleNoteRemoveLock(note);
                    break;
                case "b":
                    note.lockIfPasswordSet();
                    return this.showNotes(note.getFolder());
                default:
                    System.out.println("Incorrect option. Try again...");
            }
        }
    }

    // EFFECTS: displays note menu options
    private void displayNoteMenuOptions() {
        String msg = "Please select one of the following options:"
                + "\n- [t] edit title"
                + "\n- [e] edit text"
                + "\n- [p] edit password/lock note"
                + "\n- [r] remove lock from the note"
                + "\n- [b] return back to folder view";

        System.out.println(msg);
    }

    // MODIFIES: this
    // EFFECTS: prompts user to enter new title
    //          after verifying identity if locked
    private void handleNoteTitleEdit(Note note) {
        if (note.isLocked()) {
            System.out.println("Enter password to unlock the note:");
            if (!this.tryUnlockLockable(note, 2)) {
                System.out.println("You cannot edit the note.");
                return;
            }
        }

        System.out.println("Enter new title:");
        String newTitle = this.getNextInput();
        note.editTitle(newTitle);
        System.out.println("Set new title.");
        this.afterNoteEdit(note);
    }

    // MODIFIES: this
    // EFFECTS: prompts user to enter new text
    //          after verifying identity if locked
    private void handleNoteTextEdit(Note note) {
        if (note.isLocked()) {
            System.out.println("Enter password to unlock the note:");
            if (!this.tryUnlockLockable(note, 2)) {
                System.out.println("You cannot edit the note.");
                return;
            }
        }

        System.out.println("Enter new text (the text must be on a single line):");
        String newText = this.getNextInput();
        note.edit(newText);
        System.out.println("Set new text.");
        this.afterNoteEdit(note);
    }

    // MODIFIES: this
    // EFFECTS: prompts user to enter new password
    //          after verifying identity if locked
    private void handleNotePasswordEdit(Note note) {
        if (note.isLocked()) {
            System.out.println("Enter current password:");
            if (!this.tryUnlockLockable(note, 2)) {
                System.out.println("You cannot edit the password.");
                return;
            }
        }

        System.out.println("Enter new password:");
        String newPassword = this.readPassword();
        note.lock(newPassword);
        System.out.println("Set new password.");
        this.afterNoteEdit(note);
    }

    private void afterNoteEdit(Note note) {
        System.out.println("\n" + note.display());
        this.displayNoteMenuOptions();
    }

    // EFFECTS: reads password from the console;
    //          the password is only hidden on normal consoles,
    //          running the program in an IDE will NOT hide the password
    private String readPassword() {
        // `System.console` returns null inside IDEs
        if (this.console != null) {
            return new String(this.console.readPassword());
        } else {
            return this.getNextInput();
        }
    }

    // EFFECTS: interactively prompts the user, asking if they'd like to delete the folder;
    //          returns true if user wants to delete; false otherwise
    private boolean deleteFolderConfirmation() {
        System.out.println(
                "Are you sure you want to delete the folder?"
                + " All notes inside the folder will be deleted as well. [y/n]"
        );
        while (true) {
            String input = this.getNextInput();
            if (input.equalsIgnoreCase("y")) {
                System.out.println("Deleting...");
                return true;
            } else if (input.equalsIgnoreCase("n")) {
                System.out.println("Not deleting.");
                return false;
            } else {
                System.out.println("Invalid response, try again...");
            }
        }
    }

    // EFFECTS: interactively prompts the user to confirm if they'd like to remove lock from the note
    private void handleNoteRemoveLock(Note note) {
        if (!note.hasLock()) {
            System.out.println("The note does not have a lock!");
            return;
        } else if (note.isLocked()) {
            System.out.print("Please enter the current password: ");
            if (!this.tryUnlockLockable(note, 2)) {
                return;
            }
        }

        note.removeLock();
        System.out.println("Removed lock from the note!");
        this.afterNoteEdit(note);
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

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public Folder getDefaultFolder() {
        return defaultFolder;
    }

    public void setDefaultFolder(Folder defaultFolder) {
        this.defaultFolder = defaultFolder;
    }
}
