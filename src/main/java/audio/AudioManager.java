package src.main.java.audio;


import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import java.util.Map;
import java.util.Random;

public class AudioManager {

    TinySound tinySound;

    boolean experientialSound = false;

    private static Map<AudioClipType, Sound[]> soundClips;

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
        soundClips = AudioUtil.loadSounds(experientialSound);
    }

    public void playSound(AudioClipType audio) {
        if(soundClips.containsKey(audio)) {
            chooseClip(soundClips.get(audio)).play();
        }
        else {
            System.out.println(String.format("WARN: sound %s was played but is not defined", audio.toString()));
        }
    }

    private static Sound chooseClip(Sound[] sounds) {
        Random rand = new Random();
        int selection = rand.nextInt(sounds.length);
        return sounds[selection];
    }

    public void shutdown() {
        tinySound.shutdown();
    }


}
