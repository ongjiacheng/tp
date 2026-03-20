package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class RedoCommandParserTest {

    private final RedoCommandParser parser = new RedoCommandParser();

    @Test
    public void parse_emptyArgs_returnsRedoCommand() throws Exception {
        assertEquals(new RedoCommand(), parser.parse(""));
    }

    @Test
    public void parse_extraArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" extra"));
    }
}
