---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# MALAdress Developer Guide

<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Acknowledgements

This project is based on AddressBook-Level3 by the SE-EDU initiative: https://se-education.org

MALAdress continues to use the following key technologies adapted from AB3:
- Java 17
- JavaFX
- Jackson
- MarkBind

--------------------------------------------------------------------------------------------------------------------

## Setting up and getting started

1. Ensure Java 17 is installed on your machine.
2. Clone the repository to your local machine.
3. Open the project in IntelliJ IDEA.
4. Allow Gradle to import the project and download dependencies.
5. Run `seedu.address.Main` or use `./gradlew run` to launch the application.
6. Run `./gradlew check` to verify that the project compiles, tests pass, and code style checks succeed.

For more detailed environment setup instructions, refer to `SettingUp.md`.

--------------------------------------------------------------------------------------------------------------------

## Design

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The Architecture Diagram above illustrates the high-level design of MALAdress.

MALAdress follows a modular layered architecture adapted from AB3.  
This separation of concerns improves maintainability, testability, and extensibility.

**Main components of the architecture**

**`Main`** (classes `Main` and `MainApp`) is responsible for app launch and shutdown.
- At app launch, it initializes the other components in the correct sequence and connects them.
- At shutdown, it shuts down components and invokes cleanup where necessary.

The bulk of the app’s work is done by the following four components:
- **UI**: the GUI of the app
- **Logic**: command parsing and execution
- **Model**: in-memory data
- **Storage**: reading and writing data to disk

**How the architecture components interact**

The sequence diagram below shows how the components interact when the user enters `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each component:
- defines its API in an interface with the same name
- implements its functionality using a `{ComponentName}Manager` class

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below describe each component in more detail.

### UI component

The UI consists of a `MainWindow` made up of components such as `CommandBox`, `ResultDisplay`, and `PersonListPanel`.  
All UI parts inherit from the abstract `UiPart` class.

The UI component:
- passes user commands to the Logic component
- listens for changes in the filtered person list so the display updates automatically
- displays `Person` objects from the Model

### Logic component

The Logic component parses user input into commands and executes them.

<puml src="diagrams/LogicClassDiagram.puml" />

The sequence diagram below illustrates interactions within Logic for the command `delete 1`.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions inside Logic for delete 1" />

How the Logic component works:
1. The UI passes a command string to `LogicManager`.
2. `LogicManager` passes the string to `AddressBookParser`.
3. `AddressBookParser` selects the correct parser and creates a `Command`.
4. `LogicManager` executes the `Command`.
5. The result is returned as a `CommandResult` to the UI.

Parser-related classes are shown below:

<puml src="diagrams/ParserClasses.puml" width="600" />

### Model component

The Model component:
- stores all contacts as `Person` objects
- stores a filtered list of contacts for UI display
- stores user preferences
- exposes operations such as add, delete, edit, tag, favourite, undo, and redo through the `Model` interface

<puml src="diagrams/ModelClassDiagram.puml" width="450" />

**Note:** If the model diagram does not render, the corresponding `ModelClassDiagram.puml` file must be corrected.

### Storage component

The Storage component:
- saves and loads address book data and user preferences in JSON format
- converts JSON-friendly representations into Model objects and vice versa
- depends on Model classes because it stores Model data

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

### Common classes

Common utility classes used by multiple components are located in `seedu.address.commons`.

--------------------------------------------------------------------------------------------------------------------

## Implementation

This section describes notable implementation details of MALAdress features.

### Add and adds features

MALAdress supports two add-related commands:

- `add` adds a general operational contact
- `adds` adds a supplier contact with opening hours

The `add` command is kept because hawker stall operations may involve useful contacts that are not suppliers, such as delivery runners, stall assistants, cleaners, maintenance contacts, or landlord-related contacts. These contacts still need to be stored, but they should not appear in supplier-specific views such as `open`.

The `adds` command is specifically for supplier contacts and supports opening hours so that the `open` command can filter suppliers who are currently available.

Names should not be blank or more than 49 Characters, and it can only contain alphanumeric characters, spaces, and the following special characters: `@`, `/`, `&`, `.`, `-`, `(`, `)`, `'`, `,`, `;`, `[`, `]`, `~`, `!`, `^`, `_`, `*`, `#`, `$`, `+`, `|`, `{`, `}`, `<`, `>`, `?`, `\`, `:`, `=`

Note: Although adding `/` is allowed in names, putting prefixes between spaces like ` p/ ` can result in multiple value error

### Tag feature

The `tag` command modifies the tags of a contact identified by index.

MALAdress supports three tag actions:
- `at/` adds tag(s)
- `dt/` deletes tag(s)
- `ct/` clears all tags

High-level behaviour:
1. The parser extracts the target index and tag action prefixes.
2. The command retrieves the target contact from the filtered list.
3. The command computes the updated tag set:
    - `at/` adds new tags to the existing set
    - `dt/` removes specified tags from the existing set
    - `ct/` clears all tags
4. A new `Person` or `Supplier` object is created with identical fields except for the updated tags.
5. The model updates the contact via `Model#setPerson(...)`.
6. The filtered list refreshes and the UI updates.
7. Each Tag has a letter limit of 20
8. 'ct/' cannot be used on Supplier at all
9. 'dt/' cannot be used on Supplier with one tag remaining
10. Each Supplier needs to have a Tag

Design notes:
- Tag modification is intentionally separated from `edit`.
- This keeps `edit` focused on core contact details while keeping tag operations explicit.
- `ct/` cannot be combined with `at/` or `dt/`.

### Open suppliers feature

The `open` command filters the displayed contact list to show suppliers that are currently open.

High-level behaviour:
1. `OpenCommand` applies `Model.PREDICATE_SHOW_ALL_OPEN`.
2. The predicate checks `person.isOpen()`.
3. The UI updates because the filtered list predicate changes.

Design notes:
- `open` does not mutate stored data.
- It only changes the current filtered view.

### Remarks feature

The `remarks` command replaces the remarks of a contact identified by index.

High-level behaviour:
1. The parser extracts the target index and `r/...` value.
2. A new `Person` or `Supplier` object is created with identical fields except for remarks.
3. The model updates the contact.
4. The UI refreshes.

### Favourite and unfavourite features

MALAdress supports two separate commands for favourite status:
- `fav` marks a contact as favourite
- `unfav` removes the favourite status from a contact

#### Favourite feature

The `fav` command marks a contact identified by index as favourite.

High-level behaviour:
1. The command retrieves the target contact by index.
2. The command creates a new contact object with favourite status set to true.
3. The model updates the contact.
4. The UI refreshes.

Design note:
- `fav` is a one-way command that marks a contact as favourite.
- It does not toggle the state.

#### Unfavourite feature

The `unfav` command removes the favourite status of a contact identified by index.

High-level behaviour:
1. The command retrieves the target contact by index.
2. The command creates a new contact object with favourite status set to false.
3. The model updates the contact.
4. The UI refreshes.

Design note:
- `unfav` is a separate one-way command that removes favourite status.

### Undo/redo feature

MALAdress supports undo/redo for data-changing commands within the current app session.

High-level behaviour:
- When a data-changing command executes successfully, the previous in-memory state is saved into undo history.
- When `undo` is executed, the current state is replaced with the most recent saved state.
- When `redo` is executed, the most recently undone state is re-applied.

Supported data-changing commands include:
- `add`
- `adds`
- `edit`
- `delete`
- `clear`
- `tag`
- `remarks`
- `fav`
- `unfav`

Design notes:
- Undo/redo history is session-based only.
- Undo/redo history is not persisted across app restarts.
- If the user closes and reopens the application, previously undoable and redoable states are lost.
- Only successful data-changing commands are stored in history.
- View-only commands such as `list`, `find`, and `open` do not create undo/redo history entries.

--------------------------------------------------------------------------------------------------------------------

## Documentation, logging, testing, configuration, dev-ops

- Documentation guide: `Documentation.md`
- Testing guide: `Testing.md`
- Logging guide: `Logging.md`
- Configuration guide: `Configuration.md`
- DevOps guide: `DevOps.md`

--------------------------------------------------------------------------------------------------------------------

## Appendix: Requirements

### Product scope

#### Target user profile

MALAdress is designed for hawker stall owners and stall assistants who need to manage supplier and operational contacts efficiently during daily operations.

These users typically:
- prefer fast keyboard-based workflows
- need quick access to supplier availability
- coordinate with multiple suppliers during peak hours
- need to record short operational notes about contacts

#### Value proposition

MALAdress helps users manage supplier contacts efficiently during daily operations by enabling quick keyboard-based access to contact details, checking supplier availability before contacting them, and reducing the risk of stock shortages through faster and more reliable contact management.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a ... | I want to ... | So that I can ... |
|----------|----------|---------------|-------------------|
| `* * *` | hawker stall staff | add supplier contacts quickly | store contact details for daily operations |
| `* * *` | hawker stall staff | add non-supplier operational contacts | keep useful business contacts in one place |
| `* * *` | hawker stall staff | find contacts using keywords, not just names | locate the correct contact quickly during peak hours |
| `* * *` | hawker stall staff | tag suppliers with ingredient categories | remember who supplies what |
| `* * *` | hawker stall staff | check which suppliers are open now | contact only suppliers who are currently available |
| `* * *` | hawker stall staff | edit contact information | keep stored details accurate |
| `* * *` | hawker stall staff | delete outdated contacts | remove contacts that are no longer relevant |
| `* *` | hawker stall staff | add remarks to contacts | store operational notes such as reliability or delivery preferences |
| `* *` | hawker stall staff | favourite important contacts | identify key contacts quickly |
| `* *` | hawker stall staff | undo my last change | recover from accidental mistakes |
| `* *` | hawker stall staff | redo my last undone change | recover if I undo something by mistake |
| `* *` | hawker stall staff | see time left before a supplier closes | decide quickly whether I should contact them now |
| `*` | hawker stall staff | use UI helper buttons to autofill command formats | reduce typing and format mistakes |
| `*` | hawker stall staff | track order history | refer to previous supplier interactions if needed |

### Use cases

For all use cases below, the system is `MALAdress`.

#### Use case: Add a supplier contact (`adds`)

**Actor:** hawker stall staff

**Preconditions:** App is running and the command box is available.

**Guarantees:** A supplier contact is stored if the input is valid and not a duplicate.

**Main Success Scenario**
1. User enters the `adds` command with the required supplier fields.
2. System validates the input.
3. System stores the supplier contact.
4. System displays a success message.
5. System updates the contact list.

**Extensions**
- 2a. Opening hours format is invalid.
    - 2a1. System shows an error message with an example of the correct format.
- 2b. The contact duplicates an existing contact.
    - 2b1. System rejects the command and shows a duplicate-contact error.

**Use case ends.**

#### Use case: Add a general operational contact (`add`)

**Actor:** hawker stall staff

**Preconditions:** App is running and the command box is available.

**Guarantees:** A non-supplier operational contact is stored if the input is valid and not a duplicate.

**Main Success Scenario**
1. User enters the `add` command with the required fields.
2. System validates the input.
3. System stores the contact.
4. System displays a success message.
5. System updates the contact list.

**Extensions**
- 2a. Required fields are missing or malformed.
    - 2a1. System shows an invalid-format error.
- 2b. The contact duplicates an existing contact.
    - 2b1. System rejects the command and shows a duplicate-contact error.

**Use case ends.**

#### Use case: Modify tags of a contact (`tag`)

**Actor:** hawker stall staff

**Preconditions:** Target contact exists in the current displayed list.

**Guarantees:** The selected contact’s tags are modified if the input is valid.

**Main Success Scenario**
1. User enters `tag INDEX` with one or more tag actions.
2. System validates the index and tag inputs.
3. System updates the contact’s tags according to the action:
    - adds tags for `at/`
    - deletes tags for `dt/`
    - clears all tags for `ct/`
4. System displays a success message.
5. System updates the displayed list.

**Extensions**
- 2a. Index is invalid.
    - 2a1. System shows an invalid-index error.
- 2b. No tag action is provided.
    - 2b1. System shows an invalid-format error.
- 2c. `ct/` is used together with `at/` or `dt/`.
    - 2c1. System shows an error indicating the combination is not allowed.
- 2d. A tag specified with `dt/` does not exist on the contact.
    - 2d1. System shows an error indicating that the tag cannot be deleted.

**Use case ends.**

#### Use case: List open suppliers (`open`)

**Actor:** hawker stall staff

**Preconditions:** Supplier contacts with valid opening hours exist.

**Guarantees:** The displayed list is filtered to show currently open suppliers.

**Main Success Scenario**
1. User enters `open`.
2. System filters the list to contacts whose `isOpen()` returns true.
3. System updates the displayed contact list.

**Extensions**
- 2a. No suppliers are currently open.
    - 2a1. System displays an empty filtered list.

**Use case ends.**

#### Use case: Mark a contact as favourite (`fav`)

**Actor:** hawker stall staff

**Preconditions:** Target contact exists in the current displayed list.

**Guarantees:** The selected contact is marked as favourite.

**Main Success Scenario**
1. User enters `fav INDEX`.
2. System validates the index.
3. System marks the contact as favourite.
4. System displays a success message.
5. System updates the contact list.

**Extensions**
- 2a. Index is invalid.
    - 2a1. System shows an invalid-index error.

**Use case ends.**

#### Use case: Remove favourite status from a contact (`unfav`)

**Actor:** hawker stall staff

**Preconditions:** Target contact exists in the current displayed list.

**Guarantees:** The selected contact is no longer marked as favourite.

**Main Success Scenario**
1. User enters `unfav INDEX`.
2. System validates the index.
3. System removes the contact’s favourite status.
4. System displays a success message.
5. System updates the contact list.

**Extensions**
- 2a. Index is invalid.
    - 2a1. System shows an invalid-index error.

**Use case ends.**

#### Use case: Undo a previous data-changing command (`undo`)

**Actor:** hawker stall staff

**Preconditions:** There is at least one undoable state in history.

**Guarantees:** The most recent data-changing command is reverted.

**Main Success Scenario**
1. User enters `undo`.
2. System restores the previous saved state.
3. System displays a success message.
4. System updates the contact list.

**Extensions**
- 2a. There is no undoable state.
    - 2a1. System shows an error message indicating that there is nothing to undo.

**Use case ends.**

### Non-Functional Requirements

1. The app should work on Windows, macOS, and Linux systems with Java 17 installed.
2. The app should be able to handle at least 1000 contacts without noticeable delay for common commands such as `add`, `adds`, `find`, `tag`, and `open`.
3. Common tasks such as `adds`, `find`, `tag`, and `open` should be completable in a single command without requiring extra UI screens.
4. On invalid input, the app must not modify stored data.
5. For invalid user input, the app must display an error message that identifies at least one specific cause of failure
   (e.g. invalid index, duplicate contact, missing required prefix, or invalid time format).  
   If the error is caused by malformed command syntax, the message must also include the expected command format or an example.
6. Stored data should persist correctly across app restarts as long as the JSON file is not manually corrupted.
7. The app should remain usable in a terminal-and-window workflow without requiring mouse interaction for core tasks.

### Glossary

- **Mainstream OS**: Windows, Linux, Unix, macOS
- **Hawker stall**: A small food business operating from a stall in a hawker centre or food court
- **Supplier**: A contact that provides ingredients or services to the stall
- **Tag**: A short label used to classify contacts
- **Remarks**: A short note attached to a contact for operational information
- **Opening hours**: Supplier availability window in the format `HHmm - HHmm`
- **Open supplier**: A supplier whose opening hours include the current time
- **Favourite**: A contact marked as important
- **Undo/Redo**: Commands that revert or re-apply the most recent data-changing action

--------------------------------------------------------------------------------------------------------------------

## Appendix: Instructions for manual testing

<box type="info" seamless>

These instructions provide a starting point for testers. Testers are expected to do more exploratory testing.

</box>

### Launch and shutdown

1. Download the jar file and copy it into an empty folder.
2. Run `java -jar maladress.jar`.
   Expected: The GUI appears with sample data if no data file exists yet.

### Adding a supplier

1. Test case:  
   `adds n/Ah Seng p/91234567 e/a@b.com a/Yishun o/0900 - 1800 t/vegetable`  
   Expected: Supplier appears in the list and a success message is shown.

2. Test case with invalid opening hours:
   `adds n/Test p/91234567 e/t@t.com a/Yishun o/0900-1800 t/veg`  
   Expected: Command fails with an error message showing the correct format example.

### Tagging a contact

1. Prerequisite: list contacts.
2. Test case:
   `tag 1 at/vegetable at/fruits`  
   Expected: both tags are added to contact 1.
3. Test case:  
   `tag 1 dt/fruits`  
   Expected: the `fruits` tag is removed from contact 1.
4. Test case:
   `tag 1 ct/`
   Expected: all tags are removed from contact 1.
5. Test case:
   `tag 1 ct/ at/fish`
   Expected: command fails because `ct/` cannot be combined with `at/`.

### Open suppliers

1. Prerequisite: suppliers have valid opening hours.
2. Test case:
   `open`  
   Expected: The list shows only suppliers that are open now.

### Favourite

1. Prerequisite: list contacts.
2. Test case: run `fav 1`
   Expected: the first contact is marked as favourite.

### Unfavourite

1. Prerequisite: at least one contact is already favourited.
2. Test case: run `unfav 1`
   Expected: the first contact is no longer marked as favourite.

### Delete

1. Prerequisite: list contacts.
2. Test case:
   `delete 1`
   Expected: The first contact is removed and a success message is shown.

### Clear
1. Test case:
   `clear`
   Expected: List becomes empty; success message shown.

### Undo and Redo
1. Prerequisite: at least one contact exists.
2. Test case: run `delete 1` then `undo`
   Expected: deleted contact reappears; success message shown.
3. Test case: run `undo` then `redo`
   Expected: deletion is re-applied; contact removed again.
4. Test case: run `undo` with nothing to undo
   Expected: error message shown; data unchanged.

## Appendix: Planned Enhancement
1. Allow closing hours to be set beyond midnight
2. Implement data backup and restoration options
