package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARKS;

import java.util.Collection;

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

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARKS);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarksCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_REMARKS);
        Collection<String> remarksCollection = argMultimap.getAllValues(PREFIX_REMARKS);
	String remarks = remarksCollection.isEmpty()
	? ""
	: remarksCollection.iterator().next();

        return new RemarksCommand(index, remarks);
    }
}
