package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code UndoCommand} object.
 */
public class UndoCommandParser implements Parser<UndoCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@code UndoCommand} and returns an {@code UndoCommand} object for execution.
     *
     * @param args User input arguments.
     * @return An {@code UndoCommand}.
     * @throws ParseException If any extra arguments are provided.
     */
    @Override
    public UndoCommand parse(String args) throws ParseException {
        if (!args.trim().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UndoCommand.MESSAGE_USAGE));
        }

        return new UndoCommand();
    }
}
