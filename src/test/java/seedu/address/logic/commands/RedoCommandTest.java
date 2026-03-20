package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalPersons;

public class RedoCommandTest {

    @Test
    public void execute_noRedoHistory_throwsCommandException() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        RedoCommand redoCommand = new RedoCommand();

        assertThrows(CommandException.class, RedoCommand.MESSAGE_FAILURE, () -> redoCommand.execute(model));
    }

    @Test
    public void execute_canRedo_success() throws Exception {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        model.saveStateForUndo();
        model.setAddressBook(new seedu.address.model.AddressBook());
        model.undo();

        RedoCommand redoCommand = new RedoCommand();
        CommandResult result = redoCommand.execute(model);

        assertEquals(RedoCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
    }
}
