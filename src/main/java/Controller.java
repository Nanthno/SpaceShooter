package src.main.java;

import src.main.java.audio.AudioManager;
import src.main.java.audio.MusicType;
import src.main.java.density.DensityMap;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;
import src.main.java.graphics.GraphicsManager;
import src.main.java.spawn.SpawnController;
import src.main.java.spawn.TimeStampEvent;
import src.main.java.spawn.TimelineUtil;
import src.main.java.weapons.WeaponType;
import src.main.java.weapons.enemyWeapons.EnemyWeaponParent;
import src.main.java.weapons.enemyWeapons.ShooterEmp;
import src.main.java.weapons.playerWeapons.*;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class Controller {

    static GraphicsManager graphicsManager;
    static AudioManager audioManager;

    static boolean isSoundExperiential = false;

    static volatile List<EnemyShip> enemyShips = new ArrayList<>();

    static PlayerShip player = new PlayerShip();
    static ArrayList<PlayerWeaponParent> playerFiredWeapons = new ArrayList<>();
    static LaserBlast laserBlast = null;

    static ArrayList<EnemyWeaponParent> enemyFiredWeapons = new ArrayList<>();

    static ArrayList<Explosion> explosions = new ArrayList<>();

    static DensityMap densityMap;

    static final int frameRate = 15;

    static SpawnController spawnController = new SpawnController();

    private static final Queue<TimeStampEvent> fullTimeline = TimelineUtil.readTimelinesToQueue();
    private static Queue<TimeStampEvent> timeline = null;

    static int score = 0;

    static GameState gameState;

    static Input input;

    static List<EnemyShip> newEnemyShips = new ArrayList<>();

    static boolean killMissiles = false;

    static long frameCount = 0;

    public static void main(String[] args) {

        input = new Input();
        graphicsManager = new GraphicsManager();
        audioManager = new AudioManager();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("In shutdown hook");
                Controller.shutdown();
            }
        }, "Shutdown-thread"));

        setGameState(GameState.MENU);

        gameLoop();

    }

    static void resetTimeline() {
        timeline = new LinkedList<>(fullTimeline);
    }

    static void update() {
        if (gameState == GameState.PLAYING) {
            updateGame();
        }
        graphicsManager.drawScreen();
    }

    static void updateGame() {
        frameCount++;

        if (timeline.size() > 0)
            timeline = spawnController.updateSpawnProbabilities(timeline);

        densityMap = new DensityMap(enemyShips);

        player.update();

        // updates weapons
        ArrayList<PlayerWeaponParent> newPlayerBullets = new ArrayList<>();
        for (PlayerWeaponParent w : playerFiredWeapons) {
            boolean kill = w.update();
            if (killMissiles && w.getType() == WeaponType.PLAYER_MISSILE && ((Missile) w).isArmed()) {
                ((Missile) w).hitEnemy();
            } else if (w.getx() < 1040 && !kill) {
                newPlayerBullets.add(w);
            }
        }
        playerFiredWeapons = newPlayerBullets;
        killMissiles = false;

        ArrayList<EnemyWeaponParent> newEnemyWeapons = new ArrayList<>();
        for (EnemyWeaponParent w : enemyFiredWeapons) {
            w.update();
            if (w.getx() > 0) {
                newEnemyWeapons.add(w);
            }
        }
        enemyFiredWeapons = newEnemyWeapons;

        if (laserBlast != null) {
            boolean destroy = laserBlast.update();
            if (destroy) {
                laserBlast = null;
            }
        }

        // updates enemyShips
        for (int i = 0; i < enemyShips.size(); i++) {
            EnemyShip e = enemyShips.get(i);
            boolean offScreen = e.update();
            if (offScreen) {
                if (e.getType() != EnemyType.SHIELD)
                    player.looseHealth(10);
            } else {
                newEnemyShips.add(e);
            }
        }
        enemyShips = newEnemyShips;
        newEnemyShips = new ArrayList<>();

        // spawns new enemyShips
        List<EnemyShip> shipsSpawning = spawnController.onTick();
        enemyShips.addAll(shipsSpawning);

        // updates explosions
        for (int i = explosions.size() - 1; i >= 0; i--) {
            boolean kill = explosions.get(i).update();
            if (kill) {
                explosions.remove(i);
            }
        }

        checkEnemyBulletCollision();
        checkEnemyExplosionCollision();
        checkPlayerEnemyCollision();
        checkPlayerEnemyWeaponCollision();

        if (frameCount % 2 == 0)
            audioManager.playSoundFrame();
    }

    static void checkEnemyBulletCollision() {
        for (int j = enemyShips.size() - 1; j >= 0; j--) {
            EnemyShip e = enemyShips.get(j);

            // checks for collision with laser blast
            if (laserBlast != null) {
                int distance = Math.abs((laserBlast.getx() + laserBlast.getRadius()) - (e.getx() + e.getRadius()));
                if (distance < e.getRadius() + laserBlast.getRadius()) {
                    if (e.isKillable(LaserBlast.class)) {
                        spawnExp(e.getx(), e.gety(), e.getRadius(), e.getType(), 0);
                        enemyShips.remove(j);
                        continue;
                    }
                }
            }

            // checks for collision with a player bullet
            for (int i = playerFiredWeapons.size() - 1; i >= 0; i--) {
                PlayerWeaponParent weapon = playerFiredWeapons.get(i);
                if (e.collideWithWeapon(weapon)) {
                    if (weapon.getType() == WeaponType.PLAYER_BULLET) {
                        PlayerBullet b = (PlayerBullet) weapon;
                        if (e.isKillable(PlayerBullet.class)) {
                            spawnExp(e.getx(), e.gety(), e.getRadius(), e.getType(), 0);
                            enemyShips.remove(j);
                        }
                        playerFiredWeapons.remove(i);

                    }
                    if (weapon.getType() == WeaponType.PLAYER_MISSILE) {
                        ((Missile) weapon).hitEnemy();
                        playerFiredWeapons.remove(i);
                    }
                    if (weapon.getType() == WeaponType.PLAYER_BURST) {
                        spawnExp(e.getx(), e.gety(), e.getRadius(), e.getType(), 0);
                        enemyShips.remove(j);
                    }
                }
            }
        }
    }

    static void checkEnemyExplosionCollision() {
        for (int i = explosions.size() - 1; i >= 0; i--) {
            Explosion explosion = explosions.get(i);
            for (int j = enemyShips.size() - 1; j >= 0; j--) {
                EnemyShip enemy = enemyShips.get(j);
                if (explosion.getStage() > explosion.getEffectiveStage() &&
                        distance(explosion.getx(), explosion.gety(), explosion.getRadius(),
                                enemy.getx(), enemy.gety(), enemy.getRadius()) < 2 * (enemy.getRadius() + explosion.getRadius())) {
                    if (enemy.isKillable(Explosion.class)) {
                        enemy.killShip(explosion.getCatalystSeparation() + 1);
                        enemyShips.remove(j);
                    }

                }
            }
        }
    }

    static void checkPlayerEnemyCollision() {
        for (int i = enemyShips.size() - 1; i >= 0; i--) {
            EnemyShip e = enemyShips.get(i);
            if (distance(e.getx(), e.gety(), e.getRadius(),
                    player.getx(), player.gety(), player.getRadius()) < e.getRadius() + player.getRadius()) {
                if (e.isKillable(PlayerShip.class)) {
                    e.killShip(0);
                    enemyShips.remove(i);
                }

            }
        }
    }

    static void checkPlayerEnemyWeaponCollision() {
        for (int i = enemyFiredWeapons.size() - 1; i >= 0; i--) {
            EnemyWeaponParent w = enemyFiredWeapons.get(i);
            if (distance(w.getx(), w.gety(), w.getRadius(),
                    player.getx(), player.gety(), player.getRadius()) < w.getRadius() + player.getRadius()) {
                enemyFiredWeapons.remove(i);
                player.hitByWeapon(w);
            }
        }
    }

    static double distance(double x1, double y1, int r1, double x2, double y2, int r2) {
        x1 += r1;
        y1 += r1;
        x2 += r2;
        y2 += r2;

        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);

        return Math.sqrt(dx * dx + dy * dy);
    }

    public static void spawnExp(int x, int y, int shipR, EnemyType enemyType, int catalystSeparation) {
        ExplosionType explosionType = EnemyType.getExplosionType(enemyType);

        spawnExp(x, y, shipR, explosionType, catalystSeparation);
    }

    public static void spawnExp(int x, int y, int radius, ExplosionType explosionType, int catalystSeperation) {
        x += radius;
        y += radius;
        int expRadius = Explosion.expRadii.get(explosionType);
        x -= expRadius;
        y -= expRadius;

        explosions.add(new Explosion(x, y, catalystSeperation, explosionType));

        audioManager.addSoundToFrame(Globals.getExplosionAudioClipType(explosionType));
    }


    public static void updateSpawnProbabilities(TimeStampEvent event) {
        spawnController.updateSpawnProbabilities(event);
    }

    public static void gameLoop() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                update();
            }
        }, 0, frameRate);
    }

    static void firePlayerBullet(PlayerBullet b) {
        playerFiredWeapons.add(b);
        playWeaponSound(WeaponType.PLAYER_BULLET);
    }

    static void fireBlast(int x) {
        if (laserBlast == null) {
            laserBlast = new LaserBlast(x);
            playWeaponSound(WeaponType.PLAYER_LASER_BLAST);
        }
    }

    static void createMissile(int x, int y) {
        Missile missile = new Missile(x, y);
        playerFiredWeapons.add(missile);
        playWeaponSound(WeaponType.PLAYER_MISSILE);
    }

    static void createBurst(int x, int y) {
        Burst burst = new Burst(x - Burst.getMaxRadius(), y - Burst.getMaxRadius());
        playerFiredWeapons.add(burst);
        playWeaponSound(WeaponType.PLAYER_BURST);
    }

    public static void spawnShooterEmp(int x, int y, double dx) {
        ShooterEmp bullet = new ShooterEmp(x, y, -1 * dx);
        enemyFiredWeapons.add(bullet);

        playWeaponSound(WeaponType.ENEMY_EMP);
    }

    static void playWeaponSound(WeaponType type) {
        audioManager.addSoundToFrame(Globals.getWeaponAudioClipType(type));
    }

    public static void addKillScore(EnemyType type, int catalistSeperation) {
        int killPoints = Globals.getEnemyShipPointValue(type);
        double multiplier = catalistSeperation / 2 + 1;

        killPoints *= multiplier;
        score += killPoints;
    }

    public static void addEnemy(EnemyShip enemy) {
        newEnemyShips.add(enemy);
    }

    public static Point findMousePosition() {
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        Point framePos = graphicsManager.getFramePosition();

        mousePos.translate(-1 * framePos.x, -1 * framePos.y);
        return mousePos;
    }

    public static void shutdown() {
        audioManager.shutdown();
    }

    public static List<EnemyShip> getEnemyArray() {
        return enemyShips;
    }

    public static DensityMap getDensityMap() {
        return densityMap;
    }

    public static PlayerShip getPlayerShip() {
        return player;
    }

    public static List<PlayerWeaponParent> getPlayerFiredWeapons() {
        return playerFiredWeapons;
    }

    public static List<EnemyWeaponParent> getEnemyFiredWeapons() {
        return enemyFiredWeapons;
    }

    public static LaserBlast getLaserBlast() {
        return laserBlast;
    }

    public static List<Explosion> getExplosions() {
        return explosions;
    }

    public static int getPlayerHealth() {
        return player.getHealth();
    }

    public static int getPlayerMaxHealth() {
        return player.getMaxHealth();
    }

    public static int getPlayerMaxHeat() {
        return player.getMaxHeat();
    }

    public static int getPlayerHeat() {
        return player.getHeat();
    }

    public static double getPlayerXPos() {
        return player.getx();
    }

    public static double getPlayerYPos() {
        return player.gety();
    }

    public static double getPlayerYSpeed() {
        return player.ySpeed * player.yMove;
    }

    public static int getCharge() {
        return player.getCharge();
    }

    public static int getMaxCharge() {
        return player.getMaxCharge();
    }

    public static int getScore() {
        return score;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public static Input getInput() {
        return input;
    }

    public static MouseListener getMouseListener() {
        return input.getMouseListener();
    }

    public static boolean getIsSoundExperiential() {
        return isSoundExperiential;
    }

    public static void toggleSoundMode() {
        isSoundExperiential = !isSoundExperiential;
        audioManager.setSoundType(isSoundExperiential);
    }

    public static void setGameState(GameState newGameState) {
        if (newGameState == GameState.PLAYING) {
            SpawnController.setStartTime(System.currentTimeMillis());
            resetTimeline();
            audioManager.playMusic(MusicType.GAME);
        }
        if (newGameState == GameState.MENU) {
            audioManager.playMusic(MusicType.MENU);
        }
        if (newGameState == GameState.HIGH_SCORE)
            audioManager.playMusic(MusicType.HIGHSCORE);

        gameState = newGameState;
    }

    public static void killMissiles() {
        killMissiles = true;
    }
}
