package src.main.java.audio;


import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AudioManager {

    private boolean experientialSound = false;

    private static Map<AudioClipType, Sound[]> soundClips;

    private static Map<MusicType, Music[]> musicClips;

    private static Music currentPlayingMusic;
    private static final double maxMusicVolume = 0.4;
    private static double currentMusicVolume;
    private Map<AudioClipType, Integer> soundFrame = new HashMap<>();
    private final Map<AudioClipType, Long> lastPlayed;

    private boolean mute = false;


    public AudioManager() {
        TinySound tinySound = new TinySound();
        TinySound.init();
        loadAudioMaps();
        lastPlayed = primeLastPlayed();
        currentMusicVolume = mute ? 0 : maxMusicVolume;
    }

    private Map<AudioClipType, Long> primeLastPlayed() {
        Map<AudioClipType, Long> lastPlayed = new HashMap<>();
        for (AudioClipType audio : AudioClipType.values()) {
            lastPlayed.put(audio, 0L);
        }
        return lastPlayed;
    }

    public void setSoundType(boolean isExperiential) {
        experientialSound = isExperiential;
        loadAudioMaps();
    }

    private void loadAudioMaps() {
        soundClips = AudioUtil.loadSounds(experientialSound);
        musicClips = AudioUtil.loadMusic(experientialSound);
    }

    public void addSoundToFrame(AudioClipType audio) {
        if (!soundFrame.containsKey(audio))
            soundFrame.put(audio, 1);
        else
            soundFrame.put(audio, soundFrame.get(audio) + 1);
    }

    public void playSoundFrame(long frame) {
        if (!mute) {
            for (AudioClipType audio : soundFrame.keySet()) {
                long timeSinceLastPlayed = frame - lastPlayed.get(audio);
                int playDelay = 10;
                if (timeSinceLastPlayed > playDelay) {
                    playClip(audio);
                    lastPlayed.put(audio, frame);
                }
            }

            soundFrame = new HashMap<>();
        }
    }

    private void playClip(AudioClipType audio) {

        if (soundClips.containsKey(audio)) {
            chooseClip(soundClips.get(audio)).play();
        } else {
            System.out.println(String.format("WARN: sound %s was played but is not defined", audio.toString()));
        }
    }

    public void playMusic(MusicType music) {
        if (currentPlayingMusic != null) {
            currentPlayingMusic.stop();
        }

        if (musicClips.containsKey(music)) {
            currentPlayingMusic = musicClips.get(music)[0];
            currentPlayingMusic.play(true, currentMusicVolume);
        } else {
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
        TinySound.shutdown();
    }

    public boolean isMuted() {
        return mute;
    }

    public void toggleMute() {
        mute = !mute;
        if (mute) {
            currentMusicVolume = 0;
            currentPlayingMusic.setVolume(0);
        } else {
            currentMusicVolume = maxMusicVolume;
            currentPlayingMusic.setVolume(maxMusicVolume);
        }
    }

}
