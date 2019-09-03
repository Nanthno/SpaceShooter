package src.main.java;

import src.main.java.audio.AudioClipType;
import src.main.java.audio.AudioManager;
import src.main.java.audio.MusicType;
import src.main.java.density.DensityMap;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;
import src.main.java.graphics.GraphicsManager;
import src.main.java.graphics.HighScorePanel;
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


    static int health = 100;
    static int maxHealth = 100;

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

    static double escapingEnemyTracker = 0;
    static final double escapingEnemyPerFrameReduction = 0.1;
    static final int defaultHealthLossPerEnemy = 10;

    static final int laserBlastShake = 2;
    static final Map<ExplosionType, Integer> explosionShake = new HashMap<ExplosionType, Integer>() {{
        put(ExplosionType.SMALL, 2);
        put(ExplosionType.MEDIUM, 5);
        put(ExplosionType.FUEL, 6);
        put(ExplosionType.PROJECTILE, 8);
    }};

    // used to prevent an update from running if another update is currently running
    static boolean isUpdating = false;

    public static void main(String[] args) {

        input = new Input();
        graphicsManager = new GraphicsManager();
        audioManager = new AudioManager();

        setGameState(GameState.MENU);
        resetGame();


        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("In shutdown hook");
                Controller.shutdown();
            }
        }, "Shutdown-thread"));


        gameLoop();

    }

    static void resetGame() {
        health = maxHealth;
        enemyShips = new ArrayList<>();
        enemyFiredWeapons = new ArrayList<>();
        playerFiredWeapons = new ArrayList<>();
        player = new PlayerShip();
        score = 0;
    }

    static void resetTimeline() {
        timeline = new LinkedList<>(fullTimeline);
    }

    static void update() {
        if (!isUpdating) {
            isUpdating = true;
            if (gameState == GameState.PLAYING) {
                updateGame();
            }
            graphicsManager.drawScreen();
            isUpdating = false;
        }
    }

    static void updateGame() {
        // health -= 10;
        if (health < 0) {
            setGameState(GameState.HIGH_SCORE);
            HighScorePanel.addScore(score);
        }
        frameCount++;
        if (escapingEnemyTracker > escapingEnemyPerFrameReduction)
            escapingEnemyTracker -= escapingEnemyPerFrameReduction;
        else
            escapingEnemyTracker = 0;

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
            } else if (w.getX() < 1040 && !kill) {
                newPlayerBullets.add(w);
            }
        }
        playerFiredWeapons = newPlayerBullets;
        killMissiles = false;

        ArrayList<EnemyWeaponParent> newEnemyWeapons = new ArrayList<>();
        for (EnemyWeaponParent w : enemyFiredWeapons) {
            w.update();
            if (w.getX() > 0) {
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
                    shipEscaped();
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
            audioManager.playSoundFrame(frameCount);
    }

    static void shipEscaped() {
        escapingEnemyTracker++;
        int healthLoss = (int) Math.pow(defaultHealthLossPerEnemy, 1 / escapingEnemyTracker);
        health -= healthLoss;
    }

    static void checkEnemyBulletCollision() {
        for (int j = enemyShips.size() - 1; j >= 0; j--) {
            EnemyShip e = enemyShips.get(j);

            // checks for collision with laser blast
            if (laserBlast != null) {
                int distance = Math.abs((laserBlast.getX() + laserBlast.getRadius()) - (e.getX() + e.getRadius()));
                if (distance < e.getRadius() + laserBlast.getRadius()) {
                    if (e.isKillable(LaserBlast.class)) {
                        spawnExp(e.getX(), e.getY(), e.getRadius(), e.getType(), 0);
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
                            spawnExp(e.getX(), e.getY(), e.getRadius(), e.getType(), 0);
                            enemyShips.remove(j);
                        }
                        playerFiredWeapons.remove(i);

                    }
                    if (weapon.getType() == WeaponType.PLAYER_MISSILE) {
                        ((Missile) weapon).hitEnemy();
                        playerFiredWeapons.remove(i);
                    }
                    if (weapon.getType() == WeaponType.PLAYER_BURST) {
                        spawnExp(e.getX(), e.getY(), e.getRadius(), e.getType(), 0);
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
                        distance(explosion.getX(), explosion.getY(), explosion.getRadius(),
                                enemy.getX(), enemy.getY(), enemy.getRadius()) < 2 * (enemy.getRadius() + explosion.getRadius())) {
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
            if (distance(e.getX(), e.getY(), e.getRadius(),
                    player.getX(), player.getY(), player.getRadius()) < e.getRadius() + player.getRadius()) {
                if (e.isKillable(PlayerShip.class)) {
                    e.killShip(0);
                    enemyShips.remove(i);
                    health -= 3;
                }

            }
        }
    }

    static void checkPlayerEnemyWeaponCollision() {
        for (int i = enemyFiredWeapons.size() - 1; i >= 0; i--) {
            EnemyWeaponParent w = enemyFiredWeapons.get(i);
            if (distance(w.getX(), w.getY(), w.getRadius(),
                    player.getX(), player.getY(), player.getRadius()) < w.getRadius() + player.getRadius()) {
                enemyFiredWeapons.remove(i);
                player.hitByWeapon(w);
                audioManager.addSoundToFrame(AudioClipType.ENEMY_EMP_HIT);
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

    public static void spawnExp(int x, int y, int radius, ExplosionType explosionType, int catalystSeparation) {
        x += radius;
        y += radius;
        int expRadius = Explosion.expRadii.get(explosionType);
        x -= expRadius;
        y -= expRadius;

        explosions.add(new Explosion(x, y, catalystSeparation, explosionType));

        audioManager.addSoundToFrame(Globals.getExplosionAudioClipType(explosionType));

        int[] shake = makeShake(x, y, explosionShake.get(explosionType));

        graphicsManager.addScreenShake(shake);
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
            Random rand = new Random();
            graphicsManager.addScreenShake(0,
                    (rand.nextInt(2) == 0 ? laserBlastShake : -1 * laserBlastShake));
        }
    }

    static void createMissile(int x, int y) {
        Missile missile = new Missile(x, y);
        playerFiredWeapons.add(missile);
        playWeaponSound(WeaponType.PLAYER_MISSILE);
        graphicsManager.addScreenShake(2, 0);
        graphicsManager.addPlayerShake(-8, 0);
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

    public static void addKillScore(EnemyType type, int catalystSeparation) {
        int killPoints = Globals.getEnemyShipPointValue(type);
        double multiplier = catalystSeparation / 2 + 1;

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

    private static int[] makeShake(double x, double y, double shake) {
        Random rand = new Random();
        double xFraction = rand.nextDouble() * 2;
        int xDirection = rand.nextInt(2) == 0 ? 1 : -1;
        int yDirection = rand.nextInt(2) == 0 ? 1 : -1;

        int xShake = xDirection * (int) (xFraction * shake);
        int yShake = yDirection * (int) ((1 - xFraction) * shake);

        return new int[]{xShake, yShake};

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

    public static int getHealth() {
        return health;
    }

    public static int getMaxHealth() {
        return maxHealth;
    }

    public static int getPlayerMaxHeat() {
        return player.getMaxHeat();
    }

    public static int getPlayerHeat() {
        return player.getHeat();
    }

    public static double getPlayerXPos() {
        return player.getX();
    }

    public static double getPlayerYPos() {
        return player.getY();
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

    public static boolean isAudioMuted() {
        return audioManager.isMuted();
    }

    public static void toggleMute() {
        audioManager.toggleMute();
    }

    public static void toggleSoundMode() {
        isSoundExperiential = !isSoundExperiential;
        audioManager.setSoundType(isSoundExperiential);
    }

    public static void setGameState(GameState newGameState) {
        if (newGameState == GameState.PLAYING) {
            SpawnController.setStartTime(System.currentTimeMillis());
            resetGame();
            resetTimeline();
            audioManager.playMusic(MusicType.GAME);
        }
        if (newGameState == GameState.MENU) {
            audioManager.playMusic(MusicType.MENU);
        }
        if (newGameState == GameState.HIGH_SCORE) {
            audioManager.playMusic(MusicType.HIGHSCORE);
            HighScorePanel.loadScores();
        }

        gameState = newGameState;
    }

    public static void killMissiles() {
        killMissiles = true;
    }
}
