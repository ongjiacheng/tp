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
public class FavCommand extends Command {

    public static final String COMMAND_WORD = "fav";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets person as favourite and moves it to the favourites list\n" + "Example: " + COMMAND_WORD + " 2";

    public static final String MESSAGE_SUCCESS = "Set as favourite: %1$s";
    public static final String MESSAGE_PERSON_ALREADY_FAVOURITE = "Person is already in favourites list.";

    private final Index targetIndex;

    public FavCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToSetAsFavourite = lastShownList.get(targetIndex.getZeroBased());

        if (personToSetAsFavourite.isFavourite()) {
            throw new CommandException(MESSAGE_PERSON_ALREADY_FAVOURITE);
        }

        model.saveStateForUndo();
        model.setPersonAsFavourite(personToSetAsFavourite);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(personToSetAsFavourite)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FavCommand)) {
            return false;
        }

        FavCommand otherFavCommand = (FavCommand) other;
        return targetIndex.equals(otherFavCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
