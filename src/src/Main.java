package src;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        Game game = new Game();
        game.init(primaryStage);
        game.startGameLoop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
