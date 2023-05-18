package  src;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Level {

	private Rectangle background;

	public Level(Pane gameRoot) {
		background = new Rectangle(1280, 720);

        

		// Define your level layout and create platforms here
		// Example:
		// Node platform = createEntity(x, y, width, height, color);
		// platforms.add(platform);

		// Add the platforms to the gameRoot pane
		
	}

	public Rectangle getBackground() {
		return background;
	}

	// Utility method for creating platform entities
	

	// Other methods for level management and manipulation
}