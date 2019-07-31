package src.main.java.audio;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import java.io.File;
import java.util.*;

public class AudioUtil {

    private static final String fileExtensions = ".*\\.wav";

    protected static Map<AudioClipType, Sound[]> loadSounds(boolean isExperiencial) {
        String fileNotFoundBase;

        String location = "src/main/resources/sound/";
        if(isExperiencial) {
            location += "experiential/";
            fileNotFoundBase = "Experiential sound for ";
        }
        else {
            location += "default/";
            fileNotFoundBase = "Default sound for ";
        }

        AudioClipType[] audioClips = AudioClipType.values();

        Map<AudioClipType, Sound[]> soundMap = new HashMap<>();

        for(AudioClipType clip : audioClips) {
            File clipParentFile = new File(location + clip.toString().toLowerCase());

            if(!clipParentFile.exists()) {
                System.out.println(fileNotFoundBase + clip + " not found");
                continue;
            }

            File[] clipFiles = clipParentFile.listFiles();

            Sound[] soundArray = Arrays.stream(clipFiles)
                    .filter(f -> f.toString().matches(fileExtensions))
                    .map(f -> TinySound.loadSound(f))
                    .toArray(Sound[]::new);

            if(soundArray.length == 0) {
                System.out.println(fileNotFoundBase + clip + " not found");
                continue;
            }

            soundMap.put(clip, soundArray);

        }

        return soundMap;
    }

}
