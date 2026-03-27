package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OPENING_HOURS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code EditCommand} object.
 * Tags are not editable via {@code edit} (use {@code tag} command instead).
 */
public class EditCommandParser implements Parser<EditCommand> {

    private static final Pattern OPENING_HOURS_PATTERN = Pattern.compile("^\\d{4}\\s-\\s\\d{4}$");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HHmm");

    /**
     * Parses the given {@code String} of arguments in the context of the {@code EditCommand}
     * and returns an {@code EditCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_OPENING_HOURS, PREFIX_TAG
        );

        // Disallow tags in edit (must use tag command)
        if (!argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            throw new ParseException(EditCommand.MESSAGE_TAG_NOT_ALLOWED);
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        // All these prefixes are single-valued for edit
        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_OPENING_HOURS
        );

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_OPENING_HOURS).isPresent()) {
            String openingHours = argMultimap.getValue(PREFIX_OPENING_HOURS).get().trim();
            validateOpeningHours(openingHours);
            editPersonDescriptor.setOpeningHours(openingHours);
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    private static void validateOpeningHours(String openingHours) throws ParseException {
        // Strict format check: requires spaces around '-'
        if (!OPENING_HOURS_PATTERN.matcher(openingHours).matches()) {
            throw new ParseException(EditCommand.MESSAGE_INCORRECT_TIME_FORMAT);
        }

        // Validate actual times (e.g., rejects 2560 - 1800)
        try {
            String[] parts = openingHours.split(" - ");
            LocalTime.parse(parts[0], TIME_FORMAT);
            LocalTime.parse(parts[1], TIME_FORMAT);
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException ex) {
            throw new ParseException(EditCommand.MESSAGE_INCORRECT_TIME_FORMAT);
        }
    }
}
