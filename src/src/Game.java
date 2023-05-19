package src;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;

public class Game extends Pane {
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();

    private Pane appRoot = new Pane();
    private Pane gameRoot = new Pane();
    private Pane congratRoot = new Pane();
    private Pane menuRoot = new Pane();

    private Player player;
    private boolean running = true;
    private static Sound sound;
    private GameDialog dialog;
    private MenuPane menuPane;

    public void init(Stage primaryStage) {
    	primaryStage.setMaxHeight(720);
    	primaryStage.setMaxWidth(1280);
    	primaryStage.setMinHeight(720);
    	primaryStage.setMinWidth(1280);
        player = new Player(gameRoot, keys);
        dialog = new GameDialog();
        menuPane = new MenuPane(menuRoot, this);
        
        appRoot.getChildren().addAll(gameRoot, congratRoot);
        appRoot.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
        appRoot.setVisible(false);

        menuRoot.getChildren().addAll(appRoot);

        Scene scene = new Scene(menuRoot);
        scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));

        sound = new Sound();
        sound.playSound();

        primaryStage.setTitle("Jump! JUMP! JUmp! :)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Pane getAppRoot() {
		return appRoot;
	}

	public void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            public void handle(long now) {
                if (running) {
                    update();
                }

                if (player.isDialogEvent()) {
                    player.setDialogEvent(false);
                    Platform.runLater(() -> dialog.open(congratRoot));
                    sound.stopSound();
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
