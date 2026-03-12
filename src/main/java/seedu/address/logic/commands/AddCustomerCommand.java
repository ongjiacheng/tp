package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Customer;

/**
 * Adds a customer contact to the address book (customers have no tags).
 */
public class AddCustomerCommand extends Command {

    public static final String COMMAND_WORD = "addc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a customer contact to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Jevin "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "j@e.com "
            + PREFIX_ADDRESS + "Blk 1";

    public static final String MESSAGE_SUCCESS = "New customer added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This customer already exists in the address book";

    public static final String MESSAGE_TAGS_NOT_ALLOWED = "Customer should not have tags";
    public static final String MESSAGE_OPENING_HOURS_NOT_ALLOWED = "Customer should not have opening hours";

    private final Customer toAdd;

    public AddCustomerCommand(Customer customer) {
        requireNonNull(customer);
        toAdd = customer;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddCustomerCommand)) {
            return false;
        }
        AddCustomerCommand otherCmd = (AddCustomerCommand) other;
        return toAdd.equals(otherCmd.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
