package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalPersons;

public class UndoCommandTest {

    @Test
    public void execute_noUndoHistory_throwsCommandException() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        UndoCommand undoCommand = new UndoCommand();

        assertThrows(CommandException.class, UndoCommand.MESSAGE_FAILURE, () -> undoCommand.execute(model));
    }

    @Test
    public void execute_canUndo_success() throws Exception {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        model.saveStateForUndo();
        model.setAddressBook(new seedu.address.model.AddressBook());

        UndoCommand undoCommand = new UndoCommand();
        CommandResult result = undoCommand.execute(model);

        assertEquals(UndoCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
    }
}
