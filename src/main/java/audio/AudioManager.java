package src.main.java.audio;



import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import java.util.Map;

public class AudioManager {

    TinySound tinySound;

    boolean experientialSound = true;

    private static Map<AudioFiles, Sound> soundClips;

    public AudioManager() {
        tinySound = new TinySound();
        tinySound.init();
        loadAudioMap();
    }

    public void setSoundType(boolean isExperiencial) {
        experientialSound = isExperiencial;
        loadAudioMap();
    }

    private void loadAudioMap() {
        if (experientialSound) {
            soundClips = Map.of(
                    AudioFiles.PLAYER_GUN, TinySound.loadSound("src/main/resources/sound/Pew.wav")
            );
        }
    }

    public void playSound(AudioFiles audio) {
        if(experientialSound) {
            soundClips.get(audio).play();
        }
    }

    public void shutdown() {
        tinySound.shutdown();
    }



}
