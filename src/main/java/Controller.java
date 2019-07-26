package src.main.java;

import src.main.java.density.DensityMap;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;
import src.main.java.graphics.GraphicsManager;
import src.main.java.spawn.SpawnController;
import src.main.java.spawn.TimeStampEvent;
import src.main.java.spawn.TimelineUtil;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class Controller {

    static GraphicsManager graphicsManager;

    static List<EnemyShip> enemyShips = new ArrayList<>();

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

    static GameState gameState = GameState.MENU;

    static Input input;

    static List<EnemyShip> newEnemyShips = new ArrayList<>();

    public static void main(String[] args) {

        input = new Input();
        graphicsManager = new GraphicsManager();

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
        if (timeline.size() > 0)
            timeline = spawnController.updateSpawnProbabilities(timeline);

        densityMap = new DensityMap(enemyShips);

        player.update();

        // updates weapons
        ArrayList<PlayerWeaponParent> newPlayerBullets = new ArrayList<>();
        for (PlayerWeaponParent w : playerFiredWeapons) {
            w.update();
            if (w.getx() < 1040) {
                newPlayerBullets.add(w);
            }
        }
        playerFiredWeapons = newPlayerBullets;

        ArrayList<EnemyWeaponParent> newEnemyWeapons = new ArrayList<>();
        for(EnemyWeaponParent w : enemyFiredWeapons) {
            w.update();
            if(w.getx() > 0) {
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
                    if (weapon.getType() == WeaponType.BULLET) {
                        PlayerBullet b = (PlayerBullet) weapon;
                        if (e.isKillable(PlayerBullet.class)) {
                            spawnExp(e.getx(), e.gety(), e.getRadius(), e.getType(), 0);
                            enemyShips.remove(j);
                        }
                        playerFiredWeapons.remove(i);

                    }
                    if (weapon.getType() == WeaponType.MISSILE) {
                        ((Missile)weapon).hitEnemy();
                        playerFiredWeapons.remove(i);
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
        for(int i = enemyFiredWeapons.size()-1; i >= 0; i--) {
            EnemyWeaponParent w = enemyFiredWeapons.get(i);
            if(distance(w.getx(), w.gety(), w.getRadius(),
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
    }

    public static void spawnShooterBullet(int x, int y, double dx) {
        ShooterBullet bullet = new ShooterBullet(x, y, dx);
        enemyFiredWeapons.add(bullet);
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

    static void addBullet(PlayerBullet b) {
        playerFiredWeapons.add(b);
    }

    static void fireBlast(int x) {
        if (laserBlast == null) {
            laserBlast = new LaserBlast(x);
        }
    }

    static void createMissile(int x, int y) {
        Missile missile = new Missile(x, y);
        playerFiredWeapons.add(missile);
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

    public static void setGameState(GameState newGameState) {
        if (newGameState == GameState.PLAYING) {
            SpawnController.setStartTime(System.currentTimeMillis());
            resetTimeline();
        }
        gameState = newGameState;
    }
}
