package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARKS;

import java.util.Collection;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarksCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RemarksCommand object.
 */
public class RemarksCommandParser implements Parser<RemarksCommand> {

    @Override
    public RemarksCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();
        String[] parts = trimmedArgs.split("\\s+", 2);

        if (parts.length == 0 || parts[0].isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarksCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(parts[0]);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarksCommand.MESSAGE_USAGE), pe);
        }

        String remarks = "";
        if (parts.length > 1) {
            remarks = parts[1];
        }

        return new RemarksCommand(index, remarks);
    }
}
