package BreakYourWalls;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MyAudio {
    // Çalınacak ses
    static Clip clip;

    // Stream objesi
    static AudioInputStream audioInputStream;

    public static void PlayAudio(String filePath)
            throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        // Ses dosyasından stream objesini oluştur
        audioInputStream =
                AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

        // Clip objesi
        clip = AudioSystem.getClip();

        // clip objesi ile stream edilen objeyi aç
        clip.open(audioInputStream);

        // Ek döngü sayısı
        clip.loop(0);
    }
}
