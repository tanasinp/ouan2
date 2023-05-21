package src;

import entity.Player;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class DeathCount {

    private Text countText = new Text();
    private int count;

    public DeathCount(Player player, Pane deathRoot) {
        count = player.getCount();	//get death count
        countText.setText("Death: " + count);
        countText.setStyle("-fx-fill: black; -fx-font-size: 32; -fx-font-weight: bold;");

        countText.setTranslateX(20);	//set it at the top left of window
        countText.setTranslateY(40);
        deathRoot.getChildren().add(countText);
    }

    public void updateCount(int newCount) {	//update death count. when player died, death count will + 1
        count = newCount;
        countText.setText("Death: " + count);
    }

}
