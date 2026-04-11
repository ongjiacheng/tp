package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void constructor_tooLongTag_throwsIllegalArgumentException() {
        String tooLongTag = "abcdefghijklmnopqrstu"; // 21 chars
        assertThrows(IllegalArgumentException.class, () -> new Tag(tooLongTag));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag names
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName(" ")); // spaces only
        assertFalse(Tag.isValidTagName("^")); // only non-alphanumeric characters
        assertFalse(Tag.isValidTagName("fish!")); // contains non-alphanumeric characters
        assertFalse(Tag.isValidTagName("abcdefghijklmnopqrstu")); // too long

        // valid tag names
        assertTrue(Tag.isValidTagName("fish"));
        assertTrue(Tag.isValidTagName("vegetable123"));
        assertTrue(Tag.isValidTagName("abc123"));
        assertTrue(Tag.isValidTagName("abcdefghijklmnopqrst")); // exactly 20 chars allowed
    }
}
