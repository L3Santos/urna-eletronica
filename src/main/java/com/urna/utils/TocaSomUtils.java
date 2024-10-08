package com.urna.utils;

import javax.sound.sampled.*;
import java.io.IOException;

public class TocaSomUtils {
    public void tocarSom(String nomeArquivo) {
        try {
            // Obtém o recurso como um stream de entrada
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    getClass().getResourceAsStream("/songs/" + nomeArquivo)
            );
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Tratar exceções
        }
    }
}
