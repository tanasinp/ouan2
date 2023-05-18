package src;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

public class Game extends Pane {
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();

    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane uiRoot = new Pane();

    private Level level;
    private Player player;
    private boolean running = true;
    private static Sound sound;

    public void init(Stage primaryStage) {
        level = new Level(gameRoot);
        player = new Player(gameRoot, keys);

        appRoot.getChildren().addAll(level.getBackground(), gameRoot, uiRoot);

        Scene scene = new Scene(appRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
        
        sound = new Sound();
        sound.playSound();
        
        primaryStage.setTitle("Jump! JUMP! JUmp! :)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            public void handle(long now) {
                if (running) {
                    update();
                }
                
                if (player.isDialogEvent()) {
                	GameDialog dialog = new GameDialog();
                	dialog.open();
                }
            }
        };
        timer.start();
    }

    private void update() {
        player.update(gameRoot);

        // Handle collisions or other game logic here
    }
}
