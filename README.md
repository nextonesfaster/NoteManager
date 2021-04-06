# Note Manager

Note manager is an application to take organised notes quickly and efficiently. Notes are organised in different folders and can be sorted by date added, date modified, or alphabetically. Notes can also be protected by a password.

Students and professionals alike take notes for various reasons. This application will be useful for most people.

I have used a variety of note taking applications but all of them lacked some features that I wanted. To address that, I decided to create my own note manager! 

The application is made using Java and therefore runs on all platforms that support Java. Some of them are:

- Windows
- macOS
- Linux

## User Stories

- [x] As a user, I would like to create notes and folders.
- [x] As a user, I would like to view all folders.
- [x] As a user, I would like to view all notes inside a folder.
- [ ] As a user, I would like to search for text in my notes.
- [x] As a user, I would like to lock my notes with a password.
- [x] As a user, I would like to remove notes.
- [x] As a user, I would like to edit notes.
- [ ] As a user, I would like to sort notes in a folder.
- [x] As a user, I would like to save my notes to a file.
- [x] As a user, I would like to load my notes from a file.
- [x] As a user, I would like to be reminded to save my notes to file when I quit the application.
- [x] As a user, I would like to be given the option to load my notes from a file on start-up.
- [x] As a user, I would like to hear sound when I perform certain actions.

## Phase 4: Task 2

I chose to implement the first option:

> Test and design a class in your model package that is robust. You must have at least one method that throws a checked exception. You must have one test for the case where the exception is expected and another where the exception is not expected.

The `Note` class has a robust design, and the `searchInTitle` and `display` methods throw checked exceptions.

## Phase 4: Task 3

If I had more time, I'd refactor:

- the `Folders` class to be an `Iterator`. Calling the `getFolders()` method on an instance of `Folders` class feels odd.
