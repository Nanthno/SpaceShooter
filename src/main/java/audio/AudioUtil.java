package src.main.java.audio;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;
import src.main.java.Globals;
import src.main.java.ResourceFileType;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class AudioUtil {

    private static final String fileExtensions = ".*\\.wav";

    static Map<AudioClipType, Sound[]> loadSounds(boolean isExperiential) {
        String fileNotFoundBase = makeFileNotFoundBase(isExperiential);
        String location = makeFileLocation(isExperiential);

        System.out.println("loading sfx from " + location);

        AudioClipType[] audioClips = AudioClipType.values();

        Map<AudioClipType, Sound[]> soundMap = new HashMap<>();

        for (AudioClipType clip : audioClips) {
            File clipParentFile = new File(location + clip.toString().toLowerCase());

            if (!clipParentFile.exists()) {
                System.out.println(fileNotFoundBase + clip + " not found");
                continue;
            }

            File[] clipFiles = clipParentFile.listFiles();

            Sound[] soundArray = Arrays.stream(Objects.requireNonNull(clipFiles))
                    .filter(f -> f.toString().matches(fileExtensions))
                    .map(TinySound::loadSound)
                    .toArray(Sound[]::new);

            if (soundArray.length == 0) {
                System.out.println(fileNotFoundBase + clip + " not found");
                continue;
            }

            soundMap.put(clip, soundArray);

        }

        return soundMap;
    }

    static Map<MusicType, Music[]> loadMusic(boolean isExperiential) {
        String fileNotFoundBase = makeFileNotFoundBase(isExperiential);
        String location = makeFileLocation(isExperiential);

        System.out.println("loading music from " + location);

        MusicType[] musicClips = MusicType.values();

        Map<MusicType, Music[]> soundMap = new HashMap<>();

        for (MusicType clip : musicClips) {
            File clipParentFile = new File(location + "music_" + clip.toString().toLowerCase());

            if (!clipParentFile.exists()) {
                System.out.println(fileNotFoundBase + clip + " not found");
                continue;
            }

            File[] clipFiles = clipParentFile.listFiles();

            Music[] soundArray = Arrays.stream(Objects.requireNonNull(clipFiles))
                    .filter(f -> f.toString().matches(fileExtensions))
                    .map(TinySound::loadMusic)
                    .toArray(Music[]::new);

            if (soundArray.length == 0) {
                System.out.println(fileNotFoundBase + clip + " not found");
                continue;
            }

            soundMap.put(clip, soundArray);

        }

        return soundMap;
    }

    private static String makeFileNotFoundBase(boolean isExperiential) {
        String fileNotFoundBase;
        if (isExperiential) {
            fileNotFoundBase = "Experiential sound for ";
        } else {
            fileNotFoundBase = "Default sound for ";
        }

        return fileNotFoundBase;
    }

    private static String makeFileLocation(boolean isExperiential) {
        String location = Globals.getResourceFile(ResourceFileType.SOUND);
        if (isExperiential) {
            location += "experiential/";
        } else {
            location += "default/";
        }

        return location;
    }

}
