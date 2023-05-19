package src;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameDialog {
    private StackPane dialogPane;

    public GameDialog() {
        dialogPane = new StackPane();
        dialogPane.setAlignment(Pos.CENTER);
        dialogPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

        Text message = new Text("CONGRATULATIONS!");
        message.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        message.setFill(Color.WHITE);
        message.setEffect(new DropShadow(10, Color.BLACK));

        dialogPane.getChildren().add(message);
    }

    public void open(Pane congratRoot) {
        dialogPane.setTranslateX(400);
        dialogPane.setTranslateY(300);
        congratRoot.getChildren().add(dialogPane);
    }
}
