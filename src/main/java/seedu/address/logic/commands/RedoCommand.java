package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Restores the address book to the next state in redo history.
 *
 * <p>Redo is available only after a successful undo, and becomes unavailable
 * once a new modifying command is executed.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Restores the next address book state after an undo.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Redo successful.";
    public static final String MESSAGE_FAILURE = "Nothing to redo.";

    /**
     * Executes the redo command.
     *
     * @param model The model which the command should operate on.
     * @return Result of the redo command.
     * @throws CommandException If there is no state to redo to.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.canRedo()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.redo();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
