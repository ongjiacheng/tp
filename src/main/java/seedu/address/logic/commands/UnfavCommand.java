package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sets a person identified as favourite using it's displayed index from the address book.
 */
public class UnfavCommand extends Command {

    public static final String COMMAND_WORD = "unfav";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes person from the favourites list\n" + "Example: " + COMMAND_WORD + " 2";

    public static final String MESSAGE_SUCCESS = "Removed from favourites: %1$s";
    public static final String MESSAGE_PERSON_NOT_FAVOURITE = "Person is not in favourites list.";

    private final Index targetIndex;

    public UnfavCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToRemoveFromFavourites = lastShownList.get(targetIndex.getZeroBased());

        if (!personToRemoveFromFavourites.isFavourite()) {
            throw new CommandException(MESSAGE_PERSON_NOT_FAVOURITE);
        }

        model.saveStateForUndo();
        model.unsetPersonAsFavourite(personToRemoveFromFavourites);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(personToRemoveFromFavourites)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnfavCommand)) {
            return false;
        }

        UnfavCommand otherUnfavCommand = (UnfavCommand) other;
        return targetIndex.equals(otherUnfavCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
