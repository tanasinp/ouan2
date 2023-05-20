package src;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {

	private MediaPlayer sound;
	

	public Sound() {
//		System.out.println(ClassLoader.getSystemResource("music/BgMusic.wav"));
		Media audio = new Media(getClass().getClassLoader().getResource("music/BgMusic.wav").toExternalForm());
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