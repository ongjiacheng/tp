package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalTime;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Supplier in the address book.
 */
public class Supplier extends Person {

    private final String openingHours;
    private final Phone alternativeContact;

    /**
     * Constructs a Supplier with the given details.
     */
    public Supplier(Name name, Phone phone, Email email, Address address,
                    Set<Tag> tags, String openingHours, Phone alternativeContact) {
        super(name, phone, email, address, tags);
        requireAllNonNull(openingHours);
        this.openingHours = openingHours;
        this.alternativeContact = alternativeContact;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public Phone getAlternativeContact() {
        return alternativeContact;
    }

    public String getPersonType() {
        return "Supplier";
    }

    /**
     * Returns true if store is open.
     */
    public boolean isOpen() {
        if (openingHours == null || openingHours.isEmpty()) {
            return false;
        }

        String[] parts = openingHours.split("-");
        if (parts.length != 2) {
            return false;
        }

        LocalTime now = LocalTime.now();
        LocalTime openTime = LocalTime.parse(parts[0].trim());
        LocalTime closeTime = LocalTime.parse(parts[1].trim());

        return !now.isBefore(openTime) && now.isBefore(closeTime);
    }

}
