package src;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MenuPane {
	private Font font24 = Font.font("Arial", FontWeight.BOLD, 24);
    private Font font48 = Font.font("Arial", FontWeight.BOLD, 48);
    private Font font96 = Font.font("Arial", FontWeight.BOLD, 96);
    private Button start = new Button();
    private Button quit = new Button();
    private Text name = new Text();
    private Game game;
    private Text control = new Text();
    private Text control1 = new Text();
    private Text control2 = new Text();
    private Text control3 = new Text();

    public MenuPane(Pane menuPane, Game game) {
        start.setText("START");
        start.setFont(font48);
        start.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        quit.setText("QUIT");
        quit.setFont(font48);
        quit.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

        name.setText("Jump! JUMP! JUmp!");
        name.setFont(font96);
        
        control.setText("CONTROL");
        control1.setText("> Use A / D to move left and right!");
        control2.setText("> Press / Hold SPACEBAR to jump! (the longer you hold the further you go)");
        control3.setText("> Hold A / D while holding jump to jump to that direction");
        
        control.setFont(font48);

        control1.setFont(font24);
        control2.setFont(font24);
        control3.setFont(font24);


        VBox vbox = new VBox(20, name, start, quit, control, control1, control2, control3);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(100, 0, 0, 200));

        start.setOnAction(event -> {
            game.getAppRoot().setVisible(true);
            menuPane.getChildren().removeAll(vbox); // Remove menu elements
        });

        quit.setOnAction(event -> System.exit(0));

//        menuPane.setPrefHeight(720);
//        menuPane.setPrefWidth(1280);
        menuPane.getChildren().addAll(vbox);
        menuPane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }
}

