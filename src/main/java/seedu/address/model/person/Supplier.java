package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Supplier in the address book.
 */
public class Supplier extends Person {

    private enum Status {
        CLOSED("closed"),
        OPEN("until closing");

        private final String value;
        Status(String value) { this.value = value; }

        @Override
        public String toString() { return value; }
    }

    private static final DateTimeFormatter INPUT_TIME_FORMAT = DateTimeFormatter.ofPattern("HHmm");
    private static final int MINUTES_PER_HOUR = 60;
    private static final String TIME_LEFT_PREFIX = "%02d:%02d ";

    private final String openingHoursString;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final Phone alternativeContact;

    /**
     * Constructs a {@code Supplier} with the given details.
     * Supplier is not favourite by default.
     */
    public Supplier(Name name, Phone phone, Email email, Address address, String remarks,
                    Set<Tag> tags, String openingHours, Phone alternativeContact) throws DateTimeParseException {
        super(name, phone, email, address, remarks, tags);
        requireAllNonNull(openingHours);
        this.openingHoursString = openingHours;

        LocalTime[] openingTimes = parseTime(openingHours);
        this.openTime = openingTimes[0];
        this.closeTime = openingTimes[1];

        this.alternativeContact = alternativeContact;
    }

    /**
     * Constructs a {@code Supplier} with the given details.
     * Specifies whether supplier is favourite or not.
     */
    public Supplier(Name name, Phone phone, Email email, Address address, String remarks, Set<Tag> tags,
            boolean isFavourite, String openingHours, Phone alternativeContact) throws DateTimeParseException {
        super(name, phone, email, address, remarks, tags, isFavourite);
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

    /**
     * Returns the opening hours of supplier.
     */
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

    /**
     * Returns a {@code Supplier} with identical information, but is favourite.
     */
    @Override
    public Person createFavouritePerson() {
        boolean isFavourite = true;
        return new Supplier(this.getName(), this.getPhone(), this.getEmail(), this.getAddress(),
                this.getRemarks(), this.getTags(), isFavourite, this.getOpeningHours(), this.alternativeContact);
    }

    /**
     * Returns a {@code Supplier} with identical information, but is not favourite.
     */
    @Override
    public Person createNotFavouritePerson() {
        boolean isFavourite = false;
        return new Supplier(this.getName(), this.getPhone(), this.getEmail(), this.getAddress(),
                this.getRemarks(), this.getTags(), isFavourite, this.getOpeningHours(), this.alternativeContact);
    }

    /**
     * Returns Supplier as a String.
     */
    @Override
    public String getPersonType() {
        return "Supplier";
    }

    /**
     * Returns the current local time.
     */
    public boolean isOpen() {
        return isOpen(LocalTime.now());
    }

    /**
     * Returns true if store is open with currentTime passed in.
     */
    public boolean isOpen(LocalTime currentTime) {
        boolean isAfterOpenTime = currentTime.isAfter(this.openTime);
        boolean isBeforeCloseTime = currentTime.isBefore(this.closeTime);

        if (openTime.isBefore(closeTime)) {
            // Normal case
            return isAfterOpenTime && isBeforeCloseTime;
        } else {
            // Overnight case
            return isAfterOpenTime || isBeforeCloseTime;
        }
    }

    /**
     * Returns time left.
     * @return Time left as a string representation.
     */
    public String getTimeLeft() {
        LocalTime currentTime = LocalTime.now();
        Duration duration = Duration.between(currentTime, closeTime);

        if (duration.isNegative()) {
            return Status.CLOSED.toString();
        }

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % MINUTES_PER_HOUR;

        return String.format(TIME_LEFT_PREFIX + Status.OPEN, hours, minutes);
    }
}
