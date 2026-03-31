package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalSuppliers.ALI;
import static seedu.address.testutil.TypicalSuppliers.BEN;
import static seedu.address.testutil.TypicalSuppliers.DENNIS;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.SupplierBuilder;

public class SupplierTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Supplier supplier = new SupplierBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> supplier.getTags().remove(0));
    }

    @Test
    public void isSameSupplier() {
        // same object -> returns true
        assertTrue(ALI.isSameSupplier(ALI));

        // null -> returns false
        assertFalse(ALI.isSameSupplier(null));

        // same name, all other attributes different -> returns true
        Supplier editedAli = new SupplierBuilder(ALI).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALI.isSameSupplier(editedAli));

        // different name, all other attributes same -> returns false
        editedAli = new SupplierBuilder(ALI).withName(VALID_NAME_BOB).build();
        assertFalse(ALI.isSameSupplier(editedAli));

        // name differs in case, all other attributes same -> returns false
        Supplier editedBob = new SupplierBuilder(BEN).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BEN.isSameSupplier(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new SupplierBuilder(BEN).withName(nameWithTrailingSpaces).build();
        assertFalse(BEN.isSameSupplier(editedBob));
    }

    @Test
    public void getPersonType() {
        assertEquals("Supplier", ALI.getPersonType());
    }

    @Test
    public void isOpen() {
        assertTrue(ALI.isOpen());
        assertTrue(ALI.isOpen(LocalTime.of(1, 0)));
        assertTrue(DENNIS.isOpen(LocalTime.of(22, 12)));
        assertTrue(DENNIS.isOpen(LocalTime.of(1, 0)));
        assertFalse(BEN.isOpen(LocalTime.of(6, 59)));
        assertFalse(BEN.isOpen(LocalTime.of(23, 1)));
        assertFalse(BEN.isOpen(LocalTime.of(6, 0)));
    }

    @Test
    public void timeLeft() {
        assertNotNull(ALI.getTimeLeft());
    }

    @Test
    public void equals() {
        // same values -> returns true
        Supplier aliCopy = new SupplierBuilder(ALI).build();
        assertTrue(ALI.equals(aliCopy));

        // same object -> returns true
        assertTrue(ALI.equals(ALI));

        // null -> returns false
        assertFalse(ALI.equals(null));

        // different type -> returns false
        assertFalse(ALI.equals(5));

        // different Supplier -> returns false
        assertFalse(ALI.equals(BEN));

        // different name -> returns false
        Supplier editedAli = new SupplierBuilder(ALI).withName(VALID_NAME_BOB).build();
        assertFalse(ALI.equals(editedAli));

        // different phone -> returns false
        editedAli = new SupplierBuilder(ALI).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALI.equals(editedAli));

        // different email -> returns false
        editedAli = new SupplierBuilder(ALI).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALI.equals(editedAli));

        // different address -> returns false
        editedAli = new SupplierBuilder(ALI).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALI.equals(editedAli));

        // different tags -> returns false
        editedAli = new SupplierBuilder(ALI).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALI.equals(editedAli));
    }

    @Test
    public void toStringMethod() {
        String expected = Supplier.class.getCanonicalName() + "{name=" + ALI.getName() + ", phone=" + ALI.getPhone()
                + ", email=" + ALI.getEmail() + ", address=" + ALI.getAddress() + ", tags=" + ALI.getTags() + "}";
        assertEquals(expected, ALI.toString());
    }
}
