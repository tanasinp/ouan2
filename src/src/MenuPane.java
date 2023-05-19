package src;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class MenuPane{
    private Rectangle bg = new Rectangle(1280, 720);
    private Font font = Font.font(72);
    private Button start = new Button();
    private Game game;

    public MenuPane(Pane menuPane, Game game) {
        start.setText("START");
        start.setFont(font);
        VBox vbox = new VBox(50, start);
        vbox.setTranslateX(400);
        vbox.setTranslateY(200);
        start.setOnAction(event -> {
        	game.getAppRoot().setVisible(true);
        	menuPane.getChildren().removeAll(bg, vbox); // Remove menu elements
        });
        menuPane.getChildren().addAll(bg, vbox);
        
    }
}
