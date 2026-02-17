package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[10];

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/click.wav");
        soundURL[1] = getClass().getResource("/sound/obtain.wav");
        soundURL[2] = getClass().getResource("/sound/swoosh.wav");
        soundURL[5] = getClass().getResource("/sound/rabby.wav");
        soundURL[6] = getClass().getResource("/sound/wet_cave.wav");
        soundURL[7] = getClass().getResource("/sound/b_toony.wav");
        soundURL[8] = getClass().getResource("/sound/tape_master.wav");
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
