package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

public class AddCustomerCommand extends Command {

    public static final String COMMAND_WORD = "addcustomer";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a customer contact.\n"
            + "Format: addcustomer n/NAME p/PHONE e/EMAIL a/ADDRESS\n"
            + "Example: addcustomer n/Jevin p/91234567 e/j@e.com a/Blk 1";

    public static final String MESSAGE_SUCCESS = "New customer added: %1$s";

    private final Person toAdd;

    public AddCustomerCommand(Person toAdd) {
        requireNonNull(toAdd);
        this.toAdd = toAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
}