---
layout: default.md
title: "User Guide"
pageNav: 3
---

# MALAddress User Guide

MALAddress is a **desktop app for managing contacts**, optimized for **fast CLI workflows** while still having the
benefits of a **GUI**. It is adapted from AddressBook Level 3 (AB3) and is designed for **hawker stall owners and stall
staff** managing **suppliers** and **regular customers**.

<!-- \* Table of Contents -->

<page-nav-print />

---

## Quick start

1. Ensure you have Java `17` or above installed in your computer.<br>
   **Mac users:** Ensure you have the precise JDK version
   prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
2. Download the latest `.jar` file from your team’s GitHub Releases page.
3. Copy the file to the folder you want to use as the *home folder* for MALAddress.
4. Open a terminal, `cd` into the folder you put the jar file in, and run:
   `java -jar maladdress.jar`<br>
   A GUI similar to the below should appear in a few seconds. Note how the app may contain sample data.<br>
   ![Ui](images/Ui.png)
5. Type the command in the command box and press Enter to execute it.<br>
   Some example commands you can try:

    * `list` : Lists all contacts.
    * `add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 t/friends` : Adds a contact.
    * `adds n/Ah Seng p/91234567 e/a@b.com a/Yishun o/0900 - 1800 t/vegetable` : Adds a supplier.
    * `find yishun vegetable` : Finds contacts matching keywords.
    * `tag 3 t/vegetable t/fruits` : Replaces tags of the 3rd contact.
    * `open` : Lists suppliers who are currently open (based on stored operating hours).
    * `remark 3 r/always late` : Adds/updates remark for the 3rd contact.
    * `fav 2` : Toggles favourite status for the 2nd contact.
    * `undo` : Undoes the most recent change.
    * `redo` : Redoes the most recently undone change.
    * `delete 3` : Deletes the 3rd contact shown in the current list.

6. Refer to the [Features](#features) below for details of each command.

---

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.
* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.
* Items with `…` after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…` can be used as ` ` (0 times), `t/friend`, `t/friend t/family` etc.
* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE`, `p/PHONE n/NAME` is also acceptable.
* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines
  as space characters surrounding line-breaks may be omitted when copied over to the application.
  </box>

### Viewing help : `help`

Brings up the help window containing the link to this guide.

Format: `help`

---

### Adding a contact: `add`

Adds a contact to MALAddress.

Format: `add n/NAME p/PHONE [e/EMAIL] [a/ADDRESS] [t/TAG]…`

Notes:

* `t/` can be used multiple times.

Examples:

* `add n/Wei Ming p/81234567 t/regular t/nochilli`

---

### Adding a supplier: `adds`

Adds a supplier to MALAddress.

Format: `adds n/NAME p/PHONE [e/EMAIL] [a/ADDRESS] o/OPENING_HOURS [t/TAG]…`

Notes:

* `t/` can be used multiple times.
* Opening hours format should be `0900 - 1800`.

Examples:

* `adds n/Ah Seng p/91234567 e/a@b.com a/Yishun o/0900 - 1800 t/vegetable`

---

### Clear contacts : `clear`

Deletes all stored contacts. This action is not reversible.

Format: `clear`

---

### Listing all contacts : `list`

Shows a list of all contacts in MALAddress.

Format: `list`

---

### Editing a contact : `edit`

Edits an existing contact in MALAddress.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]…`

Notes:

* Edits the contact at the specified `INDEX`.
* The index refers to the index number shown in the displayed contact list.
* The index **must be a positive integer** 1, 2, 3, …
* At least one optional field must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags will be replaced (i.e., tag edits are not cumulative).

Examples:

* `edit 1 p/91234567 e/johndoe@example.com`
* `edit 2 t/supplier t/seafood`

---

### Finding contacts: `find`

Finds contacts whose fields contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

Search behaviour:

* The search is case-insensitive.
* Keywords can match any of: **name, phone, email, address, tags.**
* Contacts matching at least one keyword will be returned (`OR` search).

Examples:

* `find supplier`
* `find vegetables`
* `find 9123`
* `find yishun`

---

### Tagging a contact: `tag`

Updates tags for a contact.

Format: `tag INDEX t/TAG [t/TAG]...`

Notes:

* Replaces the tags of the person at the specified `INDEX` with the provided tags.
* The index refers to the index number shown in the displayed person list.
* The index must be a positive integer 1, 2, 3, …

Examples:

* `tag 3 t/vegetable t/fruits`

---

### Listing currently available suppliers : `open`

Use this command to see all suppliers that are available at the current time.

Format: `open`

Warning: Suppliers without correctly formatted hours may not appear.

Expected Output: The contact list updates to show only currently available/open suppliers.

Examples: `open` shows all suppliers that are open now.

---

### Adding/Updating a Remark: `remark`

Use this command to add or update a remark for a contact (e.g., delivery notes, special instructions).

Format: `remark INDEX r/REMARK`

Notes:
* `INDEX` refers to the index number shown in the displayed contact list.
* To clear an existing remark, use an empty remark: `remark INDEX r/`

Expected Output: The selected contact’s remark is updated and shown in the contact card/list.

Examples:
1. `remark 3 r/always late` adds/updates the remark for the 3rd contact.
2. `remark 3 r/` clears the remark for the 3rd contact.

---

### Marking a Contact as Favourite: `fav`

Use this command to mark a contact as a favourite (or unmark it if it is already favourited).

Format: `fav INDEX`

Notes:
* `INDEX` refers to the index number shown in the displayed contact list.
* Running `fav` on the same index again toggles the favourite status.

Expected Output: The selected contact’s favourite status is updated and reflected in the UI.

Examples:
1. `fav 2` marks the 2nd contact as a favourite.
2. `fav 2` again removes it from favourites.

---

### Undoing the Last Action: `undo`

Use this command to undo the most recent change made to the address book (e.g., add, adds, delete, edit, tag, remark, fav).

Format: `undo`

Warning: If there is no previous action to undo, the command will fail.

Expected Output: The address book reverts to the previous state and the contact list updates accordingly.

Examples:
1. `delete 2` followed by `undo` restores the deleted contact.
2. `tag 1 t/fish` followed by `undo` restores the previous tags.

---

### Redoing the Last Undone Action: `redo`

Use this command to redo the most recently undone action.

Format: `redo`

Warning: If there is no undone action to redo, the command will fail.

Note: Performing a new modifying command after `undo` clears the redo history.

Expected Output: The address book reapplies the previously undone change and updates the contact list accordingly.

Examples:
1. `delete 2 → undo → redo` deletes the 2nd contact again.

---

### Deleting a contact : `delete`

Deletes the specified contact from MALAddress.

Format: `delete INDEX`

Notes:

* Deletes the contact at the specified `INDEX`.
* The index refers to the index number shown in the displayed contact list.
* The index **must be a positive integer** 1, 2, 3, …

Example:

* `delete 3`

---

### Saving the data

MALAddress data is saved automatically to the hard disk after any command that changes the data. There is no need to
save manually.

### Editing the data file

Data is saved automatically as a JSON file at `[JAR file location]/data/addressbook.json`.
Advanced users may edit the data file directly, but invalid edits may cause MALAddress to discard data and start with an
empty file on the next run.

<box type="warning" seamless>

**Caution:**
If your changes make the data file format invalid, MALAddress may discard all data and start with an empty data file at
the next run. Back up the file before editing it.
</box>

---

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app on the other computer and overwrite the empty data file it creates with the data file from your
previous MALAddress home folder.

---

## Known issues

1. **When using multiple screens**, the GUI may open off-screen after changing display configurations. Remedy: delete
   the `preferences.json` file before running the app again.
2. **If you minimize the Help Window** (if implemented as a window) and run `help` again, the help window may remain
   minimized. Remedy: restore it manually.

---

## Command summary

| Action      | Format, Examples                                                                                                                                                     |
|-------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add**     | `add n/NAME p/PHONE [e/EMAIL] [a/ADDRESS] [t/TAG]...` <br> e.g., `add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 t/friends t/owesMoney` |
| **Adds**    | `adds n/NAME p/PHONE [e/EMAIL] [a/ADDRESS] o/OPENING_HOURS [t/TAG]...`<br> e.g., `adds n/Ah Seng p/91234567 e/a@b.com a/Yishun o/0900 - 1800 t/vegetable`            |
| **List**    | `list`                                                                                                                                                               |
| **Find**    | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find yishun vegetable`                                                                                                     |
| **Edit**    | `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/TAG]...`<br> e.g., `edit 2 e/jameslee@example.com a/Blk 123 Clementi Rd`                                     |
| **Tag**     | `tag INDEX t/TAG [t/TAG]...`<br> e.g., `tag 3 t/vegetable t/fruits`                                                                                                  |
| **Open**    | `open`                                                                                                                                                               |
| **Remarks** | `remarks INDEX r/REMARK`<br> e.g., `remarks 3 r/always late`                                                                                                         |
| **Fav**     | `fav INDEX`<br> e.g., `fav 2`                                                                                                                                        |
| **Unfav**   | `unfav INDEX` <br> e.g., `unfav 2`                                                                                                                                   |
| **Undo**    | `undo`                                                                                                                                                               |
| **Redo**    | `redo`                                                                                                                                                               |
| **Delete**  | `delete INDEX`<br> e.g., `delete 3`                                                                                                                                  |
| **Clear**   | `clear`                                                                                                                                                              |
| **Help**    | `help`                                                                                                                                                               |
| **Exit**    | `exit`                                                                                                                                                               |
