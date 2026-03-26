package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** {@code Predicate} that returns Supplier.isOpen */
    Predicate<Person> PREDICATE_SHOW_ALL_OPEN =
            person -> person.isOpen();

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /**
     * Sets the person as favourite and moves the given person to the favourites list.
     * The person must exist in the address book.
     * The person must not be favourite.
     */
    void setPersonAsFavourite(Person target);

    /**
     * Removes the person's favourite status and removes the person from the favourites list.
     * The person must exist in the address book.
     * The person must be favourite.
     */
    void unsetPersonAsFavourite(Person target);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);
    /**
     * Saves the current address book state into undo history.
     * Should be called right before executing a successful modifying action.
     */
    void saveStateForUndo();

    /** Returns true if there is a previous state to undo to. */
    boolean canUndo();

    /** Returns true if there is a next state to redo to. */
    boolean canRedo();

    /**
     * Restores the previous address book state and pushes current state into redo history.
     */
    void undo() throws seedu.address.logic.commands.exceptions.CommandException;

    /**
     * Restores the next address book state and pushes current state into undo history.
     */
    void redo() throws seedu.address.logic.commands.exceptions.CommandException;
}
