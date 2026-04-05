package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;

public class RemarksCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        Person personToEdit = model.getFilteredPersonList().get(0);
        String newRemarks = "Updated remarks";

        RemarksCommand command =
                new RemarksCommand(Index.fromZeroBased(0), newRemarks);

        CommandResult result = command.execute(model);

        Person updatedPerson = model.getFilteredPersonList().get(0);

        // Check remarks updated
        assertEquals(newRemarks, updatedPerson.getRemarks());

        // Check message
        assertEquals(
                String.format(RemarksCommand.MESSAGE_SUCCESS, Messages.format(updatedPerson)),
                result.getFeedbackToUser()
        );
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        int invalidIndex = model.getFilteredPersonList().size();

        RemarksCommand command =
                new RemarksCommand(Index.fromZeroBased(invalidIndex), "test");

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void equals() {
        RemarksCommand first = new RemarksCommand(Index.fromOneBased(1), "remarks");
        RemarksCommand second = new RemarksCommand(Index.fromOneBased(2), "other");

        // same object
        assertEquals(true, first.equals(first));

        // same values
        RemarksCommand firstCopy = new RemarksCommand(Index.fromOneBased(1), "remarks");
        assertEquals(true, first.equals(firstCopy));

        // different index
        assertEquals(false, first.equals(second));

        // different remarks
        assertEquals(false, first.equals(new RemarksCommand(Index.fromOneBased(1), "diff")));

        // different type
        assertEquals(false, first.equals(1));

        // null
        assertEquals(false, first.equals(null));
    }
}
