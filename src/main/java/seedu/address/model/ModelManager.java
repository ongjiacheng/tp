package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 *
 * <p>This model manager stores the current {@link AddressBook}, user preferences,
 * the filtered view of persons shown to the user, and undo/redo history stacks.
 *
 * <p>Undo/redo is implemented using snapshots of the entire address book:
 * <ul>
 *     <li>Before each successful modifying command, the current state is pushed into the undo stack.</li>
 *     <li>When an undo is performed, the current state is pushed into the redo stack,
 *     and the previous state is restored.</li>
 *     <li>When a redo is performed, the current state is pushed back into the undo stack,
 *     and the next state is restored.</li>
 * </ul>
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;

    private final Stack<AddressBook> undoStack;
    private final Stack<AddressBook> redoStack;

    /**
     * Initializes a {@code ModelManager} with the given address book and user preferences.
     *
     * @param addressBook Initial address book data.
     * @param userPrefs Initial user preferences.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        this.filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * Initializes a {@code ModelManager} with an empty address book and default user preferences.
     */
    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    /**
     * Replaces the current user preferences with the given preferences.
     *
     * @param userPrefs User preferences to copy from.
     */
    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    /**
     * Returns the current user preferences.
     *
     * @return Current user preferences.
     */
    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    /**
     * Returns the current GUI settings.
     *
     * @return Current GUI settings.
     */
    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    /**
     * Sets the GUI settings.
     *
     * @param guiSettings New GUI settings.
     */
    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    /**
     * Returns the address book file path from user preferences.
     *
     * @return Path to the address book file.
     */
    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    /**
     * Sets the address book file path in user preferences.
     *
     * @param addressBookFilePath New address book file path.
     */
    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    /**
     * Replaces the current address book contents with the given data.
     *
     * @param addressBook New address book data.
     */
    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        requireNonNull(addressBook);
        this.addressBook.resetData(addressBook);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    /**
     * Returns the current read-only view of the address book.
     *
     * @return Current address book.
     */
    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /**
     * Returns whether the given person already exists in the address book.
     *
     * @param person Person to check.
     * @return True if the person exists.
     */
    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    /**
     * Deletes the given person from the address book.
     *
     * @param target Person to delete.
     */
    @Override
    public void deletePerson(Person target) {
        requireNonNull(target);
        addressBook.removePerson(target);
    }

    /**
     * Adds the given person to the address book.
     *
     * @param person Person to add.
     */
    @Override
    public void addPerson(Person person) {
        requireNonNull(person);
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    /**
     * Replaces the given target person with the edited person.
     *
     * @param target Person to be replaced.
     * @param editedPerson Replacement person.
     */
    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        addressBook.setPerson(target, editedPerson);
    }

    //=========== Favourites  ================================================================================

    /**
     * Sets the given person as favourite and moves it to the favourites list.
     *
     * @param target Person to set as favourite
     */
    @Override
    public void setPersonAsFavourite(Person target) {
        requireAllNonNull(target);
        addressBook.setPersonAsFavourite(target);
    }

    /**
     * Sets the given person as not favourite and removes it from the favourites list.
     */
    @Override
    public void unsetPersonAsFavourite(Person target) {
        requireAllNonNull(target);
        addressBook.unsetPersonAsFavourite(target);
    }

    //=========== Undo / Redo ================================================================================

    /**
     * Saves the current address book state into the undo history.
     *
     * <p>This should be called immediately before a successful modifying action.
     * Calling this method also clears the redo history, since a new branch of
     * changes invalidates previously redoable states.
     */
    @Override
    public void saveStateForUndo() {
        undoStack.push(copyAddressBook());
        redoStack.clear();
    }

    /**
     * Returns whether an undo operation is currently possible.
     *
     * @return True if there is a previous state to restore.
     */
    @Override
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Returns whether a redo operation is currently possible.
     *
     * @return True if there is a redo state to restore.
     */
    @Override
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Restores the previous address book state.
     *
     * <p>The current state is first saved into the redo history so that it can
     * be restored later using redo.
     *
     * @throws CommandException If there is no state available to undo to.
     */
    @Override
    public void undo() throws CommandException {
        if (!canUndo()) {
            throw new CommandException("Nothing to undo.");
        }

        redoStack.push(copyAddressBook());
        AddressBook previousState = undoStack.pop();
        addressBook.resetData(previousState);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    /**
     * Restores the next address book state from redo history.
     *
     * <p>The current state is first saved into the undo history so that it can
     * be restored later using undo.
     *
     * @throws CommandException If there is no state available to redo to.
     */
    @Override
    public void redo() throws CommandException {
        if (!canRedo()) {
            throw new CommandException("Nothing to redo.");
        }

        undoStack.push(copyAddressBook());
        AddressBook nextState = redoStack.pop();
        addressBook.resetData(nextState);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    /**
     * Returns a defensive copy of the current address book.
     *
     * @return A copy of the current address book state.
     */
    private AddressBook copyAddressBook() {
        return new AddressBook(addressBook);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the filtered person list.
     *
     * @return Filtered list of persons.
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    /**
     * Updates the filtered person list to match the given predicate.
     *
     * @param predicate Predicate to filter the displayed list by.
     */
    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    /**
     * Returns true if both model managers contain the same address book, preferences,
     * and filtered person list state.
     *
     * @param other Object to compare against.
     * @return True if equal.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }
}
