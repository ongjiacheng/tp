package seedu.address.logic.parser;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddSupplierCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class AddSupplierCommandParserTest {

    private final AddSupplierCommandParser parser = new AddSupplierCommandParser();

    @Test
    public void parse_invalidOpeningHours_throwsParseException() {
        String userInput = " n/Ah Seng"
                + " p/91234567"
                + " e/a@b.com"
                + " a/Yishun"
                + " o/2500 - 1800"
                + " t/vegetable";

        assertThrows(ParseException.class,
                AddSupplierCommand.MESSAGE_INCORRECT_TIME_FORMAT
                        + "\n"
                        + AddSupplierCommand.MESSAGE_USAGE,
                () -> parser.parse(userInput));
    }
}
