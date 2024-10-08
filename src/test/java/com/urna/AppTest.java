package com.urna;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class AppTest {

    private Voice voice;

    public AppTest() {
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice("kevin16");
        if (voice != null) {
            voice.allocate();
        }
    }

    @Test
    public void testApp() {
        System.out.println("Executando o teste testApp");
        if (voice != null) {
            voice.speak("Executando o teste testApp");
        }
        assertTrue(true);
    }
}
