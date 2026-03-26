package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private Index nextFavouriteIndex = Index.fromZeroBased(0);
    private final UniquePersonList persons;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    public void setNextFavouriteIndex(List<Person> persons) {
        int favouritePersonCount = countFavouritePersons(persons);
        this.nextFavouriteIndex = Index.fromZeroBased(favouritePersonCount);
    }

    private int countFavouritePersons(List<Person> persons) {
        return (int) persons.stream()
                .filter(person -> person.isFavourite())
                .count();
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setNextFavouriteIndex(newData.getPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        assert target.isFavourite() == editedPerson.isFavourite()
                : "Modifying commands should not change favourite status";

        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Sets {@code target} as favourite and moves it to the favourites list.
     */
    public void setPersonAsFavourite(Person target) {
        requireNonNull(target);

        assert persons.asUnmodifiableObservableList().indexOf(target) >= nextFavouriteIndex.getZeroBased()
                : "Person must not be in favourites list";
        persons.movePerson(target, nextFavouriteIndex);
        assert persons.contains(target);

        nextFavouriteIndex.increment();

        persons.setPerson(target, target.createFavouritePerson());
    }

    /**
     * Sets {@code target} as not favourite and removes it from the favourites list.
     */
    public void unsetPersonAsFavourite(Person target) {
        requireNonNull(target);

        assert persons.asUnmodifiableObservableList().indexOf(target) < nextFavouriteIndex.getZeroBased()
                : "Person must be in favourites list";
        assert nextFavouriteIndex.getZeroBased() != 0;
        nextFavouriteIndex.decrement();

        persons.movePerson(target, nextFavouriteIndex);
        assert persons.contains(target);

        persons.setPerson(target, target.createNotFavouritePerson());
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
