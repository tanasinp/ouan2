package src;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameDialog extends Stage {
	
	private Text congrat = new Text();
	
	public GameDialog() {
		VBox vbox = new VBox(10); //, textQuestion, fieldAnswer, textActualAnswer, btnSubmit);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox);
        
        setScene(scene);
	}
	
	public void open() {
		congrat.setText("CONGRATURATIONS!");
		show();
	}
}
