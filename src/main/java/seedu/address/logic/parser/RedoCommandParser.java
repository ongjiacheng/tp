package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code RedoCommand} object.
 */
public class RedoCommandParser implements Parser<RedoCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@code RedoCommand} and returns a {@code RedoCommand} object for execution.
     *
     * @param args User input arguments.
     * @return A {@code RedoCommand}.
     * @throws ParseException If any extra arguments are provided.
     */
    @Override
    public RedoCommand parse(String args) throws ParseException {
        if (!args.trim().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RedoCommand.MESSAGE_USAGE));
        }

        return new RedoCommand();
    }
}
