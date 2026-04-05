---
layout: default.md
title: "Developer Guide"
pageNav: 3
---

# MALAdress Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Acknowledgements

This project is based on the AddressBook-Level3 project created by the SE-EDU initiative: https://se-education.org

MALAddress continues to use the following key technologies from AB3:
- Java 17
- JavaFX (GUI)
- Jackson (JSON storage)
- MarkBind (project website and docs)

--------------------------------------------------------------------------------------------------------------------

## Setting up, getting started

Refer to the guide _Setting up and getting started_ (SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## Design

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The Architecture Diagram above illustrates the high-level design of MALAddress, which follows a modular, layered architecture adapted from AB3. This separation of concerns improves maintainability, testability, and extensibility of the system.
**Main components of the architecture**

**Main** (classes `Main` and `MainApp`) is in charge of app launch and shut down.
- At app launch, it initializes the other components in the correct sequence and connects them.
- At shut down, it shuts down components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:
- **UI**: the GUI of the app
- **Logic**: command parsing and execution
- **Model**: in-memory data of the app
- **Storage**: reads/writes data to disk

**How the architecture components interact with each other**

The sequence diagram below shows how components interact when a user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each component:
- defines an API in an interface with the same name
- implements functionality using a concrete `{ComponentName}Manager` class

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The UI consists of a `MainWindow` made up of parts such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, etc.
All UI parts inherit from the abstract `UiPart` class.

The UI component:
- executes user commands using the Logic component
- listens for changes to Model data so that UI updates automatically
- displays `Person` objects from the Model

### Logic component

The Logic component is responsible for parsing user input into commands and executing them.

<puml src="diagrams/LogicClassDiagram.puml"/>

The sequence diagram below illustrates the interactions within Logic for the command `delete 1`.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

How the Logic component works:
1. Logic receives a command string from the UI.
2. It passes the command string to `AddressBookParser` which selects the correct `XYZCommandParser`.
3. The parser creates a `Command` object (`XYZCommand`).
4. `LogicManager` executes the `Command`.
5. The result is returned as a `CommandResult` and shown in ResultDisplay.

Parser-related classes are shown here:

<puml src="diagrams/ParserClasses.puml" width="600"/>

### Model component

The Model component:
- stores all `Person` objects (contacts) in memory
- stores a filtered list of contacts (`ObservableList<Person>`) for UI display
- stores user preferences

<puml src="diagrams/ModelClassDiagram.puml" width="450" />

### Storage component

The Storage component:
- saves/loads address book data and user preferences in JSON format
- depends on Model classes because it saves/retrieves Model objects

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

### Common classes

Shared classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## Implementation

This section describes noteworthy implementation details for MALAddress-specific features.

### Tag feature

The `tag` command replaces the tags of a contact identified by an index from the current list.

High-level behaviour:
1. Parser extracts the index and tag values (`t/...`).
2. Command retrieves the target `Person` from the filtered list.
3. A new `Person` (or `Supplier`, if the existing contact is a supplier-type object) is created with identical fields except tags.
4. Model updates the contact via `Model#setPerson(...)`.
5. UI refreshes because the filtered list is updated.

Design notes:
- Tagging is separated from `edit` to keep tagging behaviour consistent and easy to discover.
- Tags are a lightweight way to represent categories such as ingredients and relationships.

### Open suppliers feature

The `open` command filters the displayed contact list to show only suppliers that are currently open (“open now”).

High-level behaviour:
1. `OpenCommand` applies `Model.PREDICATE_SHOW_ALL_OPEN`.
2. The predicate calls `person.isOpen()` to decide whether a person is currently open.
3. The UI list updates automatically because the filtered list predicate changes.

Design notes:
- This keeps the command simple: it does not mutate data, it only changes the view.

### Remarks feature

The `remarks` command replaces the remarks of a contact identified by index.

High-level behaviour:
1. Parser extracts the index and remarks (`r/...`).
2. A new `Person` (or `Supplier`) is created with identical fields except remarks.
3. Model updates the contact via `Model#setPerson(...)`.
4. UI refreshes due to filtered list update.

### Favourite feature

The `fav` command toggles the favourite state of a contact by index.

High-level behaviour:
1. Command retrieves the target contact by index.
2. Command creates a new contact object with favourite state toggled.
3. Model updates the contact.
4. UI refreshes.

### Undo/redo feature

MALAddress supports undo/redo for data-changing commands.

High-level behaviour:
- When a data-changing command executes (e.g., add, adds, edit, delete, clear, tag, remarks, fav):
    1. The system saves the previous data state (snapshot) into a cache file (e.g., `cacheData.json`).
- When `undo` is executed:
    1. Current data is replaced with the cached previous data.
    2. The current data is stored as the redo state so `redo` can re-apply it.
- When `redo` is executed:
    1. The most recently undone state is re-applied.

Design note:
- This approach prioritizes simplicity and correctness over memory optimisation.
- Only successful commands save state; failed commands should not affect undo/redo history.

--------------------------------------------------------------------------------------------------------------------

## Documentation, logging, testing, configuration, dev-ops

- Documentation guide (Documentation.md)
- Testing guide (Testing.md)
- Logging guide (Logging.md)
- Configuration guide (Configuration.md)
- DevOps guide (DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## Appendix: Requirements

### Product scope

#### Target user profile
MALAddress is designed for hawker stall owners and stall assistants who need to manage supplier contacts efficiently during daily operations.
These users typically:
- prefer fast, keyboard-based workflows
- need quick access to supplier availability
- coordinate frequently with multiple suppliers during peak hours
- need to note down various information of people

#### Value proposition
MALAddress helps users manage supplier contacts efficiently during daily operations by enabling quick keyboard-based access to contact details, checking supplier availability before contacting to prevent disturbances during off working hours, and reducing the risk of stock shortages through faster, more reliable contact management.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​              | I want to …​                                         | So that I can…​                                                     |
|----------|----------------------|------------------------------------------------------|---------------------------------------------------------------------|
| `* * *`  | hawker stall staff   | add supplier contacts quickly                         | store contact details for daily operations                          |
| `* * *`  | hawker stall staff   | find suppliers using keywords (not only names)        | locate suppliers quickly during peak hours                          |
| `* * *`  | hawker stall staff   | tag suppliers with ingredient categories              | keep track of who supplies what                                     |
| `* * *`  | hawker stall staff   | check which suppliers are open now                    | contact suppliers who are currently available                       |
| `* * *`  | hawker stall staff   | update supplier contact details                       | keep contact information accurate                                   |
| `* * *`  | hawker stall staff   | delete outdated suppliers                             | remove contacts that are no longer relevant                         |
| `* *`    | hawker stall staff   | add remarks to a supplier                             | store operational notes (e.g., “always late”)                       |
| `* *`    | hawker stall staff   | favourite important suppliers                         | access key suppliers more easily                                    |
| `* *`    | hawker stall staff   | undo my last change                                  | recover from accidental mistakes                                    |
| `* *`    | hawker stall staff   | redo my last undone change                           | recover if I undo by mistake                                        |
| `* *`    | hawker stall staff   | see time left before a supplier closes               | decide quickly whether I should contact them now                    |
| `*`      | hawker stall staff   | use UI helper buttons to autofill command formats     | reduce typing and format mistakes                                   |
| `*`      | hawker stall staff   | track order history                                  | remember previous supplier orders if needed                          |

### Use cases

(For all use cases below, the System is `MALAddress`.)

#### Use case: Add a supplier contact (`adds`)

**Actor:** hawker stall staff

**Preconditions:** App is running; user is at the command box.

**Guarantees:** Supplier contact is added if the input is valid and not a duplicate.

**Main Success Scenario (MSS):**
1. User enters the `adds` command with required fields.
2. System validates the input.
3. System saves the supplier.
4. System shows a success message and updates the contact list.

**Extensions:**
- 2a. Invalid opening hours format
  2a1. System shows an error message with an example and does not add.
- 3a. Duplicate contact
  3a1. System rejects add and shows duplicate error.

#### Use case: Tag a supplier (`tag`)

**Actor:** hawker stall staff

**Preconditions:** Contact exists and is visible in the current list.

**Guarantees:** Tags for the selected contact are replaced if input is valid.

**MSS:**
1. User enters `tag INDEX t/TAG [t/TAG]...`
2. System validates the index and tag(s).
3. System replaces the contact’s tags.
4. System shows a success message and updates the list.

**Extensions:**
- 2a. Invalid index
  2a1. System shows invalid index error.
- 2b. Missing tag values
  2b1. System shows invalid format error with usage.

#### Use case: List open suppliers (`open`)

**Actor:** hawker stall staff

**Preconditions:** Suppliers have correctly formatted opening hours.

**Guarantees:** Display is filtered to open suppliers. 

**MSS:**
1. User enters `open`.
2. System filters the list to suppliers whose opening hours include current time.
3. System shows a success message and updates the list.

### Non-Functional Requirements

1. Should work on any mainstream OS as long as it has Java 17 or above installed.
2. Should be able to hold up to 1000 contacts without noticeable sluggishness for typical usage.
3. Common tasks (adds/find/tag/open) should be completable using one command without multiple UI screens.
4. Error messages must clearly state what went wrong and what format is expected.
5. On invalid input, the app must not crash, corrupt data, or modify stored data.

### Glossary

- **Mainstream OS**: Windows, Linux, Unix, macOS
- **Hawker stall**: A small food business operating from a stall in a hawker centre/food court, common in Singapore/Malaysia
- **Supplier**: A contact that provides ingredients/services to the stall
- **Tag**: A short label used to classify contacts (e.g., “vegetable”, “seafood”, “friend”)
- **Ingredient tag**: A tag describing what the supplier sells (e.g., “fish”, “meat”)
- **Relationship tag**: A tag describing the relationship category (e.g., “supplier”)
- **Remarks**: A short note attached to a contact for operational info (e.g., “always late”)
- **Opening hours**: Supplier availability window in format `HHmm - HHmm`
- **Open supplier (open now)**: A supplier whose opening hours include the current time
- **Favourite**: A contact marked as important (shown with a favourite indicator)
- **Undo/Redo**: Commands that revert or re-apply the most recent change(s)

--------------------------------------------------------------------------------------------------------------------

## Appendix: Instructions for manual testing

<box type="info" seamless>

These instructions provide a starting point for testers. Testers are expected to do additional exploratory testing.

</box>

### Launch and shutdown
1. Download the jar file and copy it into an empty folder.
2. Run `java -jar maladdress.jar`.
   Expected: GUI appears with sample data (if no data file exists yet).

### Adding a supplier
1. Test case:
   `adds n/Ah Seng p/91234567 e/a@b.com a/Yishun o/0900 - 1800 t/vegetable`
   Expected: Supplier appears in list and success message shown.

2. Test case (invalid opening hours):
   `adds n/Test p/91234567 e/t@t.com a/Yishun o/0900-1800 t/veg`
   Expected: Command fails with error message showing correct format example.

### Tagging a supplier
1. Prerequisite: list contacts.
2. Test case:
   `tag 1 t/vegetable t/fruits`
   Expected: Tags replaced and shown in contact card.

### Open suppliers
1. Prerequisite: suppliers have valid opening hours.
2. Test case:
   `open`
   Expected: List shows only suppliers that are open now.

### Delete
1. Prerequisite: list contacts.
2. Test case:
   `delete 1`
   Expected: Contact removed; success message shown.

### Clear
1. Test case:
   `clear`
   Expected: List becomes empty; success message shown.
