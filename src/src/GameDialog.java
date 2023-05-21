package src;

import entity.Player;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GameDialog {
    private StackPane dialogPane;

    public GameDialog(Player player) {	//when player intersect with coin
        dialogPane = new StackPane();
        dialogPane.setAlignment(Pos.CENTER);
        dialogPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);

        Text message = new Text("CONGRATULATIONS!");	//set message, font, color
        message.setFont(Font.font("Arial", FontWeight.BOLD, 96));
        message.setFill(Color.WHITE);
        message.setEffect(new DropShadow(10, Color.BLACK));

        contentBox.getChildren().addAll(message);
        dialogPane.getChildren().add(contentBox);
    }

    public void open(Pane congratRoot) {
        dialogPane.setTranslateX(160);	//set position
        dialogPane.setTranslateY(300);
        congratRoot.getChildren().add(dialogPane);
    }
}

