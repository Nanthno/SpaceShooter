package src.main.java;

import src.main.java.density.DensityMap;
import src.main.java.enemy.EnemyShielder;
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

    static List<EnemyShip> enemyShips = new ArrayList<EnemyShip>();

    static PlayerShip player = new PlayerShip();
    static ArrayList<PlayerBullet> playerBullets = new ArrayList<PlayerBullet>();
    static LaserBlast laserBlast = null;

    static ArrayList<Explosion> explosions = new ArrayList<Explosion>();

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

        // updates player bullets
        ArrayList<PlayerBullet> newPlayerBullets = new ArrayList<PlayerBullet>();
        for (PlayerBullet b : playerBullets) {
            b.update();
            if (b.getx() < 1040) {
                newPlayerBullets.add(b);
            }
        }
        playerBullets = newPlayerBullets;

        if (laserBlast != null) {
            boolean destroy = laserBlast.update();
            if (destroy) {
                laserBlast = null;
            }
        }

        // updates enemyShips
        for (int i = 0; i < enemyShips.size(); i++) {
            EnemyShip e = enemyShips.get(i);
            boolean offScreen = e.updateShip();
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
    }

    static void checkEnemyBulletCollision() {
        for (int j = enemyShips.size() - 1; j >= 0; j--) {
            EnemyShip e = enemyShips.get(j);

            // checks for collision with laser blast
            if (laserBlast != null) {
                int distance = Math.abs((laserBlast.getx()+laserBlast.getRadius()) - (e.getx()+e.getRadius()));
                if (distance < e.getRadius() + laserBlast.getRadius()) {
                    if (e.isKillable(LaserBlast.class)) {
                        spawnExp(e.getx(), e.gety(), e.getRadius(), e.getType(), 0);
                        enemyShips.remove(j);
                        continue;
                    }
                }
            }

            // checks for collision with a player bullet
            for (int i = playerBullets.size() - 1; i >= 0; i--) {
                PlayerBullet b = playerBullets.get(i);
                if(e.collideWithBullet(b)) {
                    if (e.isKillable(PlayerBullet.class)) {
                        spawnExp(e.getx(), e.gety(), e.getRadius(), e.getType(), 0);
                        enemyShips.remove(j);
                    }
                    playerBullets.remove(i);

                }
            }
        }
    }

    static void checkEnemyExplosionCollision() {
        for (int i = explosions.size() - 1; i >= 0; i--) {
            Explosion explosion = explosions.get(i);
            for (int j = enemyShips.size() - 1; j >= 0; j--) {
                EnemyShip enemy = enemyShips.get(j);
                if (explosion.getStage() > 2 &&
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

        // compensates location for ship radius
        x += shipR;
        y += shipR;
        // compensates location for explosion radius
        x -= Explosion.expRadii.get(explosionType);
        y -= Explosion.expRadii.get(explosionType);


        if (explosionType == ExplosionType.SMALL) {
            explosions.add(new Explosion(x, y, catalystSeparation, ExplosionType.SMALL));
        }
        if (explosionType == ExplosionType.FUEL) {
            explosions.add(new Explosion(x, y, catalystSeparation, ExplosionType.FUEL));
        }
        if (explosionType == ExplosionType.MEDIUM) {
            explosions.add(new Explosion(x, y, catalystSeparation, ExplosionType.MEDIUM));
        }
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
        playerBullets.add(b);
    }

    static void fireBlast(int x) {
        if (laserBlast == null) {
            laserBlast = new LaserBlast(x);
        }
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

    public static ArrayList<PlayerBullet> getPlayerBullets() {
        return playerBullets;
    }

    public static LaserBlast getLaserBlast() {
        return laserBlast;
    }

    public static ArrayList<Explosion> getExplosions() {
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
