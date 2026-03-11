package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

public class AddSupplierCommand extends Command {

    public static final String COMMAND_WORD = "addsupplier";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a supplier contact.\n"
            + "Format: addsupplier n/NAME p/PHONE e/EMAIL a/ADDRESS\n"
            + "Example: addsupplier n/Ah Seng p/91234567 e/a@b.com a/Yishun";

    public static final String MESSAGE_SUCCESS = "New supplier added: %1$s";

    private final Person toAdd;

    public AddSupplierCommand(Person toAdd) {
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