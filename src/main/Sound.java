package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[10];

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/harmonia.wav");
        soundURL[1] = getClass().getResource("/sound/click.wav");
        soundURL[2] = getClass().getResource("/sound/obtain.wav");
        soundURL[3] = getClass().getResource("/sound/swoosh.wav");
    }

    public void setFile(int index) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[index]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
