package chiochat;

import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private ChioChat chioChat;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/DaUser.png"));
    private final Image botImage = new Image(this.getClass().getResourceAsStream("/DaBot.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duke instance */
    public void setBot(ChioChat d) {
        chioChat = d;

        dialogContainer.getChildren().add(
            DialogBox.getBotDialog("Hello! I'm ChioChat\nWhat can I do for you?", botImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        try {
            if ("bye".equalsIgnoreCase(userText.trim())) {
                Platform.exit();
                return;
            }
            String chioChatText = chioChat.getResponse(userInput.getText());
            Stream.of(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getBotDialog(chioChatText, botImage)
            ).forEach(dialog -> dialogContainer.getChildren().add(dialog));
            
            userInput.clear();
        } catch (ChioChatException.EmptyInput e) {
            userInput.clear();
        }
    }
}

