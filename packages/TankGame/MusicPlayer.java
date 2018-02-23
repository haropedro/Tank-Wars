
package TankGame;

import java.io.File;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;


public class MusicPlayer implements Runnable{
    private ArrayList<String> musicFiles;
    private int currentSongNum;
    
    public MusicPlayer(String... files) {
        musicFiles = new ArrayList<String>();
        for(String file: files) {
            try {
              musicFiles.add("Resources/" + file);
            }
            catch(Exception e) {
              e.printStackTrace();
            }
        }
    }
    
    private void play(String fileName) {
        try {
            File soundFile = new File(fileName);
            AudioInputStream audioFile = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat format = audioFile.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioFile);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); //sets volume
            gainControl.setValue(-10);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        play(musicFiles.get(currentSongNum));
    }
    
    
}
