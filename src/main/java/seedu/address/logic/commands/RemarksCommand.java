package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARKS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Supplier;

/**
 * Replaces the remarks of an existing person in the address book.
 *
 * <p>The target person is identified using the index shown in the currently displayed person list.
 * The person's existing remarks will be overwritten by the remarks provided in the command.</p>
 */
public class RemarksCommand extends Command {

    public static final String COMMAND_WORD = "remarks";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Replaces the remarks of the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARKS + "REMARKS\n"
            + "Example: " + COMMAND_WORD + " 3 " + PREFIX_REMARKS + "always late";

    public static final String MESSAGE_SUCCESS = "Updated remarks for: %1$s";

    private final Index index;
    private final String remarks;

    /**
     * Creates a {@code RemarksCommand} that replaces the remarks of the person at {@code index}.
     *
     * @param index Index of the person in the filtered person list whose remarks will be replaced.
     * @param remarks Remarks to replace the person's existing remarks with.
     */
    public RemarksCommand(Index index, String remarks) {
        requireNonNull(index);
        requireNonNull(remarks);
        this.index = index;
        this.remarks = remarks;
    }

    /**
     * Executes the command and replaces the remarks of the specified person.
     *
     * @param model The model containing the current address book data.
     * @return A {@code CommandResult} containing the result message to be shown to the user.
     * @throws CommandException If the index is invalid.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

		model.saveStateForUndo();
        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person remarkedPerson = createRemarkedPerson(personToEdit, remarks);

        // If you have undo/redo, keep this (otherwise remove)
        // model.saveStateForUndo();

        model.setPerson(personToEdit, remarkedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(remarkedPerson)));
    }

    /**
     * Creates a new {@code Person} (or {@code Supplier}) with the updated remarks,
     * preserving all other fields including favourite status.
     */
    private static Person createRemarkedPerson(Person personToEdit, String remarks) {
        if (personToEdit instanceof Supplier supplier) {
            return new Supplier(
                    supplier.getName(),
                    supplier.getPhone(),
                    supplier.getEmail(),
                    supplier.getAddress(),
                    remarks,
                    supplier.getTags(),
                    supplier.isFavourite(),
                    supplier.getOpeningHours(),
                    supplier.getAlternativeContact()
            );
        }

        return new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                remarks,
                personToEdit.getTags(),
                personToEdit.isFavourite()
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RemarksCommand)) {
            return false;
        }
        RemarksCommand o = (RemarksCommand) other;
        return index.equals(o.index) && remarks.equals(o.remarks);
    }
}
