package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data field
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final String remarks;

    private final boolean isFavourite;

    /**
     * Constructs a {@code Person} with the given details.
     * Person is not favourite by default.
     */
    public Person(Name name, Phone phone, Email email, Address address, String remarks, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, remarks, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
	      this.remarks = remarks;
        this.isFavourite = false;
    }

    /**
     * Constructs a {@code Person} with the given details.
     * Specifies whether person is favourite or not.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, boolean isFavourite) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.isFavourite = isFavourite;
    }

    public String getPersonType() {
        return "Person";
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public String getRemarks() {
        return remarks;
    }

    public boolean isOpen() {
        return false;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    /**
     * Returns a {@code Person} with identical information, but is favourite.
     */
    public Person createFavouritePerson() {
        boolean isFavourite = true;
        return new Person(this.name, this.phone, this.email, this.address, this.tags, isFavourite);
    }

    /**
     * Return a {@code Person} with identical information, but is not favourite.
     */
    public Person createNotFavouritePerson() {
        boolean isFavourite = false;
        return new Person(this.name, this.phone, this.email, this.address, this.tags, isFavourite);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public String getOpeningHours() {
        return "";
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .toString();
    }

}
