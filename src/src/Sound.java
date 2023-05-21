package src;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {

	private MediaPlayer sound;
	

	public Sound() {
		Media audio = new Media(getClass().getClassLoader().getResource("music/BgMusic.wav").toExternalForm());	//getting sound
		this.sound = new MediaPlayer(audio);
		sound.setCycleCount(MediaPlayer.INDEFINITE);	//loop sound for infinite
	}

	public void playSound() {	//play sound
		sound.play();
	}

	public void stopSound() {	//stop sound
		sound.stop();
	}

}