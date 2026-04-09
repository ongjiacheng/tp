package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OPENING_HOURS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Supplier;

/**
 * Adds a supplier contact to the address book.
 *
 * <p>A supplier contact is represented by a {@link Supplier} and must have at least one tag.
 * The supplier may also contain supplier-specific fields such as opening hours.</p>
 */
public class AddSupplierCommand extends Command {

    public static final String COMMAND_WORD = "adds";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a supplier contact to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_OPENING_HOURS + "OPENING HOURS "
            + PREFIX_TAG + "TAG [" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Ah Seng "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "a@b.com "
            + PREFIX_ADDRESS + "Yishun "
            + PREFIX_OPENING_HOURS + "0900 - 1800 "
            + PREFIX_TAG + "vegetable";

    public static final String MESSAGE_SUCCESS = "New supplier added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This supplier already exists in the address book";

    public static final String MESSAGE_TAG_REQUIRED = "Suppliers must have at least one tag (t/...)\n";
    public static final String MESSAGE_OPENING_HOURS_REQUIRED = "Suppliers must have opening hours (o/...)\n";
    public static final String MESSAGE_INCORRECT_TIME_FORMAT = "Opening hours should follow 'HHmm - HHmm'";
    public static final String MESSAGE_INVALID_TIME =
            "Opening hours should be between 0000 and 2300, minutes should be between 00 and 59";

    private final Supplier toAdd;

    /**
     * Creates an {@code AddSupplierCommand} to add the specified supplier.
     *
     * @param supplier Supplier to be added to the address book. Must not be null.
     */
    public AddSupplierCommand(Supplier supplier) {
        requireNonNull(supplier);
        toAdd = supplier;
    }

    /**
     * Executes the command and adds the supplier to the address book.
     *
     * @param model The model containing the current address book data.
     * @return A {@code CommandResult} containing the result message to be shown to the user.
     * @throws CommandException If the supplier already exists in the address book.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.saveStateForUndo();
        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddSupplierCommand)) {
            return false;
        }
        AddSupplierCommand otherCmd = (AddSupplierCommand) other;
        return toAdd.equals(otherCmd.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
