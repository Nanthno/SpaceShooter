package src.main.java.audio;


import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AudioManager {

    TinySound tinySound;

    boolean experientialSound = false;

    private static Map<AudioClipType, Sound[]> soundClips;

    private static Map<MusicType, Music[]> musicClips;

    private static Music currentPlayingMusic;
    Map<AudioClipType, Integer> soundFrame = new HashMap<>();

    public AudioManager() {
        tinySound = new TinySound();
        tinySound.init();
        loadAudioMaps();
    }

    public void setSoundType(boolean isExperiencial) {
        experientialSound = isExperiencial;
        loadAudioMaps();
    }

    private void loadAudioMaps() {
        soundClips = AudioUtil.loadSounds(experientialSound);
        musicClips = AudioUtil.loadMusic(experientialSound);
    }

    public void addSoundToFrame(AudioClipType audio) {
        if(!soundFrame.containsKey(audio))
            soundFrame.put(audio, 1);
        else
            soundFrame.put(audio, soundFrame.get(audio)+1);
    }

    public void playSoundFrame() {
        for(AudioClipType audio : soundFrame.keySet()) {
            playClip(audio, soundFrame.get(audio));
        }

        soundFrame = new HashMap<>();
    }

    private void playClip(AudioClipType audio, int clipCount) {

        if(soundClips.containsKey(audio)) {
            chooseClip(soundClips.get(audio)).play();//Math.pow(clipCount, 0.25));
        }
        else {
            System.out.println(String.format("WARN: sound %s was played but is not defined", audio.toString()));
        }
    }

    public void playMusic(MusicType music) {
        if(currentPlayingMusic != null) {
            currentPlayingMusic.stop();
        }

        if(musicClips.containsKey(music)) {
            currentPlayingMusic = musicClips.get(music)[0];
            currentPlayingMusic.play(true, 0.3);
        }
        else {
            System.out.println(String.format("WARN: music %s was played but is not defined", music.toString()));
            currentPlayingMusic = null;
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