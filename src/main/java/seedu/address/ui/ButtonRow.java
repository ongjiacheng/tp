package seedu.address.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

/**
 * Represents the row of command buttons.
 */
public class ButtonRow extends UiPart<Region> {

    private static final String FXML = "ButtonRow.fxml";

    private CommandBox commandBox;

    public ButtonRow() {
        super(FXML);
    }

    /** Set the CommandBox that this ButtonRow will control */
    public void setCommandBox(CommandBox commandBox) {
        this.commandBox = commandBox;
    }

    /**
     * Handles the button pressed event for partial commands.
     * @param event The button pressed event.
     */
    @FXML
    private void handleCommandPartial(ActionEvent event) {
        Button button = (Button) event.getSource();
        String command = (String) button.getUserData();

        commandBox.insertCommand(command);
    }

    /**
     * Handles the button pressed event for full commands.
     * @param event The button pressed event.
     */
    @FXML
    private void handleCommandFull(ActionEvent event) {
        Button button = (Button) event.getSource();
        String command = (String) button.getUserData();

        commandBox.runCommand(command);
    }
}
