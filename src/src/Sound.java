package src;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {

	private MediaPlayer sound;
	

	public Sound() {
		Media audio = new Media(getClass().getClassLoader().getResource("res/BgMusic.wav").toExternalForm());
		this.sound = new MediaPlayer(audio);
		sound.setCycleCount(MediaPlayer.INDEFINITE);
	}

	public void playSound() {
		sound.play();
	}

	public void stopSound() {
		sound.stop();
	}

}