package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Supplier in the address book.
 */
public class Supplier extends Person {

    private static final DateTimeFormatter INPUT_TIME_FORMAT = DateTimeFormatter.ofPattern("HHmm");

    private final String openingHoursString;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final Phone alternativeContact;

    /**
     * Constructs a Supplier with the given details.
     */
    public Supplier(Name name, Phone phone, Email email, Address address,
                    Set<Tag> tags, String openingHours, Phone alternativeContact) throws DateTimeParseException {
        super(name, phone, email, address, tags);
        requireAllNonNull(openingHours);
        this.openingHoursString = openingHours;

        LocalTime[] openingTimes = parseTime(openingHours);
        this.openTime = openingTimes[0];
        this.closeTime = openingTimes[1];

        this.alternativeContact = alternativeContact;
    }

    private LocalTime[] parseTime(String openingHours) throws DateTimeParseException {
        String[] splitOpeningHours = openingHours.split(" - ");
        LocalTime openTime = LocalTime.parse(splitOpeningHours[0], INPUT_TIME_FORMAT);
        LocalTime closeTime = LocalTime.parse(splitOpeningHours[1], INPUT_TIME_FORMAT);
        return new LocalTime[]{openTime, closeTime};
    }

    public String getOpeningHours() {
        return openingHoursString;
    }

    public Phone getAlternativeContact() {
        return alternativeContact;
    }

    /**
     * Returns true if both supplier have the same name.
     * This defines a weaker notion of equality between two supplier.
     */
    public boolean isSameSupplier(Supplier otherSupplier) {
        if (otherSupplier == this) {
            return true;
        }

        return otherSupplier != null
                && otherSupplier.getName().equals(getName());
    }

    @Override
    public String getPersonType() {
        return "Supplier";
    }

    /**
     * Returns true if store is open.
     */
    @Override
    public boolean isOpen() {
        LocalTime now = LocalTime.now();

        boolean isAfterOpenTime = now.isAfter(this.openTime);
        boolean isBeforeCloseTime = now.isBefore(this.closeTime);

        return isAfterOpenTime & isBeforeCloseTime;
    }
}
