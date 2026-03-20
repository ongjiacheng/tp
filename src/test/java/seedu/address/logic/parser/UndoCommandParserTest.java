package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class UndoCommandParserTest {

    private final UndoCommandParser parser = new UndoCommandParser();

    @Test
    public void parse_emptyArgs_returnsUndoCommand() throws Exception {
        assertEquals(new UndoCommand(), parser.parse(""));
    }

    @Test
    public void parse_extraArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" extra"));
    }
}
