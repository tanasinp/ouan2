package src;

import entity.Player;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class DeathCount {

    private Text countText = new Text();
    private int count;

    public DeathCount(Player player, Pane deathRoot) {
        count = player.getCount();
        countText.setText("Death: " + count);
        countText.setStyle("-fx-fill: black; -fx-font-size: 32; -fx-font-weight: bold;");

        countText.setTranslateX(20);
        countText.setTranslateY(40);
        deathRoot.getChildren().add(countText);
    }

    public void updateCount(int newCount) {
        count = newCount;
        countText.setText("Death: " + count);
    }

}
