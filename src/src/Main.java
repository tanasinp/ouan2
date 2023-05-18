package src;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Game game;

    public void start(Stage primaryStage) throws Exception {
        game = new Game();
        game.init(primaryStage);
        game.startGameLoop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}