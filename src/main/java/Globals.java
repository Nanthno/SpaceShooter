package src.main.java;


import src.main.java.audio.AudioClipType;
import src.main.java.enemy.*;
import src.main.java.weapons.WeaponType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Globals {

    public static final int screenHeight = 640;
    public static final int screenWidth = 1024;

    static final Map<EnemyType, Integer> enemyShipRadius = Map.of(
            EnemyType.BASIC, 8,
            EnemyType.FUEL, 12,
            EnemyType.AGILE, 12,
            EnemyType.SHIELDER, 16,
            EnemyType.SHIELD, 128,
            EnemyType.ARMORED1, 16,
            EnemyType.SHOOTER, 12,
            EnemyType.PILOTED, 12
    );


    static final Map<EnemyType, Integer> enemyShipPointValue = Map.of(
            EnemyType.BASIC, 100,
            EnemyType.FUEL, 200,
            EnemyType.AGILE, 300,
            EnemyType.SHIELDER, 400,
            EnemyType.ARMORED1, 400,
            EnemyType.SHOOTER, 300,
            EnemyType.PILOTED, 300);

    static final Map<EnemyType, Class> enemyTypeClasses = Map.of(
            EnemyType.BASIC, EnemyBasic.class,
            EnemyType.FUEL, EnemyFuel.class,
            EnemyType.AGILE, EnemyAgile.class,
            EnemyType.SHIELDER, EnemyShielder.class,
            EnemyType.SHIELD, EnemyShield.class,
            EnemyType.ARMORED1, EnemyArmored1.class,
            EnemyType.SHOOTER, EnemyShooter.class,
            EnemyType.PILOTED, EnemyPiloted.class
    );

    static final Map<ExplosionType, AudioClipType> explosionAudioClips = makeExplosionAudioClipsMap();
    static final Map<WeaponType, AudioClipType> weaponAudioClips = makeWeaponAudioClipsMap();

    static Map<ExplosionType, Integer> explosionTypeMaxFrames = new HashMap<>();

    static Map<EnemyType, Integer> enemyShipsMaxFrames = new HashMap<>();
    static Map<WeaponType, Integer> weaponMaxFrames = new HashMap<>();
    static int playerMaxFrames;

    private static Map<WeaponType, AudioClipType> makeWeaponAudioClipsMap() {
        Map<WeaponType, AudioClipType> clipsMap = new HashMap<>();

        Set<String> audioClipStrings = Arrays.stream(AudioClipType.values()).map(c -> c.toString()).collect(Collectors.toSet());

        for (WeaponType weaponType : WeaponType.values()) {
            String clipName = weaponType.toString() + "_FIRED";
            if (audioClipStrings.contains(clipName)) {
                clipsMap.put(weaponType, AudioClipType.valueOf(clipName));
            } else {
                System.out.println(String.format("WARN: explosion %s does not have a matching AudioClipType of %s", weaponType, clipName));
            }
        }

        return clipsMap;
    }

    private static Map<ExplosionType, AudioClipType> makeExplosionAudioClipsMap() {

        Map<ExplosionType, AudioClipType> clipsMap = new HashMap<>();

        Set<String> audioClipStrings = Arrays.stream(AudioClipType.values()).map(c -> c.toString()).collect(Collectors.toSet());

        for (ExplosionType expType : ExplosionType.values()) {
            String clipName = expType.toString() + "_EXPLOSION";
            if (audioClipStrings.contains(clipName)) {
                clipsMap.put(expType, AudioClipType.valueOf(clipName));
            } else {
                System.out.println(String.format("WARN: explosion %s does not have a matching AudioClipType of %s", expType, clipName));
            }
        }

        return clipsMap;
    }

    public static int getEnemyShipRadius(EnemyType type) {
        return enemyShipRadius.get(type);
    }

    public static int getEnemyShipPointValue(EnemyType type) {
        return enemyShipPointValue.get(type);
    }

    public static double distance(double x1, double x2, double y1, double y2) {

        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);

        return Math.sqrt(dx * dx + dy * dy);
    }

    public static double distance(int[] pointA, int[] pointB) {
        int x1 = pointA[0];
        int y1 = pointA[1];
        if (pointA.length > 2) {
            int r1 = pointA[2];
            x1 += r1;
            y1 += r1;
        }
        int x2 = pointB[0];
        int y2 = pointB[1];
        if (pointB.length > 2) {
            int r2 = pointB[2];
            y2 += r2;
            x2 += r2;
        }

        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);

        return Math.sqrt(dx * dx + dy * dy);
    }

    // TODO Remove this method
    public static Class getEnemyClass(EnemyType type) {
        return enemyTypeClasses.get(type);
    }

    public static void addToExplosionMaxFrames(ExplosionType type, int maxFrames) {
        explosionTypeMaxFrames.put(type, maxFrames);
    }

    public static int getExplosionMaxFrame(ExplosionType type) {
        return explosionTypeMaxFrames.get(type);
    }

    public static void addToEnemyShipMaxFrames(EnemyType type, int maxFramesCount) {
        enemyShipsMaxFrames.put(type, maxFramesCount);
    }

    public static void addToWeaponMaxFrames(WeaponType type, int maxFrameCount) {
        weaponMaxFrames.put(type, maxFrameCount);
    }

    public static void setPlayerMaxFrames(int playerMaxFrames) {
        Globals.playerMaxFrames = playerMaxFrames;
    }

    public static int getEnemyShipsMaxFrames(EnemyType type) {
        if (enemyShipsMaxFrames.containsKey(type))
            return enemyShipsMaxFrames.get(type);

        Exception e = new NoSuchFieldException("There is no stored frame count for EnemyType = " + type);
        e.printStackTrace();
        return 1;
    }

    public static int getWeaponMaxFrames(WeaponType type) {
        return weaponMaxFrames.get(type);
    }

    public static int getPlayerMaxFrames() {
        return playerMaxFrames;
    }

    public static AudioClipType getExplosionAudioClipType(ExplosionType explosionType) {
        return explosionAudioClips.get(explosionType);
    }

    public static AudioClipType getWeaponAudioClipType(WeaponType weaponType) {
        return weaponAudioClips.get(weaponType);
    }
}
