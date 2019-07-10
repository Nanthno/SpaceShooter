package src.main.java;

import src.main.java.density.DensityMap;
import src.main.java.enemy.EnemyBasic;
import src.main.java.enemy.EnemyFuel;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;
import src.main.java.spawn.SpawnController;
import src.main.java.spawn.TimeStampEvent;
import src.main.java.spawn.TimelineUtil;

import java.util.*;

public class Controller {

    static GraphicsManager graphicsManager;

    static ArrayList<EnemyShip> enemyShips = new ArrayList<EnemyShip>();

    static PlayerShip player = new PlayerShip();
    static ArrayList<PlayerBullet> playerBullets = new ArrayList<PlayerBullet>();
    static LaserBlast laserBlast = null;

    static ArrayList<Explosion> explosions = new ArrayList<Explosion>();

    static DensityMap densityMap;

    static final int frameRate = 15;

    // probability of a spawn occurring on each tick
    static double spawnChance0 = 0;
    static double spawnChance1 = 0;
    static double spawnChance2 = 1;

    static SpawnController spawnController = new SpawnController();
    //static TimelineController timelineController = new TimelineController();

    private static Queue<TimeStampEvent> timeline = null;

    static int score = 0;

    GameState gameState = GameState.MENU;
    private enum GameState {MENU, PLAYING, HIGHSCORE}

    public static void main(String[] args) {

        graphicsManager = new GraphicsManager();

        timeline = TimelineUtil.readTimelinesToQueue();

        gameLoop();

    }

    static void update() {
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
        ArrayList<EnemyShip> newEnemyShips = new ArrayList<EnemyShip>();
        for (int i = 0; i < enemyShips.size(); i++) {
            EnemyShip e = enemyShips.get(i);
            boolean offScreen = e.updateShip();
            if (offScreen) {
                player.looseHealth(10);
            } else {
                newEnemyShips.add(e);
            }
        }
        enemyShips = newEnemyShips;

        // spawns new enemyShips
        Random rand = new Random();
        double spawns = rand.nextDouble();

        if (spawns < spawnChance0) {
            // randomly chooses a y position for the ship's spawn point with a 32 pixel margin
            int y = rand.nextInt(Globals.screenHeight - 64) + 32;
            double xSpeed = rand.nextDouble() * (EnemyBasic.maxSpeed - EnemyBasic.minSpeed) + EnemyBasic.minSpeed;
            enemyShips.add(new EnemyBasic(y, xSpeed));

        }

        spawns = rand.nextDouble();
        if (spawns < spawnChance1) {
            int y = rand.nextInt(Globals.screenHeight - 64) + 32;
            double xSpeed = EnemyFuel.minSpeed + rand.nextDouble() * (EnemyFuel.maxSpeed - EnemyFuel.minSpeed);
            enemyShips.add(new EnemyFuel(y, xSpeed));
        }

        spawns = rand.nextDouble();
        if (spawns < spawnChance2) {
            List<EnemyShip> shipsSpawning = spawnController.onTick();
            enemyShips.addAll(shipsSpawning);
        }

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

        graphicsManager.drawScreen(player.getHealth(), player.getHeat());
    }

    static void checkEnemyBulletCollision() {
        for (int j = enemyShips.size() - 1; j >= 0; j--) {
            EnemyShip e = enemyShips.get(j);

            // checks for collision with laser blast
            if (laserBlast != null) {
                int distance = Math.abs(laserBlast.getx() - e.getx());
                if (distance < e.getRadius() + laserBlast.getRadius()) {
                    spawnExp(e.getx(), e.gety(), e.getRadius(), e.getType(), 0);
                    enemyShips.remove(j);
                    continue;
                }
            }

            // checks for collision with a player bullet
            for (int i = playerBullets.size() - 1; i >= 0; i--) {
                PlayerBullet b = playerBullets.get(i);

                if (distance(b.getx(), b.gety(), b.getRadius(),
                        e.getx(), e.gety(), e.getRadius()) < e.getRadius() + b.getRadius()) {
                    spawnExp(e.getx(), e.gety(), e.getRadius(), e.getType(), 0);
                    playerBullets.remove(i);
                    enemyShips.remove(j);

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
                    enemy.killShip(explosion.getCatalystSeparation() + 1);
                    enemyShips.remove(j);

                }
            }
        }
    }

    static void checkPlayerEnemyCollision() {
        for (int i = enemyShips.size() - 1; i >= 0; i--) {
            EnemyShip e = enemyShips.get(i);
            if (distance(e.getx(), e.gety(), e.getRadius(),
                    player.getx(), player.gety(), player.getRadius()) < e.getRadius() + player.getRadius()) {
                e.killShip(0);
                enemyShips.remove(i);
                //player.shipCollision();
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

    public static void spawnExp(int x, int y, int shipR, EnemyType enemyType, int catalistSeperation) {

        int explosionType = EnemyType.getExplosionType(enemyType);

        // compensates location for ship radius
        x += shipR;
        y += shipR;
        // compensates location for explosion radius
        x -= Explosion.expRadiusArray[explosionType];
        y -= Explosion.expRadiusArray[explosionType];


        if (explosionType == 0) {
            explosions.add(new SmallExplosion(x, y, catalistSeperation));
        }
        if (explosionType == 1) {
            explosions.add(new FuelExplosion(x, y, catalistSeperation));
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
        double multiplier = catalistSeperation/2 + 1;

        killPoints *= multiplier;
        score += killPoints;
    }

    static ArrayList<EnemyShip> getEnemyArray() {
        return enemyShips;
    }

    public static DensityMap getDensityMap() {
        return densityMap;
    }

    static PlayerShip getPlayerShip() {
        return player;
    }

    static ArrayList<PlayerBullet> getPlayerBullets() {
        return playerBullets;
    }

    static LaserBlast getLaserBlast() {
        return laserBlast;
    }

    static ArrayList<Explosion> getExplosions() {
        return explosions;
    }

    static int getPlayerHealth() {
        return player.getHealth();
    }

    static int getPlayerMaxHealth() {
        return player.getMaxHealth();
    }

    static int getPlayerMaxHeat() {
        return player.getMaxHeat();
    }

    static int getPlayerHeat() {
        return player.getHeat();
    }

    static int getCharge() {
        return player.getCharge();
    }

    static int getMaxCharge() {
        return player.getMaxCharge();
    }

    public static int getScore() {
        return score;
    }
}
