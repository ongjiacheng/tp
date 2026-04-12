package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.model.tag.Tag;

public class TagCommandParserTest {

    private final TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_addTags_success() {
        TagCommand expected = new TagCommand(
                Index.fromOneBased(1),
                Set.of(new Tag("fish"), new Tag("meat")),
                Set.of(),
                false
        );

        assertParseSuccess(parser, "1 at/fish at/meat", expected);
    }

    @Test
    public void parse_deleteTags_success() {
        TagCommand expected = new TagCommand(
                Index.fromOneBased(2),
                Set.of(),
                Set.of(new Tag("fish")),
                false
        );

        assertParseSuccess(parser, "2 dt/fish", expected);
    }

    @Test
    public void parse_clearTags_success() {
        TagCommand expected = new TagCommand(
                Index.fromOneBased(3),
                Set.of(),
                Set.of(),
                true
        );

        assertParseSuccess(parser, "3 ct/", expected);
    }

    @Test
    public void parse_noAction_failure() {
        assertParseFailure(parser, "1", TagCommand.MESSAGE_NO_TAG_ACTION);
    }

    @Test
    public void parse_clearTagsConflict_failure() {
        assertParseFailure(parser, "1 ct/ at/fish", TagCommand.MESSAGE_CLEAR_TAGS_CONFLICT);
    }

    @Test
    public void parse_invalidIndex_failure() {
        // 0
        assertParseFailure(
                parser,
                "0 at/fish",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, TagCommand.MESSAGE_USAGE)
        );

        // negative index
        assertParseFailure(
                parser,
                "-1 at/fish",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT_OR_INDEX, TagCommand.MESSAGE_USAGE)
        );

    }
}
