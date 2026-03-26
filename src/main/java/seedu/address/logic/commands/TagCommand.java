package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Supplier;
import seedu.address.model.tag.Tag;

/**
 * Replaces the tags of an existing person in the address book.
 *
 * <p>The target person is identified using the index shown in the currently displayed person list.
 * The person's existing tags will be overwritten by the tags provided in the command.</p>
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Replaces the tags of the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Example: " + COMMAND_WORD + " 3 " + PREFIX_TAG + "vegetable " + PREFIX_TAG + "fruits";

    public static final String MESSAGE_SUCCESS = "Updated tags for: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final Set<Tag> tags;
    /**
     * Creates a {@code TagCommand} that replaces the tags of the person at {@code index}.
     *
     * @param index Index of the person in the filtered person list whose tags will be replaced.
     * @param tags  Tags to replace the person's existing tags with.
     */
    public TagCommand(Index index, Set<Tag> tags) {
        this.index = index;
        this.tags = tags;
    }

    /**
     * Executes the command and replaces the tags of the specified person.
     *
     * @param model The model containing the current address book data.
     * @return A {@code CommandResult} containing the result message to be shown to the user.
     * @throws CommandException If the index is invalid or if the operation results in a duplicate person.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }



        Person personToEdit = lastShownList.get(index.getZeroBased());
	Person taggedPerson;

	if (personToEdit instanceof Supplier supplier) {
            taggedPerson = new Supplier(
                    supplier.getName(),
                    supplier.getPhone(),
                    supplier.getEmail(),
                    supplier.getAddress(),
		                supplier.getRemarks(),
                    tags,
                    supplier.isFavourite(),
                    supplier.getOpeningHours(),
                    supplier.getAlternativeContact()
            );
	} else {
            taggedPerson = new Person(
                    personToEdit.getName(),
                    personToEdit.getPhone(),
                    personToEdit.getEmail(),
                    personToEdit.getAddress(),
		                personToEdit.getRemarks(),
                    tags,
                    personToEdit.isFavourite()
            );
	}

        // Same duplicate check pattern as EditCommand
        if (!personToEdit.isSamePerson(taggedPerson) && model.hasPerson(taggedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, taggedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(taggedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TagCommand)) {
            return false;
        }
        TagCommand o = (TagCommand) other;
        return index.equals(o.index) && tags.equals(o.tags);
    }
}
