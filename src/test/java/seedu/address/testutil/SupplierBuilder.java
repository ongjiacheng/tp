package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Supplier;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class SupplierBuilder {

    public static final String DEFAULT_NAME = "Tan Ah Kow";
    public static final String DEFAULT_PHONE = "96466366";
    public static final String DEFAULT_EMAIL = "tak@gmail.com";
    public static final String DEFAULT_ADDRESS = "456, Tampines Ave 7, #04-222";
    public static final String DEFAULT_REMARKS = "Is friendly.";
    public static final String DEFAULT_HOURS = "0000 - 2359";
    public static final String DEFAULT_ALT_PHONE = "97577477";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private String remarks;
    private Set<Tag> tags;
    private String openingHours;
    private Phone alternativeContact;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public SupplierBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        remarks = DEFAULT_REMARKS;
        tags = new HashSet<>();
        openingHours = DEFAULT_HOURS;
        alternativeContact = new Phone(DEFAULT_ALT_PHONE);
    }

    /**
     * Initializes the SupplierBuilder with the data of {@code supplierToCopy}.
     */
    public SupplierBuilder(Supplier supplierToCopy) {
        name = supplierToCopy.getName();
        phone = supplierToCopy.getPhone();
        email = supplierToCopy.getEmail();
        address = supplierToCopy.getAddress();
        remarks = supplierToCopy.getRemarks();
        tags = new HashSet<>(supplierToCopy.getTags());
        openingHours = supplierToCopy.getOpeningHours();
        alternativeContact = supplierToCopy.getAlternativeContact();
    }

    /**
     * Sets the {@code Name} of the {@code Supplier} that we are building.
     */
    public SupplierBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public SupplierBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Supplier} that we are building.
     */
    public SupplierBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code remarks} of the {@code Supplier} that we are building.
     */
    public SupplierBuilder withRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Supplier} that we are building.
     */
    public SupplierBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Supplier} that we are building.
     */
    public SupplierBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code OpeningHours} of the {@code Supplier} that we are building.
     */
    public SupplierBuilder withOpeningHours(String openingHours) {
        this.openingHours = openingHours;
        return this;
    }

    /**
     * Sets the {@code AlternativeContact} of the {@code Supplier} that we are building.
     */
    public SupplierBuilder withAlternativeContact(String alternativeContact) {
        this.alternativeContact = new Phone(alternativeContact);
        return this;
    }

    public Supplier build() {
        return new Supplier(name, phone, email, address, remarks, tags, openingHours, alternativeContact);
    }

}
