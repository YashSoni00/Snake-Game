import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class EatSound extends Thread {

    public void run() {
        File file = new File("sound/swallow.wav");
        if (file.exists()) {
            AudioInputStream audioInputStream = null;
            try {
                audioInputStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                clip.start();
                Thread.sleep(clip.getMicrosecondLength() / 1000);
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
