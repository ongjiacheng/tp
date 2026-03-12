package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Customer in the address book.
 */
public class Customer extends Person {

    /**
     * Constructs a Customer with the given details.
     */
    public Customer(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
    }

    /**
     * Returns the person type.
     */
    public String getPersonType() {
        return "Customer";
    }

    public String getOpeningHours() {
        return "9:00 - 18:00";
    }

}
